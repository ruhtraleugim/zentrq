package com.spectre.zentrq.auth;

import com.spectre.zentrq.auth.dto.*;
import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.cliente.ClienteRepository;
import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.profissional.ProfissionalRepository;
import com.spectre.zentrq.shared.email.EmailService;
import com.spectre.zentrq.shared.exception.BusinessException;
import com.spectre.zentrq.shared.exception.ResourceNotFoundException;
import com.spectre.zentrq.user.User;
import com.spectre.zentrq.user.UserRepository;
import com.spectre.zentrq.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        String email = normalize(request.email());

        if (userRepository.existsByEmail(email)) {
            User existing = userRepository.findByEmail(email).orElseThrow();
            if (!existing.isEmailVerified()) {
                /* OTP DISABLED — auto-verify existing unverified user
                String otp = generateOtp();
                existing.setOtpCode(otp);
                existing.setOtpExpiresAt(LocalDateTime.now().plusMinutes(15));
                userRepository.save(existing);
                sendOtpAsync(email, otp);
                throw new BusinessException("Email não verificado");
                */
                existing.setEmailVerified(true);
                userRepository.save(existing);
                return;
            }
            throw new BusinessException("Email já cadastrado");
        }

        /* OTP DISABLED — auto-verify on register
        String otp = generateOtp();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);
        */

        if (request.role() == UserRole.CLIENTE) {
            Cliente cliente = new Cliente();
            /* OTP DISABLED */ fill(cliente, request, email, null, null);
            clienteRepository.save(cliente);
        } else {
            Profissional prof = new Profissional();
            /* OTP DISABLED */ fill(prof, request, email, null, null);
            profissionalRepository.save(prof);
        }

        /* OTP DISABLED
        sendOtpAsync(email, otp);
        */
    }

    @Override
    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        String email = normalize(request.email());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!request.otp().equals(user.getOtpCode())) {
            throw new BusinessException("Código inválido");
        }
        if (user.getOtpExpiresAt() == null || LocalDateTime.now().isAfter(user.getOtpExpiresAt())) {
            throw new BusinessException("Código expirado");
        }

        user.setEmailVerified(true);
        user.setOtpCode(null);
        user.setOtpExpiresAt(null);
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user), user.getRole().name());
    }

    @Override
    @Transactional
    public void resendOtp(String email) {
        String normalizedEmail = normalize(email);
        User user = userRepository.findByEmail(normalizedEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (user.isEmailVerified()) {
            throw new BusinessException("Email já verificado");
        }
        String otp = generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        sendOtpAsync(normalizedEmail, otp);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String email = normalize(request.email());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("Credenciais inválidas"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }
        if (!user.isEmailVerified()) {
            throw new BusinessException("Email não verificado");
        }
        return new AuthResponse(jwtService.generateToken(user), user.getRole().name());
    }

    private void fill(User user, RegisterRequest req, String email, String otp, LocalDateTime expiresAt) {
        user.setName(req.name());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(req.role());
        user.setOtpCode(otp);
        user.setOtpExpiresAt(expiresAt);
        user.setEmailVerified(otp == null); /* OTP DISABLED: auto-verify when no OTP */
    }

    private void sendOtpAsync(String email, String otp) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try { emailService.sendOtp(email, otp); }
                catch (Exception e) { log.warn("OTP email failed for {}: {}", email, e.getMessage()); }
            }
        });
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String generateOtp() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}

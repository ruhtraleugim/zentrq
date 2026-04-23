package com.spectre.zentrq.auth;

import com.spectre.zentrq.auth.dto.*;
import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.cliente.ClienteRepository;
import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.profissional.ProfissionalRepository;
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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        String email = normalize(request.email());

        if (userRepository.existsByEmail(email)) {
            User existing = userRepository.findByEmail(email).orElseThrow();
            if (!existing.isEmailVerified()) {
                existing.setEmailVerified(true);
                userRepository.save(existing);
            } else {
                throw new BusinessException("Email já cadastrado");
            }
            return;
        }

        if (request.role() == UserRole.CLIENTE) {
            Cliente cliente = new Cliente();
            fill(cliente, request, email);
            clienteRepository.save(cliente);
        } else {
            Profissional prof = new Profissional();
            fill(prof, request, email);
            profissionalRepository.save(prof);
        }
    }

    @Override
    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        String email = normalize(request.email());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        user.setEmailVerified(true);
        user.setOtpCode(null);
        user.setOtpExpiresAt(null);
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user), user.getRole().name());
    }

    @Override
    @Transactional
    public void resendOtp(String email) {
        userRepository.findByEmail(normalize(email))
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
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

    private void fill(User user, RegisterRequest req, String email) {
        user.setName(req.name());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(req.role());
        user.setEmailVerified(true);
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}

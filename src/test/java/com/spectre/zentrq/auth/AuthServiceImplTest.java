package com.spectre.zentrq.auth;

import com.spectre.zentrq.auth.dto.*;
import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.cliente.ClienteRepository;
import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.profissional.ProfissionalRepository;
import com.spectre.zentrq.shared.exception.BusinessException;
import com.spectre.zentrq.user.UserRepository;
import com.spectre.zentrq.user.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock ProfissionalRepository profissionalRepository;
    @Mock JwtService jwtService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JavaMailSender mailSender;

    @InjectMocks AuthServiceImpl authService;

    @Test
    void register_withExistingEmail_throwsBusinessException() {
        when(userRepository.existsByEmail("exists@test.com")).thenReturn(true);
        var req = new RegisterRequest("Name", "exists@test.com", "pass123", UserRole.CLIENTE);
        assertThrows(BusinessException.class, () -> authService.register(req));
    }

    @Test
    void register_newCliente_savesClienteAndSendsEmail() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(clienteRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mailSender.createMimeMessage()).thenReturn(mock(jakarta.mail.internet.MimeMessage.class));

        authService.register(new RegisterRequest("Name", "new@test.com", "pass123", UserRole.CLIENTE));

        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void verifyOtp_withExpiredOtp_throwsBusinessException() {
        Cliente user = new Cliente();
        user.setEmail("a@b.com");
        user.setOtpCode("123456");
        user.setOtpExpiresAt(LocalDateTime.now().minusMinutes(1));
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class,
            () -> authService.verifyOtp(new VerifyOtpRequest("a@b.com", "123456")));
    }

    @Test
    void verifyOtp_withWrongOtp_throwsBusinessException() {
        Cliente user = new Cliente();
        user.setEmail("a@b.com");
        user.setOtpCode("111111");
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(10));
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class,
            () -> authService.verifyOtp(new VerifyOtpRequest("a@b.com", "999999")));
    }

    @Test
    void login_withUnverifiedEmail_throwsBusinessException() {
        Cliente user = new Cliente();
        user.setPassword("hashed");
        user.setEmailVerified(false);
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);

        assertThrows(BusinessException.class,
            () -> authService.login(new LoginRequest("a@b.com", "pass")));
    }

    @Test
    void login_withWrongPassword_throwsBusinessException() {
        Cliente user = new Cliente();
        user.setPassword("hashed");
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(BusinessException.class,
            () -> authService.login(new LoginRequest("a@b.com", "wrong")));
    }
}

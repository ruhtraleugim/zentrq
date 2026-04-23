package com.spectre.zentrq.auth;

import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
            "test-secret-that-is-long-enough-for-hmac-sha256-algorithm-ok");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L);
    }

    @Test
    void generateToken_thenExtractUserId_returnsCorrectId() {
        Cliente user = new Cliente();
        ReflectionTestUtils.setField(user, "id", 42L);
        user.setRole(UserRole.CLIENTE);
        user.setEmail("test@test.com");

        String token = jwtService.generateToken(user);
        Long userId = jwtService.extractUserId(token);

        assertThat(userId).isEqualTo(42L);
    }

    @Test
    void isTokenValid_withValidToken_returnsTrue() {
        Cliente user = new Cliente();
        ReflectionTestUtils.setField(user, "id", 1L);
        user.setRole(UserRole.CLIENTE);
        user.setEmail("a@b.com");

        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_withGarbage_returnsFalse() {
        assertThat(jwtService.isTokenValid("not.a.token")).isFalse();
    }
}

package com.spectre.zentrq.auth;

import com.spectre.zentrq.auth.dto.*;
import com.spectre.zentrq.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<MessageResponse> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MessageResponse("Cadastro realizado. Verifique seu email."));
    }

    @PostMapping("/auth/verify")
    public ResponseEntity<AuthResponse> verify(@RequestBody @Valid VerifyOtpRequest request) {
        System.out.println("EMAIL: " + request.email());
        System.out.println("OTP: " + request.otp());
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/auth/resend-otp")
    public ResponseEntity<MessageResponse> resendOtp(@RequestBody @Valid ResendOtpRequest request) {
        authService.resendOtp(request.email());
        return ResponseEntity.ok(new MessageResponse("Novo código enviado."));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserMeResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserMeResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name()));
    }

    public record UserMeResponse(Long id, String name, String email, String role) {}
}

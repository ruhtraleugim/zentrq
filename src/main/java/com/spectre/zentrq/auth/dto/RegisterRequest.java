package com.spectre.zentrq.auth.dto;
import com.spectre.zentrq.user.UserRole;
import jakarta.validation.constraints.*;
public record RegisterRequest(
    @NotBlank @Size(min = 2, max = 150) String name,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 6) String password,
    @NotNull UserRole role
) {}

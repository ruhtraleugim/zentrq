package com.spectre.zentrq.auth.dto;
import jakarta.validation.constraints.*;
public record ResendOtpRequest(@NotBlank @Email String email) {}

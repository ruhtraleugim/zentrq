package com.spectre.zentrq.auth;

import com.spectre.zentrq.auth.dto.*;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse verifyOtp(VerifyOtpRequest request);
    void resendOtp(String email);
    AuthResponse login(LoginRequest request);
}
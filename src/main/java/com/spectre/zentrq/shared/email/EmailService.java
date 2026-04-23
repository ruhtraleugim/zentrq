package com.spectre.zentrq.shared.email;

public interface EmailService {
    void sendOtp(String to, String otp);
}
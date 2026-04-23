package com.spectre.zentrq.shared.email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ResendEmailService implements EmailService {

    private final Resend resend;
    private final String from;

    public ResendEmailService(
            @Value("${app.resend.api-key}") String apiKey,
            @Value("${app.resend.from}") String from) {
        this.resend = new Resend(apiKey);
        this.from = from;
    }

    @Override
    public void sendOtp(String to, String otp) {
        try {
            CreateEmailOptions email = CreateEmailOptions.builder()
                    .from(from)
                    .to(to)
                    .subject("Seu código de verificação — Zentro")
                    .html("<p>Seu código de verificação é: <strong style=\"font-size:24px\">" + otp + "</strong></p>" +
                          "<p>Expira em 15 minutos.</p>")
                    .build();

            resend.emails().send(email);
            log.info("OTP email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Falha ao enviar email de verificação", e);
        }
    }
}

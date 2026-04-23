package com.spectre.zentrq.payment;

import com.spectre.zentrq.payment.abacatepay.AbacateWebhookPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final PaymentService paymentService;

    @Value("${app.abacatepay.webhook-secret}")
    private String webhookSecret;

    @PostMapping("/abacatepay")
    public ResponseEntity<Void> handle(
            @RequestHeader(value = "X-Abacate-Token", required = false) String token,
            @RequestBody AbacateWebhookPayload payload) {

        if (!constantTimeEquals(webhookSecret, token == null ? "" : token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        paymentService.processWebhook(payload);
        return ResponseEntity.ok().build();
    }

    private boolean constantTimeEquals(String a, String b) {
        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);
        int result = aBytes.length ^ bBytes.length;
        int len = Math.min(aBytes.length, bBytes.length);
        for (int i = 0; i < len; i++) result |= aBytes[i] ^ bBytes[i];
        return result == 0;
    }
}

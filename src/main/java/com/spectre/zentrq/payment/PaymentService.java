package com.spectre.zentrq.payment;

import com.spectre.zentrq.payment.abacatepay.AbacateWebhookPayload;

public interface PaymentService {
    void processWebhook(AbacateWebhookPayload payload);
}

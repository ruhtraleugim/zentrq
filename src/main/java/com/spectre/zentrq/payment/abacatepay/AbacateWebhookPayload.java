package com.spectre.zentrq.payment.abacatepay;

public record AbacateWebhookPayload(
    String id,
    String status,
    String event
) {}

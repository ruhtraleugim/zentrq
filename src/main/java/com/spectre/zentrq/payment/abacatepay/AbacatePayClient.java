package com.spectre.zentrq.payment.abacatepay;

import java.math.BigDecimal;

public interface AbacatePayClient {
    AbacateBillingResponse createBilling(
        Long jobId,
        String jobTitle,
        BigDecimal amount,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerTaxId
    );
}

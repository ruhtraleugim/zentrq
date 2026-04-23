package com.spectre.zentrq.payment.abacatepay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AbacatePayClientImpl implements AbacatePayClient {

    private final WebClient webClient;
    private final String frontendUrl;

    public AbacatePayClientImpl(
            @Value("${app.abacatepay.base-url}") String baseUrl,
            @Value("${app.abacatepay.api-key}") String apiKey,
            @Value("${app.frontend-url:http://localhost:3000}") String frontendUrl) {
        this.frontendUrl = frontendUrl;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public AbacateBillingResponse createBilling(
            Long jobId, String jobTitle, BigDecimal amount,
            String customerName, String customerEmail,
            String customerPhone, String customerTaxId) {

        long amountInCents = amount.multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP).longValue();

        String phone = (customerPhone != null && !customerPhone.isBlank()) ? customerPhone : "11999999999";
        String taxId = (customerTaxId != null && !customerTaxId.isBlank()) ? customerTaxId : "529.982.247-25";

        Map<String, Object> body = Map.of(
            "frequency", "ONE_TIME",
            "methods", List.of("PIX"),
            "products", List.of(Map.of(
                "externalId", "job-" + jobId,
                "name", jobTitle,
                "description", "Serviço contratado via ZentrQ",
                "quantity", 1,
                "price", amountInCents
            )),
            "customer", Map.of(
                "name", customerName,
                "email", customerEmail,
                "cellphone", phone,
                "taxId", taxId
            ),
            "returnUrl", frontendUrl + "/cliente/jobs",
            "completionUrl", frontendUrl + "/cliente/jobs"
        );

        AbacateApiResponse response = webClient.post()
                .uri("/v1/billing/create")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AbacateApiResponse.class)
                .block();

        if (response == null || response.data() == null) {
            throw new IllegalStateException("AbacatePay retornou resposta vazia");
        }
        if (response.error() != null && !response.error().isBlank()) {
            throw new IllegalStateException("AbacatePay error: " + response.error());
        }

        log.info("AbacatePay billing created: id={} url={}", response.data().id(), response.data().url());
        return response.data();
    }

    record AbacateApiResponse(AbacateBillingResponse data, String error) {}
}

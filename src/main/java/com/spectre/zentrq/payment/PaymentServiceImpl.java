package com.spectre.zentrq.payment;

import com.spectre.zentrq.job.JobRepository;
import com.spectre.zentrq.job.JobStatus;
import com.spectre.zentrq.payment.abacatepay.AbacateWebhookPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final JobRepository jobRepository;

    @Override
    @Transactional
    public void processWebhook(AbacateWebhookPayload payload) {
        paymentRepository.findByExternalId(payload.id()).ifPresent(payment -> {
            if (payment.getStatus() == PaymentStatus.PAID) {
                log.info("Webhook idempotente ignorado: externalId={}", payload.id());
                return;
            }
            PaymentStatus previous = payment.getStatus();

            if ("PAID".equalsIgnoreCase(payload.status())) {
                payment.setStatus(PaymentStatus.PAID);
                paymentRepository.save(payment);

                jobRepository.findById(payment.getJobId()).ifPresent(job -> {
                    job.setStatus(JobStatus.IN_PROGRESS);
                    jobRepository.save(job);
                    log.info("Payment PAID: externalId={} jobId={} {} → PAID",
                        payload.id(), job.getId(), previous);
                });
            } else if ("FAILED".equalsIgnoreCase(payload.status())) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                log.info("Payment FAILED: externalId={} {} → FAILED", payload.id(), previous);
            }
        });
    }
}

package com.spectre.zentrq.payment;

import com.spectre.zentrq.job.Job;
import com.spectre.zentrq.job.JobRepository;
import com.spectre.zentrq.job.JobStatus;
import com.spectre.zentrq.payment.abacatepay.AbacateWebhookPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock PaymentRepository paymentRepository;
    @Mock JobRepository jobRepository;

    @InjectMocks PaymentServiceImpl paymentService;

    @Test
    void processWebhook_paidEvent_updatesPaymentAndJob() {
        Job job = new Job();
        ReflectionTestUtils.setField(job, "id", 1L);
        job.setStatus(JobStatus.ACCEPTED);

        Payment payment = new Payment();
        ReflectionTestUtils.setField(payment, "id", 1L);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setJobId(1L);
        payment.setExternalId("ext-123");

        when(paymentRepository.findByExternalId("ext-123")).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        paymentService.processWebhook(new AbacateWebhookPayload("ext-123", "PAID", "payment.paid"));

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(job.getStatus()).isEqualTo(JobStatus.IN_PROGRESS);
        verify(paymentRepository).save(payment);
        verify(jobRepository).save(job);
    }

    @Test
    void processWebhook_alreadyPaid_isIdempotent() {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PAID);
        when(paymentRepository.findByExternalId("ext-456")).thenReturn(Optional.of(payment));

        paymentService.processWebhook(new AbacateWebhookPayload("ext-456", "PAID", "payment.paid"));

        verify(paymentRepository, never()).save(any());
        verify(jobRepository, never()).save(any());
    }

    @Test
    void processWebhook_unknownExternalId_doesNothing() {
        when(paymentRepository.findByExternalId("unknown")).thenReturn(Optional.empty());
        paymentService.processWebhook(new AbacateWebhookPayload("unknown", "PAID", "payment.paid"));
        verify(paymentRepository, never()).save(any());
    }
}

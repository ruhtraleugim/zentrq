package com.spectre.zentrq.job;

import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.cliente.ClienteRepository;
import com.spectre.zentrq.job.dto.CreateJobRequest;
import com.spectre.zentrq.payment.PaymentRepository;
import com.spectre.zentrq.payment.PaymentStatus;
import com.spectre.zentrq.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock JobRepository jobRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock PaymentRepository paymentRepository;

    @InjectMocks JobServiceImpl jobService;

    @Test
    void createJob_savesWithOpenStatus() {
        Cliente cliente = new Cliente();
        ReflectionTestUtils.setField(cliente, "id", 1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(jobRepository.save(any())).thenAnswer(i -> {
            Job j = i.getArgument(0);
            ReflectionTestUtils.setField(j, "id", 10L);
            return j;
        });

        var req = new CreateJobRequest("Pintura", "Pintar sala", JobType.NORMAL, "Fortaleza");
        var response = jobService.create(1L, req);

        assertThat(response.status()).isEqualTo(JobStatus.OPEN);
        assertThat(response.city()).isEqualTo("Fortaleza");
    }

    @Test
    void cancelJob_whenInProgress_throwsBusinessException() {
        Cliente cliente = new Cliente();
        ReflectionTestUtils.setField(cliente, "id", 1L);
        Job job = new Job();
        ReflectionTestUtils.setField(job, "id", 5L);
        job.setClient(cliente);
        job.setStatus(JobStatus.IN_PROGRESS);
        when(jobRepository.findById(5L)).thenReturn(Optional.of(job));

        assertThrows(BusinessException.class, () -> jobService.cancel(5L, 1L));
    }

    @Test
    void cancelJob_byNonOwner_throwsBusinessException() {
        Cliente cliente = new Cliente();
        ReflectionTestUtils.setField(cliente, "id", 1L);
        Job job = new Job();
        job.setClient(cliente);
        job.setStatus(JobStatus.OPEN);
        when(jobRepository.findById(5L)).thenReturn(Optional.of(job));

        assertThrows(BusinessException.class, () -> jobService.cancel(5L, 99L));
    }
}

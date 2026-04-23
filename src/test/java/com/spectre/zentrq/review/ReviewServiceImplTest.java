package com.spectre.zentrq.review;

import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.cliente.ClienteRepository;
import com.spectre.zentrq.job.Job;
import com.spectre.zentrq.job.JobRepository;
import com.spectre.zentrq.job.JobStatus;
import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.profissional.ProfissionalRepository;
import com.spectre.zentrq.proposal.ProposalRepository;
import com.spectre.zentrq.review.dto.CreateReviewRequest;
import com.spectre.zentrq.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock ReviewRepository reviewRepository;
    @Mock JobRepository jobRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock ProfissionalRepository profissionalRepository;
    @Mock ProposalRepository proposalRepository;

    @InjectMocks ReviewServiceImpl reviewService;

    private Job makeJob(JobStatus status, Long clientId) {
        Cliente c = new Cliente();
        ReflectionTestUtils.setField(c, "id", clientId);
        Job job = new Job();
        ReflectionTestUtils.setField(job, "id", 1L);
        job.setStatus(status);
        job.setClient(c);
        return job;
    }

    @Test
    void create_whenJobNotCompleted_throwsBusinessException() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(makeJob(JobStatus.IN_PROGRESS, 1L)));
        assertThrows(BusinessException.class,
            () -> reviewService.create(1L, new CreateReviewRequest(1L, 5, "ok")));
    }

    @Test
    void create_byNonOwner_throwsBusinessException() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(makeJob(JobStatus.COMPLETED, 1L)));
        assertThrows(BusinessException.class,
            () -> reviewService.create(99L, new CreateReviewRequest(1L, 5, "ok")));
    }

    @Test
    void create_duplicateReview_throwsBusinessException() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(makeJob(JobStatus.COMPLETED, 1L)));
        when(reviewRepository.existsByJobId(1L)).thenReturn(true);
        assertThrows(BusinessException.class,
            () -> reviewService.create(1L, new CreateReviewRequest(1L, 5, "ok")));
    }
}

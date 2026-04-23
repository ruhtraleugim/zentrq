package com.spectre.zentrq.proposal;

import com.spectre.zentrq.job.Job;
import com.spectre.zentrq.job.JobRepository;
import com.spectre.zentrq.job.JobStatus;
import com.spectre.zentrq.job.JobType;
import com.spectre.zentrq.payment.PaymentRepository;
import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.profissional.ProfissionalRepository;
import com.spectre.zentrq.proposal.dto.CreateProposalRequest;
import com.spectre.zentrq.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProposalServiceImplTest {

    @Mock ProposalRepository proposalRepository;
    @Mock JobRepository jobRepository;
    @Mock ProfissionalRepository profissionalRepository;
    @Mock PaymentRepository paymentRepository;

    @InjectMocks ProposalServiceImpl proposalService;

    private Job makeJob(JobStatus status, JobType type) {
        Job job = new Job();
        ReflectionTestUtils.setField(job, "id", 1L);
        job.setStatus(status);
        job.setType(type);
        return job;
    }

    private Profissional makeProfissional(boolean available) {
        Profissional p = new Profissional();
        ReflectionTestUtils.setField(p, "id", 2L);
        p.setIsAvailableNow(available);
        return p;
    }

    @Test
    void submit_whenJobClosed_throwsBusinessException() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(makeJob(JobStatus.ACCEPTED, JobType.NORMAL)));
        when(profissionalRepository.findById(2L)).thenReturn(Optional.of(makeProfissional(true)));
        assertThrows(BusinessException.class,
            () -> proposalService.submit(2L, new CreateProposalRequest(1L, BigDecimal.TEN, null)));
    }

    @Test
    void submit_urgentJobWithUnavailableProfessional_throwsBusinessException() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(makeJob(JobStatus.OPEN, JobType.URGENT)));
        when(profissionalRepository.findById(2L)).thenReturn(Optional.of(makeProfissional(false)));
        assertThrows(BusinessException.class,
            () -> proposalService.submit(2L, new CreateProposalRequest(1L, BigDecimal.TEN, null)));
    }

    @Test
    void submit_duplicateProposal_throwsBusinessException() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(makeJob(JobStatus.OPEN, JobType.NORMAL)));
        when(profissionalRepository.findById(2L)).thenReturn(Optional.of(makeProfissional(true)));
        when(proposalRepository.existsByJobIdAndProfessionalId(1L, 2L)).thenReturn(true);
        assertThrows(BusinessException.class,
            () -> proposalService.submit(2L, new CreateProposalRequest(1L, BigDecimal.TEN, null)));
    }

    @Test
    void submit_whenLimitReached_throwsBusinessException() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(makeJob(JobStatus.OPEN, JobType.NORMAL)));
        when(profissionalRepository.findById(2L)).thenReturn(Optional.of(makeProfissional(true)));
        when(proposalRepository.existsByJobIdAndProfessionalId(any(), any())).thenReturn(false);
        when(proposalRepository.countByJobId(1L)).thenReturn(10L);
        assertThrows(BusinessException.class,
            () -> proposalService.submit(2L, new CreateProposalRequest(1L, BigDecimal.TEN, null)));
    }
}

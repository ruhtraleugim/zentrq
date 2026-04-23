package com.spectre.zentrq.proposal;

import com.spectre.zentrq.job.*;
import com.spectre.zentrq.payment.*;
import com.spectre.zentrq.payment.abacatepay.AbacateBillingResponse;
import com.spectre.zentrq.payment.abacatepay.AbacatePayClient;
import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.profissional.ProfissionalRepository;
import com.spectre.zentrq.proposal.dto.*;
import com.spectre.zentrq.shared.exception.BusinessException;
import com.spectre.zentrq.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

    private final ProposalRepository proposalRepository;
    private final JobRepository jobRepository;
    private final ProfissionalRepository profissionalRepository;
    private final PaymentRepository paymentRepository;
    private final AbacatePayClient abacatePayClient;

    @Value("${app.platform-fee:0.10}")
    private BigDecimal platformFeeRate;

    private static final Set<JobStatus> PROPOSAL_ALLOWED = Set.of(
        JobStatus.OPEN, JobStatus.IN_NEGOTIATION
    );

    @Override
    @Transactional
    public ProposalResponse submit(Long professionalId, CreateProposalRequest request) {
        Job job = jobRepository.findById(request.jobId())
            .orElseThrow(() -> new ResourceNotFoundException("Job não encontrado"));
        Profissional prof = profissionalRepository.findById(professionalId)
            .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        if (!PROPOSAL_ALLOWED.contains(job.getStatus())) {
            throw new BusinessException("Job não está aceitando proposals");
        }
        if (job.getType() == JobType.URGENT && !prof.getIsAvailableNow()) {
            throw new BusinessException("Apenas profissionais disponíveis podem propor em jobs URGENT");
        }
        if (proposalRepository.existsByJobIdAndProfessionalId(job.getId(), professionalId)) {
            throw new BusinessException("Você já enviou uma proposal para este job");
        }
        if (proposalRepository.countByJobId(job.getId()) >= 10) {
            throw new BusinessException("Limite de proposals atingido para este job");
        }

        if (job.getStatus() == JobStatus.OPEN) {
            job.setStatus(JobStatus.IN_NEGOTIATION);
            jobRepository.save(job);
        }

        Proposal proposal = new Proposal();
        proposal.setJob(job);
        proposal.setProfessional(prof);
        proposal.setPrice(request.price());
        proposal.setEstimatedTime(request.estimatedTime());
        return toResponse(proposalRepository.save(proposal));
    }

    @Override
    public List<ProposalResponse> listByJob(Long jobId, Long clientId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job não encontrado"));
        if (!job.getClient().getId().equals(clientId)) {
            throw new BusinessException("Não autorizado");
        }
        return proposalRepository.findByJobId(jobId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProposalResponse> listByProfessional(Long professionalId) {
        return proposalRepository.findByProfessionalIdOrderByCreatedAtDesc(professionalId)
            .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public AcceptProposalResponse accept(Long proposalId, Long clientId) {
        Proposal proposal = proposalRepository.findById(proposalId)
            .orElseThrow(() -> new ResourceNotFoundException("Proposal não encontrada"));

        Job job = jobRepository.findByIdWithLock(proposal.getJob().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Job não encontrado"));

        if (!job.getClient().getId().equals(clientId)) {
            throw new BusinessException("Não autorizado");
        }
        if (!PROPOSAL_ALLOWED.contains(job.getStatus())) {
            throw new BusinessException("Job não pode aceitar proposals no estado: " + job.getStatus());
        }
        if (paymentRepository.existsByJobId(job.getId())) {
            throw new BusinessException("Pagamento já existe para este job");
        }

        proposal.setStatus(ProposalStatus.ACCEPTED);
        proposalRepository.save(proposal);
        proposalRepository.rejectOthers(job.getId(), proposalId);

        job.setStatus(JobStatus.ACCEPTED);
        jobRepository.save(job);

        BigDecimal fee = proposal.getPrice().multiply(platformFeeRate);
        Payment payment = new Payment();
        payment.setJobId(job.getId());
        payment.setProposalId(proposalId);
        payment.setAmount(proposal.getPrice());
        payment.setPlatformFee(fee);
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        AbacateBillingResponse billing = abacatePayClient.createBilling(
            job.getId(),
            job.getTitle(),
            proposal.getPrice(),
            job.getClient().getName(),
            job.getClient().getEmail(),
            job.getClient().getPhone(),
            job.getClient().getCpf()
        );
        payment.setExternalId(billing.id());
        paymentRepository.save(payment);

        return toAcceptResponse(proposal, billing.url());
    }

    @Override
    @Transactional
    public ProposalResponse reject(Long proposalId, Long clientId) {
        Proposal proposal = proposalRepository.findById(proposalId)
            .orElseThrow(() -> new ResourceNotFoundException("Proposal não encontrada"));
        Job job = jobRepository.findById(proposal.getJob().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Job não encontrado"));
        if (!job.getClient().getId().equals(clientId)) {
            throw new BusinessException("Não autorizado");
        }
        proposal.setStatus(ProposalStatus.REJECTED);
        return toResponse(proposalRepository.save(proposal));
    }

    public ProposalResponse toResponse(Proposal p) {
        return new ProposalResponse(
            p.getId(), p.getJob().getId(), p.getProfessional().getId(),
            p.getProfessional().getName(), p.getPrice(),
            p.getEstimatedTime(), p.getStatus(), p.getCreatedAt()
        );
    }

    private AcceptProposalResponse toAcceptResponse(Proposal p, String checkoutUrl) {
        return new AcceptProposalResponse(
            p.getId(), p.getJob().getId(), p.getProfessional().getId(),
            p.getProfessional().getName(), p.getPrice(),
            p.getEstimatedTime(), p.getStatus(), p.getCreatedAt(),
            checkoutUrl
        );
    }
}

package com.spectre.zentrq.review;

import com.spectre.zentrq.cliente.ClienteRepository;
import com.spectre.zentrq.job.Job;
import com.spectre.zentrq.job.JobRepository;
import com.spectre.zentrq.job.JobStatus;
import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.profissional.ProfissionalRepository;
import com.spectre.zentrq.proposal.Proposal;
import com.spectre.zentrq.proposal.ProposalRepository;
import com.spectre.zentrq.proposal.ProposalStatus;
import com.spectre.zentrq.review.dto.CreateReviewRequest;
import com.spectre.zentrq.shared.exception.BusinessException;
import com.spectre.zentrq.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final JobRepository jobRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ProposalRepository proposalRepository;

    @Override
    @Transactional
    public Review create(Long clientId, CreateReviewRequest request) {
        Job job = jobRepository.findById(request.jobId())
            .orElseThrow(() -> new ResourceNotFoundException("Job não encontrado"));

        if (!job.getClient().getId().equals(clientId)) {
            throw new BusinessException("Não autorizado");
        }
        if (job.getStatus() != JobStatus.COMPLETED) {
            throw new BusinessException("Job não está concluído");
        }
        if (reviewRepository.existsByJobId(job.getId())) {
            throw new BusinessException("Este job já foi avaliado");
        }

        Profissional professional = proposalRepository.findByJobId(job.getId()).stream()
            .filter(p -> p.getStatus() == ProposalStatus.ACCEPTED)
            .findFirst()
            .map(Proposal::getProfessional)
            .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        var cliente = clienteRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Review review = new Review();
        review.setJob(job);
        review.setClient(cliente);
        review.setProfessional(professional);
        review.setRating(request.rating().shortValue());
        review.setComment(request.comment());
        reviewRepository.save(review);

        Double avg = reviewRepository.averageRatingByProfessional(professional.getId());
        if (avg != null) {
            professional.setRating(Math.round(avg * 10.0) / 10.0);
            professional.setJobsCompleted(professional.getJobsCompleted() + 1);
            profissionalRepository.save(professional);
        }

        return review;
    }
}

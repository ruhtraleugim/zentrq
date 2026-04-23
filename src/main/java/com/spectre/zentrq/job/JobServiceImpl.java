package com.spectre.zentrq.job;

import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.cliente.ClienteRepository;
import com.spectre.zentrq.job.dto.*;
import com.spectre.zentrq.location.City;
import com.spectre.zentrq.location.CityRepository;
import com.spectre.zentrq.location.dto.CityResponse;
import com.spectre.zentrq.payment.PaymentRepository;
import com.spectre.zentrq.payment.PaymentStatus;
import com.spectre.zentrq.shared.exception.BusinessException;
import com.spectre.zentrq.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ClienteRepository clienteRepository;
    private final CityRepository cityRepository;
    private final PaymentRepository paymentRepository;

    private static final Set<JobStatus> CANCELABLE = Set.of(
        JobStatus.OPEN, JobStatus.IN_NEGOTIATION, JobStatus.ACCEPTED
    );

    @Override
    @Transactional
    public JobResponse create(Long clientId, CreateJobRequest request) {
        Cliente client = clienteRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        City city = cityRepository.findById(request.cityId())
            .orElseThrow(() -> new BusinessException("Cidade inválida"));
        Job job = new Job();
        job.setClient(client);
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setType(request.type());
        job.setCity(city);
        job.setStatus(JobStatus.OPEN);
        return toResponse(jobRepository.save(job));
    }

    @Override
    public List<JobResponse> listForCliente(Long clientId) {
        return jobRepository.findByClientIdOrderByCreatedAtDesc(clientId)
            .stream().map(this::toResponse).toList();
    }

    @Override
    public List<JobResponse> listForProfissional(List<City> cities) {
        List<Long> cityIds = cities.stream().map(City::getId).toList();
        if (cityIds.isEmpty()) return List.of();
        return jobRepository.findEligibleForProfessional(
            cityIds, List.of(JobStatus.OPEN, JobStatus.IN_NEGOTIATION)
        ).stream().map(this::toResponse).toList();
    }

    @Override
    public JobResponse findById(Long id) {
        return toResponse(jobRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Job não encontrado")));
    }

    @Override
    @Transactional
    public JobResponse cancel(Long jobId, Long clientId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job não encontrado"));
        if (!job.getClient().getId().equals(clientId)) {
            throw new BusinessException("Não autorizado");
        }
        if (!CANCELABLE.contains(job.getStatus())) {
            throw new BusinessException("Job não pode ser cancelado no estado atual: " + job.getStatus());
        }
        job.setStatus(JobStatus.CANCELED);
        return toResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public JobResponse complete(Long jobId, Long clientId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job não encontrado"));
        if (!job.getClient().getId().equals(clientId)) {
            throw new BusinessException("Não autorizado");
        }
        if (job.getStatus() != JobStatus.IN_PROGRESS) {
            throw new BusinessException("Job não está em progresso");
        }
        boolean paid = paymentRepository.existsByJobIdAndStatus(jobId, PaymentStatus.PAID);
        if (!paid) {
            throw new BusinessException("Pagamento não confirmado");
        }
        job.setStatus(JobStatus.COMPLETED);
        return toResponse(jobRepository.save(job));
    }

    public JobResponse toResponse(Job j) {
        CityResponse cityResponse = new CityResponse(j.getCity().getId(), j.getCity().getName());
        return new JobResponse(
            j.getId(), j.getClient().getId(),
            j.getTitle(), j.getDescription(),
            j.getType(), j.getStatus(), cityResponse, j.getCreatedAt()
        );
    }
}

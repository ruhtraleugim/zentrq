package com.spectre.zentrq.job;

import com.spectre.zentrq.job.dto.*;
import com.spectre.zentrq.location.City;

import java.util.List;

public interface JobService {
    JobResponse create(Long clientId, CreateJobRequest request);
    List<JobResponse> listForCliente(Long clientId);
    List<JobResponse> listForProfissional(List<City> cities);
    JobResponse findById(Long id);
    JobResponse cancel(Long jobId, Long clientId);
    JobResponse complete(Long jobId, Long clientId);
}

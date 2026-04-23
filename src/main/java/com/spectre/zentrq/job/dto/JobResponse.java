package com.spectre.zentrq.job.dto;

import com.spectre.zentrq.job.JobStatus;
import com.spectre.zentrq.job.JobType;
import com.spectre.zentrq.location.dto.CityResponse;

import java.time.LocalDateTime;

public record JobResponse(
    Long id,
    Long clientId,
    String title,
    String description,
    JobType type,
    JobStatus status,
    CityResponse city,
    LocalDateTime createdAt
) {}

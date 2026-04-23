package com.spectre.zentrq.job.dto;
import com.spectre.zentrq.job.JobType;
import jakarta.validation.constraints.*;
public record CreateJobRequest(
    @NotBlank @Size(max = 255) String title,
    @NotBlank String description,
    @NotNull JobType type,
    @NotNull Long cityId
) {}
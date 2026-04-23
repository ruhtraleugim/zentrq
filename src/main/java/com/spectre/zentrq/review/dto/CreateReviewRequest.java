package com.spectre.zentrq.review.dto;
import jakarta.validation.constraints.*;
public record CreateReviewRequest(
    @NotNull Long jobId,
    @NotNull @Min(1) @Max(5) Integer rating,
    @Size(max = 2000) String comment
) {}

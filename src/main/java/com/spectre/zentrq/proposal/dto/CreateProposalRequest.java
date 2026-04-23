package com.spectre.zentrq.proposal.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CreateProposalRequest(
    @NotNull Long jobId,
    @NotNull @DecimalMin("0.01") BigDecimal price,
    @Size(max = 100) String estimatedTime
) {}

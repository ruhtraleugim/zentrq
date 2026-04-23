package com.spectre.zentrq.proposal.dto;
import com.spectre.zentrq.proposal.ProposalStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record ProposalResponse(
    Long id, Long jobId, Long professionalId, String professionalName,
    BigDecimal price, String estimatedTime, ProposalStatus status, LocalDateTime createdAt
) {}

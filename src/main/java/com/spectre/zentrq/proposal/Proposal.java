package com.spectre.zentrq.proposal;

import com.spectre.zentrq.job.Job;
import com.spectre.zentrq.profissional.Profissional;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposals",
    indexes = { @Index(name = "idx_proposals_job_id", columnList = "job_id") },
    uniqueConstraints = { @UniqueConstraint(name = "uk_proposals_job_prof",
                          columnNames = {"job_id", "professional_id"}) }
)
@Getter @Setter @NoArgsConstructor
public class Proposal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Profissional professional;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "estimated_time", length = 100)
    private String estimatedTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProposalStatus status = ProposalStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

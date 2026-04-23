package com.spectre.zentrq.review;

import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.job.Job;
import com.spectre.zentrq.profissional.Profissional;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter @Setter @NoArgsConstructor
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false, unique = true)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Cliente client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Profissional professional;

    @Column(nullable = false, columnDefinition = "SMALLINT")
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;
}

package com.spectre.zentrq.job;

import com.spectre.zentrq.cliente.Cliente;
import com.spectre.zentrq.location.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs", indexes = {
    @Index(name = "idx_jobs_city_status", columnList = "city_id, status")
})
@Getter @Setter @NoArgsConstructor
public class Job {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //futuramente mudar para UUID

    // para futuros olhos , o lazy aqui é para evitar ciclos redundantes de chamada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Cliente client;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobStatus status = JobStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // eu pessoalmente não gosto da @Column , mas é necessario por enquanto
}
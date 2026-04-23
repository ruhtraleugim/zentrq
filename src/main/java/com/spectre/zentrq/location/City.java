package com.spectre.zentrq.location;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cities",
    uniqueConstraints = @UniqueConstraint(name = "uk_cities_slug_state", columnNames = {"slug", "state_id"}))
@Getter @Setter @NoArgsConstructor
public class City {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @Column(nullable = false)
    private Boolean active = true;
}

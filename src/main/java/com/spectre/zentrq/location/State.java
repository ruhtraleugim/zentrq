package com.spectre.zentrq.location;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "states")
@Getter @Setter @NoArgsConstructor
public class State {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 2, unique = true)
    private String uf;

    @Column(nullable = false, length = 100, unique = true)
    private String slug;

    @Column(nullable = false)
    private Boolean active = true;
}

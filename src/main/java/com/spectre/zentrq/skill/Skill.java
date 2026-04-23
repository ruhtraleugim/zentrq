package com.spectre.zentrq.skill;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skills")
@Getter @Setter @NoArgsConstructor
public class Skill {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    private String slug;

    @Column(nullable = false)
    private Boolean active = true;
}

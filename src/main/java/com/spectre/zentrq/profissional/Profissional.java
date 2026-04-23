package com.spectre.zentrq.profissional;

import com.spectre.zentrq.location.City;
import com.spectre.zentrq.skill.Skill;
import com.spectre.zentrq.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profissionais", indexes = {
    @Index(name = "idx_profissionais_available", columnList = "is_available_now")
})
@DiscriminatorValue("PROFISSIONAL")
@Getter @Setter @NoArgsConstructor
public class Profissional extends User {

    @Column(length = 9)
    private String cep;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(name = "jobs_completed", nullable = false)
    private Integer jobsCompleted = 0;

    @Column(name = "is_available_now", nullable = false)
    private Boolean isAvailableNow = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "profissional_cities",
        joinColumns = @JoinColumn(name = "profissional_id"),
        inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private List<City> citiesServed = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "profissional_skills",
        joinColumns = @JoinColumn(name = "profissional_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();
}

package com.spectre.zentrq.skill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByActiveTrueOrderByName();
    List<Skill> findByIdIn(List<Long> ids);
}

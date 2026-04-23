package com.spectre.zentrq.profissional.dto;

import com.spectre.zentrq.location.dto.CityResponse;
import com.spectre.zentrq.skill.dto.SkillResponse;

import java.util.List;

public record ProfissionalResponse(
    Long id, String name, String email,
    String cep, Double rating, Integer jobsCompleted,
    Boolean isAvailableNow,
    List<CityResponse> citiesServed,
    List<SkillResponse> skills
) {}

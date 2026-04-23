package com.spectre.zentrq.profissional;

import com.spectre.zentrq.location.City;
import com.spectre.zentrq.location.CityRepository;
import com.spectre.zentrq.location.dto.CityResponse;
import com.spectre.zentrq.profissional.dto.ProfissionalResponse;
import com.spectre.zentrq.profissional.dto.ProfileRequest;
import com.spectre.zentrq.shared.exception.BusinessException;
import com.spectre.zentrq.shared.exception.ResourceNotFoundException;
import com.spectre.zentrq.skill.Skill;
import com.spectre.zentrq.skill.SkillRepository;
import com.spectre.zentrq.skill.dto.SkillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalServiceImpl implements ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final CityRepository cityRepository;
    private final SkillRepository skillRepository;

    @Override
    @Transactional
    public ProfissionalResponse updateProfile(Long userId, ProfileRequest request) {
        Profissional prof = profissionalRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        List<City> cities = cityRepository.findByIdIn(request.cityIds());
        if (cities.size() != request.cityIds().size()) {
            throw new BusinessException("Uma ou mais cidades são inválidas");
        }
        prof.getCitiesServed().clear();
        prof.getCitiesServed().addAll(cities);

        List<Skill> skills = (request.skillIds() != null && !request.skillIds().isEmpty())
            ? skillRepository.findByIdIn(request.skillIds())
            : List.of();
        prof.getSkills().clear();
        prof.getSkills().addAll(skills);

        prof.setCep(request.cep());
        return toResponse(profissionalRepository.save(prof));
    }

    @Override
    @Transactional
    public ProfissionalResponse toggleAvailability(Long userId) {
        Profissional prof = profissionalRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));
        prof.setIsAvailableNow(!prof.getIsAvailableNow());
        return toResponse(profissionalRepository.save(prof));
    }

    @Override
    public List<ProfissionalResponse> findAll() {
        return profissionalRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProfissionalResponse toResponse(Profissional p) {
        List<CityResponse> cities = p.getCitiesServed().stream()
            .map(c -> new CityResponse(c.getId(), c.getName())).toList();
        List<SkillResponse> skills = p.getSkills().stream()
            .map(s -> new SkillResponse(s.getId(), s.getName())).toList();
        return new ProfissionalResponse(
            p.getId(), p.getName(), p.getEmail(),
            p.getCep(), p.getRating(), p.getJobsCompleted(),
            p.getIsAvailableNow(), cities, skills
        );
    }
}

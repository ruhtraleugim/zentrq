package com.spectre.zentrq.profissional.matching;

import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.profissional.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CityBasedMatcher implements ProfissionalMatcher {

    private final ProfissionalRepository profissionalRepository;

    @Override
    public List<Profissional> findEligible(Long cityId, boolean urgentOnly) {
        if (urgentOnly) {
            return profissionalRepository.findAvailableByCityId(cityId);
        }
        return profissionalRepository.findByCityId(cityId);
    }
}

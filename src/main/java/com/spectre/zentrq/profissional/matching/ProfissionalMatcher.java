package com.spectre.zentrq.profissional.matching;

import com.spectre.zentrq.profissional.Profissional;

import java.util.List;

public interface ProfissionalMatcher {
    List<Profissional> findEligible(Long cityId, boolean urgentOnly);
}

package com.spectre.zentrq.profissional;

import com.spectre.zentrq.profissional.dto.*;
import java.util.List;

public interface ProfissionalService {
    ProfissionalResponse updateProfile(Long userId, ProfileRequest request);
    ProfissionalResponse toggleAvailability(Long userId);
    List<ProfissionalResponse> findAll();
}

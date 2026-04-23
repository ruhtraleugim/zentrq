package com.spectre.zentrq.location;

import com.spectre.zentrq.location.dto.CityResponse;
import com.spectre.zentrq.location.dto.StateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    @GetMapping("/states")
    public List<StateResponse> states() {
        return stateRepository.findByActiveTrueOrderByName()
            .stream().map(s -> new StateResponse(s.getId(), s.getName(), s.getUf())).toList();
    }

    @GetMapping("/states/{id}/cities")
    public List<CityResponse> cities(@PathVariable Long id) {
        return cityRepository.findByStateIdAndActiveTrueOrderByName(id)
            .stream().map(c -> new CityResponse(c.getId(), c.getName())).toList();
    }
}

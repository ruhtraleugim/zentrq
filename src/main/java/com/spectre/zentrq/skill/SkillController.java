package com.spectre.zentrq.skill;

import com.spectre.zentrq.skill.dto.SkillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SkillController {

    private final SkillRepository skillRepository;

    @GetMapping("/skills")
    public List<SkillResponse> skills() {
        return skillRepository.findByActiveTrueOrderByName()
            .stream().map(s -> new SkillResponse(s.getId(), s.getName())).toList();
    }
}

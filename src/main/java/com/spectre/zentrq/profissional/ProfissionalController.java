package com.spectre.zentrq.profissional;

import com.spectre.zentrq.profissional.dto.*;
import com.spectre.zentrq.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<ProfissionalResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid ProfileRequest request) {
        return ResponseEntity.ok(profissionalService.updateProfile(user.getId(), request));
    }

    @PutMapping("/availability")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<ProfissionalResponse> toggleAvailability(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profissionalService.toggleAvailability(user.getId()));
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponse>> list() {
        return ResponseEntity.ok(profissionalService.findAll());
    }
}

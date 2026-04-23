package com.spectre.zentrq.job;

import com.spectre.zentrq.job.dto.*;
import com.spectre.zentrq.profissional.Profissional;
import com.spectre.zentrq.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // aqui a role é verificada antes de criar o job, pois se não seria problematico,
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<JobResponse> create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateJobRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(jobService.create(user.getId(), request));
    }
    // esse end point é para profficionais verem seus jobs
    @GetMapping("/me")
    public ResponseEntity<List<JobResponse>> listMine(@AuthenticationPrincipal User user) {
        if (user instanceof Profissional prof) {
            return ResponseEntity.ok(jobService.listForProfissional(prof.getCitiesServed()));
        }
        return ResponseEntity.ok(jobService.listForCliente(user.getId()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.findById(id));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<JobResponse> cancel(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(jobService.cancel(id, user.getId()));
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<JobResponse> complete(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(jobService.complete(id, user.getId()));
    }
}

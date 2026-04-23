package com.spectre.zentrq.proposal;

import com.spectre.zentrq.proposal.dto.*;
import com.spectre.zentrq.proposal.dto.AcceptProposalResponse;
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
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping("/proposals")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<ProposalResponse> submit(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateProposalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(proposalService.submit(user.getId(), request));
    }

    @GetMapping("/jobs/{jobId}/proposals")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ProposalResponse>> listByJob(
            @PathVariable Long jobId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(proposalService.listByJob(jobId, user.getId()));
    }

    @GetMapping("/proposals/me")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<List<ProposalResponse>> listMine(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(proposalService.listByProfessional(user.getId()));
    }

    @PostMapping("/proposals/{id}/accept")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<AcceptProposalResponse> accept(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(proposalService.accept(id, user.getId()));
    }

    @PostMapping("/proposals/{id}/reject")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ProposalResponse> reject(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(proposalService.reject(id, user.getId()));
    }
}

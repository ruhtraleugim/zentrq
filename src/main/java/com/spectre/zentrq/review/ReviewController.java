package com.spectre.zentrq.review;

import com.spectre.zentrq.review.dto.CreateReviewRequest;
import com.spectre.zentrq.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateReviewRequest request) {
        reviewService.create(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

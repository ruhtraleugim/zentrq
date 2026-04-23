package com.spectre.zentrq.review;
import com.spectre.zentrq.review.dto.CreateReviewRequest;
public interface ReviewService {
    Review create(Long clientId, CreateReviewRequest request);
}

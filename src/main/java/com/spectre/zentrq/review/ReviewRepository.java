package com.spectre.zentrq.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByJobId(Long jobId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.professional.id = :profId")
    Double averageRatingByProfessional(@Param("profId") Long profId);
}

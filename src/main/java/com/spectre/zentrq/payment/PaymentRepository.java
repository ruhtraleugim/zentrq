package com.spectre.zentrq.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByJobId(Long jobId);
    boolean existsByJobIdAndStatus(Long jobId, PaymentStatus status);
    Optional<Payment> findByExternalId(String externalId);
    Optional<Payment> findByJobId(Long jobId);
}

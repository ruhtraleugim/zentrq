package com.spectre.zentrq.job;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByClientIdOrderByCreatedAtDesc(Long clientId);

    @Query("SELECT j FROM Job j WHERE j.city.id IN :cityIds AND j.status IN :statuses ORDER BY j.type DESC, j.createdAt DESC")
    List<Job> findEligibleForProfessional(
        @Param("cityIds") List<Long> cityIds,
        @Param("statuses") List<JobStatus> statuses
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT j FROM Job j WHERE j.id = :id")
    Optional<Job> findByIdWithLock(@Param("id") Long id);
}

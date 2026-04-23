package com.spectre.zentrq.proposal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findByJobId(Long jobId);

    List<Proposal> findByProfessionalIdOrderByCreatedAtDesc(Long professionalId);

    long countByJobId(Long jobId);

    boolean existsByJobIdAndProfessionalId(Long jobId, Long professionalId);

    @Modifying
    @Query("UPDATE Proposal p SET p.status = 'REJECTED' WHERE p.job.id = :jobId AND p.id <> :acceptedId AND p.status = 'PENDING'")
    int rejectOthers(@Param("jobId") Long jobId, @Param("acceptedId") Long acceptedId);
}

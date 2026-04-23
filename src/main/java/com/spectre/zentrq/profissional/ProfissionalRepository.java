package com.spectre.zentrq.profissional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    @Query("SELECT DISTINCT p FROM Profissional p JOIN p.citiesServed c WHERE c.id = :cityId")
    List<Profissional> findByCityId(@Param("cityId") Long cityId);

    @Query("SELECT DISTINCT p FROM Profissional p JOIN p.citiesServed c WHERE c.id = :cityId AND p.isAvailableNow = true")
    List<Profissional> findAvailableByCityId(@Param("cityId") Long cityId);
}

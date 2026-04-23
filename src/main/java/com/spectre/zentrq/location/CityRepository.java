package com.spectre.zentrq.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByStateIdAndActiveTrueOrderByName(Long stateId);
    List<City> findByIdIn(List<Long> ids);
}

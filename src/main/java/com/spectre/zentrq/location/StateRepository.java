package com.spectre.zentrq.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
    List<State> findByActiveTrueOrderByName();
}

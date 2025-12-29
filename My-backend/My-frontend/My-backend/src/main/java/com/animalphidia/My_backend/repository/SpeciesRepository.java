package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Integer> {
}
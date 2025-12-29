package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.Phylum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhylumRepository extends JpaRepository<Phylum, Integer> {
}
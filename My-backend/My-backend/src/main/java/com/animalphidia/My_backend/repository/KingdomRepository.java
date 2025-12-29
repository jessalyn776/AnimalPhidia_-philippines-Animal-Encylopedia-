package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.Kingdom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KingdomRepository extends JpaRepository<Kingdom, Integer> {
}
package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.AnimalSound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalSoundRepository extends JpaRepository<AnimalSound, Integer> {

    List<AnimalSound> findByAnimalId(Integer animalId);

    List<AnimalSound> findByAnimalIdAndSoundType(Integer animalId, String soundType);

    Page<AnimalSound> findByAnimalId(Integer animalId, Pageable pageable);
}
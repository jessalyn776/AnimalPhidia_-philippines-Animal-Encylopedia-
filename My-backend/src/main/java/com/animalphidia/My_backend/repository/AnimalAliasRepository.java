package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.AnimalAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalAliasRepository extends JpaRepository<AnimalAlias, Long> {

    // ✅ FIXED: Direct column search (matches database)
    List<AnimalAlias> findByAnimalId(Integer animalId);

    Optional<AnimalAlias> findByAliasNameIgnoreCase(String aliasName);

    List<AnimalAlias> findByLanguageIgnoreCase(String language);
}
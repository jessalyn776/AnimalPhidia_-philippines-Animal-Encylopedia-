package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.ClassTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassTaxRepository extends JpaRepository<ClassTax, Integer> {
}
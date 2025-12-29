package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    Optional<Animal> findByScientificNameIgnoreCase(String scientificName);
    Optional<Animal> findByCommonNameIgnoreCase(String commonName);
    Page<Animal> findByActiveTrue(Pageable pageable);
    Page<Animal> findByIsVerifiedTrue(Pageable pageable);
    List<Animal> findByRegionIgnoreCase(String region);
    List<Animal> findByIslandIgnoreCase(String island);
    List<Animal> findByProvinceIgnoreCase(String province);
    List<Animal> findByIsEndangeredTrue();
    List<Animal> findByIsProtectedTrue();

    @Query("SELECT a FROM Animal a WHERE a.commonName LIKE %:keyword% OR a.scientificName LIKE %:keyword% OR a.description LIKE %:keyword%")
    Page<Animal> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT a FROM Animal a WHERE a.active = true AND a.isVerified = true ORDER BY a.createdAt DESC")
    Page<Animal> findFeaturedAnimals(Pageable pageable);

    @Query("SELECT a FROM Animal a WHERE a.active = true AND a.isVerified = true")
    List<Animal> findAllVerifiedAndActive();

    @Query("SELECT a FROM Animal a WHERE LOWER(a.conservationStatusString) LIKE LOWER(CONCAT('%', :status, '%')) AND a.active = true")
    List<Animal> findByConservationStatusAndActive(@Param("status") String status);

    Page<Animal> findByActiveTrueAndIsVerifiedTrue(Pageable pageable);

    Page<Animal> findByIsVerifiedFalse(Pageable pageable);
    List<Animal> findByIsVerifiedFalse();
    long countByIsVerified(Boolean isVerified);
    long countByActive(Boolean active);

    // This query returns animals that are active AND (either verified OR created by the user)
    @Query("SELECT a FROM Animal a WHERE a.active = true AND (a.isVerified = true OR a.createdBy = :userId)")
    Page<Animal> findByActiveTrueAndVerifiedOrUser(@Param("userId") Integer userId, Pageable pageable);
}
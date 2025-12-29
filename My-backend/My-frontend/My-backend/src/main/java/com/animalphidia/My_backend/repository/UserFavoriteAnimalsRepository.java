package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.UserFavoriteAnimals;
import com.animalphidia.My_backend.model.UserFavoriteAnimalsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteAnimalsRepository extends JpaRepository<UserFavoriteAnimals, UserFavoriteAnimalsId> {
    List<UserFavoriteAnimals> findByUserId(Integer userId);
    List<UserFavoriteAnimals> findByAnimalId(Integer animalId);
    boolean existsByUserIdAndAnimalId(Integer userId, Integer animalId);
    void deleteByUserIdAndAnimalId(Integer userId, Integer animalId);
}
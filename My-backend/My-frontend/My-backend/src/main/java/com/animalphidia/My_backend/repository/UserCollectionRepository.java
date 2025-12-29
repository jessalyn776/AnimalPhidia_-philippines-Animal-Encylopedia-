package com.animalphidia.My_backend.repository;

import com.animalphidia.My_backend.model.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCollectionRepository extends JpaRepository<UserCollection, Long> {

    List<UserCollection> findByUserId(Long userId);

    List<UserCollection> findByUserIdAndIsPublicTrue(Long userId);

    List<UserCollection> findByCollectionNameIgnoreCase(String collectionName);
}
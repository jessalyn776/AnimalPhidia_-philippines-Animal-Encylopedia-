package com.animalphidia.My_backend.service;

import com.animalphidia.My_backend.dto.UserCollectionDTO;
import com.animalphidia.My_backend.model.UserCollection;
import com.animalphidia.My_backend.model.User;
import com.animalphidia.My_backend.repository.UserCollectionRepository;
import com.animalphidia.My_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCollectionService {

    @Autowired
    private UserCollectionRepository userCollectionRepository;

    @Autowired
    private UserRepository userRepository;

    // For now, resolve a default user if security is not present
    private User resolveDefaultUser() {
        // try to find a user with username 'admin' (case-insensitive), otherwise return first user if exists
        return userRepository.findByUsernameIgnoreCase("admin").orElseGet(() -> userRepository.findAll().stream().findFirst().orElse(null));
    }

    public List<UserCollectionDTO> getCollectionsForCurrentUser() {
        User user = resolveDefaultUser();
        if (user == null) return List.of();
        // pass Long user id to repository
        Long uid = user.getId() == null ? null : user.getId().longValue();
        if (uid == null) return List.of();
        List<UserCollection> list = userCollectionRepository.findByUserId(uid);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserCollectionDTO createCollection(UserCollectionDTO dto) {
        User user = resolveDefaultUser();
        if (user == null) return null;
        UserCollection uc = new UserCollection();
        uc.user = user;
        uc.collectionName = dto.getCollectionName();
        uc.description = dto.getDescription();
        uc.isPublic = dto.getIsPublic() != null && dto.getIsPublic();
        UserCollection saved = userCollectionRepository.save(uc);
        return toDto(saved);
    }

    public void deleteCollection(Long id) {
        userCollectionRepository.deleteById(id);
    }

    private UserCollectionDTO toDto(UserCollection uc) {
        UserCollectionDTO d = new UserCollectionDTO();
        d.setId(uc.id);
        d.setCollectionName(uc.collectionName);
        d.setDescription(uc.description);
        d.setIsPublic(uc.isPublic);
        d.setCreatedAt(uc.createdAt);
        d.setCount(0); // count calculation not implemented (requires join table)
        return d;
    }
}
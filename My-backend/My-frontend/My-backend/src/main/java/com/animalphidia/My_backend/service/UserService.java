package com.animalphidia.My_backend.service;

import com.animalphidia.My_backend.model.User;
import com.animalphidia.My_backend.model.UserRole;
import com.animalphidia.My_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        String username = auth.getName();
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);

        return userOpt.map(User::getId).orElse(null);
    }

    public Integer getUserIdByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
        return userOpt.map(User::getId).orElse(null);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        String username = auth.getName();
        return userRepository.findByUsernameIgnoreCase(username).orElse(null);
    }

    public boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && user.getRole() == UserRole.ADMIN;
    }

    public boolean isModerator() {
        User user = getCurrentUser();
        return user != null && (user.getRole() == UserRole.MODERATOR || user.getRole() == UserRole.ADMIN);
    }

    public boolean isContributor() {
        User user = getCurrentUser();
        return user != null && (user.getRole() == UserRole.CONTRIBUTOR ||
                user.getRole() == UserRole.MODERATOR ||
                user.getRole() == UserRole.ADMIN);
    }

    public boolean isViewer() {
        User user = getCurrentUser();
        return user != null && user.getRole() == UserRole.VIEWER;
    }
}
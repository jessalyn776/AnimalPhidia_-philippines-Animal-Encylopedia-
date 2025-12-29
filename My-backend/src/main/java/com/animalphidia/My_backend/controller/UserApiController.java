package com.animalphidia.My_backend.controller;

import com.animalphidia.My_backend.model.Animal;
import com.animalphidia.My_backend.model.User;
import com.animalphidia.My_backend.repository.AnimalRepository;
import com.animalphidia.My_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimalRepository animalRepository;

    // Resolve a default user for dev: find username 'admin' or first user
    private Optional<User> resolveDefaultUserOptional() {
        return userRepository.findByUsernameIgnoreCase("admin")
                .or(() -> userRepository.findAll().stream().findFirst());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        Optional<User> u = resolveDefaultUserOptional();
        if (u.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "No user found"));

        User user = u.get();
        Map<String, Object> out = new HashMap<>();
        out.put("userId", user.getId());
        out.put("username", user.getUsername());
        out.put("firstName", user.getFirstName());
        out.put("lastName", user.getLastName());
        out.put("bio", user.getBio());
        out.put("createdAt", user.getCreatedAt());
        out.put("profilePicture", user.getProfilePicture());
        out.put("email", user.getEmail());
        out.put("role", user.getRole().toString());
        out.put("emailVerified", user.getEmailVerified());

        return ResponseEntity.ok(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(u -> ResponseEntity.ok(Map.of(
                        "userId", u.getId(),
                        "username", u.getUsername(),
                        "firstName", u.getFirstName(),
                        "lastName", u.getLastName(),
                        "email", u.getEmail(),
                        "profilePicture", u.getProfilePicture()
                )))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> notifications() {
        // For now return empty list (implementing Notification model left as future work)
        return ResponseEntity.ok(Map.of("notifications", List.of()));
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> favorites() {
        Optional<User> opt = resolveDefaultUserOptional();
        if (opt.isEmpty()) return ResponseEntity.ok(List.of());

        User u = opt.get();
        Set<Animal> favs = u.getFavoriteAnimals();
        List<Map<String, Object>> list = new ArrayList<>();

        if (favs != null) {
            favs.forEach(a -> list.add(Map.of(
                    "id", a.getAnimalId(),
                    "commonName", a.getCommonName(),
                    "imageUrl", a.getImageUrl(),
                    "scientificName", a.getScientificName()
            )));
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/favorites/{animalId}/check")
    public ResponseEntity<?> checkFavorite(@PathVariable Integer animalId) {
        Optional<User> opt = resolveDefaultUserOptional();
        boolean fav = false;

        if (opt.isPresent()) {
            User u = opt.get();
            Set<Animal> favoriteAnimals = u.getFavoriteAnimals();
            if (favoriteAnimals != null) {
                fav = favoriteAnimals.stream()
                        .anyMatch(a -> Objects.equals(a.getAnimalId(), animalId));
            }
        }
        return ResponseEntity.ok(Map.of("isFavorited", fav));
    }

    @PostMapping("/favorites/{animalId}")
    public ResponseEntity<?> addFavorite(@PathVariable Integer animalId) {
        Optional<User> opt = resolveDefaultUserOptional();
        if (opt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "No user"));

        User u = opt.get();
        Optional<Animal> aopt = animalRepository.findById(animalId);
        if (aopt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Animal not found"));

        Animal a = aopt.get();
        Set<Animal> favoriteAnimals = u.getFavoriteAnimals();
        if (favoriteAnimals == null) {
            favoriteAnimals = new HashSet<>();
            u.setFavoriteAnimals(favoriteAnimals);
        }

        favoriteAnimals.add(a);
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("message", "Added to favorites"));
    }

    @DeleteMapping("/favorites/{animalId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Integer animalId) {
        Optional<User> opt = resolveDefaultUserOptional();
        if (opt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "No user"));

        User u = opt.get();
        Set<Animal> favoriteAnimals = u.getFavoriteAnimals();

        if (favoriteAnimals != null) {
            favoriteAnimals.removeIf(a -> Objects.equals(a.getAnimalId(), animalId));
            userRepository.save(u);
        }

        return ResponseEntity.noContent().build();
    }
}
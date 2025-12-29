package com.animalphidia.My_backend.controller;

import com.animalphidia.My_backend.dto.UserCollectionDTO;
import com.animalphidia.My_backend.service.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/collections")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCollectionController {

    @Autowired
    private UserCollectionService userCollectionService;

    @GetMapping
    public ResponseEntity<List<UserCollectionDTO>> list() {
        List<UserCollectionDTO> list = userCollectionService.getCollectionsForCurrentUser();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<UserCollectionDTO> create(@RequestBody UserCollectionDTO dto) {
        UserCollectionDTO created = userCollectionService.createCollection(dto);
        if (created == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userCollectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }
}
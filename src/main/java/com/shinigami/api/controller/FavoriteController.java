package com.shinigami.api.controller;

import com.shinigami.api.dto.FavoriteChapterDto;
import com.shinigami.api.model.favorite.FavoriteChapterModel;
import com.shinigami.api.service.FavoriteChapterService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@AllArgsConstructor
public class FavoriteController { // for favorite chapter

    private FavoriteChapterService favoriteChapterService;

    @PostMapping
    public ResponseEntity<Void> saveFavorite(@RequestBody FavoriteChapterDto favoriteChapterDto) {
        favoriteChapterService.save(favoriteChapterDto);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteChapterModel>> all(){
        return ResponseEntity.ok(favoriteChapterService.all());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteChapterModel>> byId(@PathVariable String userId){
        return ResponseEntity.ok(favoriteChapterService.byId(userId));
    }

}

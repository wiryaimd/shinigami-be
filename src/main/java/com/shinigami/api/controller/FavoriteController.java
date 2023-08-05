/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller;

import com.shinigami.api.dto.FavoriteChapterDto;
import com.shinigami.api.model.FavoriteChapterModel;
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable("userId") String userId, @RequestParam("comic") String comicUrl){
        favoriteChapterService.remove(userId, comicUrl);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/all/{userId}")
    public ResponseEntity<Void> deleteFavoriteAll(@PathVariable("userId") String userId, @RequestParam("auth") String auth){
        if (!auth.equals("its so sweet knowing that you love mee")){
            return ResponseEntity.status(404).body(null);
        }

        favoriteChapterService.removeAll(userId);
        return ResponseEntity.ok(null);
    }

}

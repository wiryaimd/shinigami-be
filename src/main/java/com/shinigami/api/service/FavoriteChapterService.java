/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service;

import com.shinigami.api.dto.FavoriteChapterDto;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.model.FavoriteChapterModel;
import com.shinigami.api.repositories.FavoriteChapterRepository;
import com.shinigami.api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FavoriteChapterService {

    private UserRepository userRepository;
    private FavoriteChapterRepository favoriteChapterRepository;

    public void save(FavoriteChapterDto favoriteChapterDto){
        UserModel userModel = userRepository.findByUserId(favoriteChapterDto.getUserId()).orElse(null);
        if (userModel == null){
            return;
        }

        log.info("user: {}", userModel.getUserId());
        FavoriteChapterModel favoriteChapterModel = favoriteChapterDto.getFavoriteData();

        favoriteChapterRepository.save(new FavoriteChapterModel(
                favoriteChapterModel.getTitle(),
                favoriteChapterModel.getUrl(),
                favoriteChapterModel.getCover(),
                favoriteChapterModel.getChapter(),
                userModel
        ));
    }

    public List<FavoriteChapterModel> all() {
        return favoriteChapterRepository.findAll();
    }

    public List<FavoriteChapterModel> byId(String userId) {
        UserModel userModel = userRepository.findByUserId(userId).orElse(null);
        if (userModel == null){
            return List.of();
        }
        return userModel.getFavoriteChapterList();
    }

    public void remove(String userId, String comicUrl) {
        UserModel userModel = userRepository.findByUserId(userId).orElse(null);
        if (userModel == null){
            return;
        }

        for (int i = 0; i < userModel.getFavoriteChapterList().size(); i++) {
            FavoriteChapterModel favoriteChapterModel = userModel.getFavoriteChapterList().get(i);
            log.info("url: " + favoriteChapterModel.getTitle());
            if (favoriteChapterModel.getUrl().equalsIgnoreCase(comicUrl)){
                favoriteChapterRepository.delete(favoriteChapterModel);
            }
        }
    }

    @Transactional
    public void removeAll(int userId) {
        favoriteChapterRepository.deleteByUserModel_Id(userId);
    }
}

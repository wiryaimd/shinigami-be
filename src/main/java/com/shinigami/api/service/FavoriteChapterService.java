package com.shinigami.api.service;

import com.shinigami.api.dto.FavoriteChapterDto;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.model.favorite.FavoriteChapterModel;
import com.shinigami.api.model.favorite.FavoriteComicModel;
import com.shinigami.api.repositories.FavoriteChapterRepository;
import com.shinigami.api.repositories.FavoriteComicRepository;
import com.shinigami.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
        UserModel userModel = userRepository.findByUserId(userId).orElseGet(null);
        if (userModel == null){
            return List.of();
        }
        return userModel.getFavoriteChapterList();
    }
}

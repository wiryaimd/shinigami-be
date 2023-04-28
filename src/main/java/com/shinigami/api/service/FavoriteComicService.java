package com.shinigami.api.service;

import com.shinigami.api.dto.FavoriteComicDto;
import com.shinigami.api.repositories.FavoriteComicRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FavoriteComicService {

    private FavoriteComicRepository favoriteComicRepository;

    public void saveFavoriteComic(FavoriteComicDto favoriteComicDto){

//        favoriteComicRepository.save(new FavoriteComicModel());
    }

}

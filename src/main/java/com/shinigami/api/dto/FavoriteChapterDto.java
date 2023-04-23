package com.shinigami.api.dto;

import com.shinigami.api.model.favorite.FavoriteChapterModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteChapterDto {

    private String userId;
    private FavoriteChapterModel favoriteData;
}

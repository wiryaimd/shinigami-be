package com.shinigami.api.dto;

import com.shinigami.api.model.favorite.FavoriteComicModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteComicDto {

    private String userId;
    private FavoriteComicModel favoriteComicModel;

}

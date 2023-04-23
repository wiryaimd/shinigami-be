package com.shinigami.api.model;

import com.shinigami.api.model.favorite.FavoriteChapterModel;
import com.shinigami.api.model.favorite.FavoriteComicModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userId;
    private String email;
    private boolean isPremium;

    @OneToMany(mappedBy = "userModel")
    private List<FavoriteComicModel> favoriteComicList;

    @OneToMany(mappedBy = "userModel")
    private List<FavoriteChapterModel> favoriteChapterList;

    public UserModel(String userId, String email, boolean isPremium) {
        this.userId = userId;
        this.email = email;
        this.isPremium = isPremium;
    }
}

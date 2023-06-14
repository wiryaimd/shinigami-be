/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "_users", indexes = { @Index(name = "index_user_model_uid", columnList = "userId") })
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
    private LocalDateTime premiumDate;

    // val: 30, 90, 180, 360 (day)
    private int premiumDay = -1;

    @OneToMany(mappedBy = "userModel")
    private List<FavoriteComicModel> favoriteComicList;

    @OneToMany(mappedBy = "userModel")
    private List<FavoriteChapterModel> favoriteChapterList;

    @OneToMany(mappedBy = "userModel")
    private List<UserHistoryModel> historyList;

    public UserModel(String userId, String email, boolean isPremium) {
        this.userId = userId;
        this.email = email;
        this.isPremium = isPremium;
    }
}

/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shinigami.api.model.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "_favorite_chapter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteChapterModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title, url, cover, chapter;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UserModel userModel;

    public FavoriteChapterModel(String title, String url, String cover, String chapter, UserModel userModel) {
        this.title = title;
        this.url = url;
        this.cover = cover;
        this.chapter = chapter;
        this.userModel = userModel;
    }
}

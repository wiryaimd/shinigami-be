/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "_users_history",
        indexes = { @Index(name = "index_history_comic_url", columnList = "chapterUrl") } // pakai index jika diperlukan
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comicUrl;
    private String chapterTitle, chapterUrl;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UserModel userModel;

    public UserHistoryModel(String comicUrl, String chapterTitle, String chapterUrl, UserModel userModel) {
        this.comicUrl = comicUrl;
        this.chapterTitle = chapterTitle;
        this.chapterUrl = chapterUrl;
        this.userModel = userModel;
    }
}

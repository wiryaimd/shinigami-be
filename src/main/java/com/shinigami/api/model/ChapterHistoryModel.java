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

import java.util.List;

@Entity
@Table(name = "_history_chapter"
//        indexes = { @Index(name = "index_history_comic_chapurl", columnList = "chapterUrl", unique = true) } // pakai index jika diperlukan
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String chapterTitle, chapterUrl;

    @ManyToOne
    @JoinColumn(name = "id_comic")
    @JsonIgnore
    private ComicHistoryModel comicHistoryModel;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UserModel userModel;

    public ChapterHistoryModel(String chapterTitle, String chapterUrl, ComicHistoryModel comicHistoryModel, UserModel userModel) {
        this.chapterTitle = chapterTitle;
        this.chapterUrl = chapterUrl;
        this.comicHistoryModel = comicHistoryModel;
        this.userModel = userModel;
    }
}

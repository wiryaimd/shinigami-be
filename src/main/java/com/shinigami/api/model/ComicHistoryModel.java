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
@Table(name = "_history_comic",
        indexes = { @Index(name = "history_comic_url", columnList = "comicUrl") }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComicHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comicUrl;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UserModel userModel;

    @OneToMany(mappedBy = "comicHistoryModel")
    private List<ChapterHistoryModel> chapterHistoryList;

    public ComicHistoryModel(String comicUrl, UserModel userModel) {
        this.comicUrl = comicUrl;
        this.userModel = userModel;
    }
}

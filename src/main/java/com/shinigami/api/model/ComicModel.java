/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComicModel {

    private String title, url, cover;
    private String latestChapter, latestChapterUrl;
    private float rating;

    public ComicModel(String title, String url, String cover) {
        this.title = title;
        this.url = url;
        this.cover = cover;
    }
}

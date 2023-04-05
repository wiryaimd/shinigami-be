package com.shinigami.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendingModel {

    private String title, url, cover;
    private String latestChapter, latestChapterUrl;
    private float rating;

}

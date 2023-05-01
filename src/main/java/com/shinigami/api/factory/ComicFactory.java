/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.factory;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ComicFactory {

    private List<String> titleList, urlList, coverList, chapterList, chapterUrlList;
    private List<Float> ratingList;

    public ComicFactory() {
        titleList = new ArrayList<>();
        urlList = new ArrayList<>();
        ratingList = new ArrayList<>();
        coverList = new ArrayList<>();
        chapterList = new ArrayList<>();
        chapterUrlList = new ArrayList<>();

    }
}

package com.shinigami.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComicDetailModel {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail{
        private String name, value;
    }

    private String synopsis;
    private List<Detail> detailList;
    private List<ChapterModel> chapterList;
    private List<ComicModel> related;

}

package com.shinigami.api.factory;

import com.shinigami.api.model.ChapterModel;
import com.shinigami.api.model.ComicDetailModel;
import com.shinigami.api.model.ComicModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ComicDetailFactory {

    private List<ComicDetailModel.Detail> detailList;
    private List<ChapterModel> chapterList;
    private List<ComicModel> relatedList;

    public ComicDetailFactory(){
        detailList = new ArrayList<>();
        chapterList = new ArrayList<>();
        relatedList = new ArrayList<>();
    }
}

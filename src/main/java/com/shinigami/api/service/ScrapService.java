package com.shinigami.api.service;

import com.shinigami.api.dto.FilterDto;
import com.shinigami.api.factory.ComicDetailFactory;
import com.shinigami.api.factory.ComicFactory;
import com.shinigami.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScrapService {

    public static final String TRENDING = "trending";
    public static final String LATEST = "latest";
    public static final String AZ = "alphabet";
    public static final String RATING = "rating";
    public static final String VIEWS = "views";
    public static final String NEW = "new-manga";

    public BrowseModel scrapBrowse() throws IOException {
        Document document = Jsoup.connect("https://shinigami.id/")
                .userAgent("Mozilla/5.0")
                .get();


        List<ComicModel> all = scrapHome(document);
        List<ComicModel> hotList = all.size() >= 8 ? all.stream().limit(8).toList() : all;
        List<ComicModel> newsList = all.size() >= 15 ? all.stream().skip(8).limit(15).toList() : List.of();

        List<ComicModel> trendingList = scrapBy(TRENDING, 1, false);

        return new BrowseModel(hotList, newsList, trendingList);
    }

    public List<ComicModel> scrapHome(Document document){

        ComicFactory factory = new ComicFactory();

        Elements comicElement = document.select("h5.series-title");
        for (Element element : comicElement) {
            factory.getTitleList().add(element.text());
        }

        Elements urlElement = document.select("a.series-link");
        for (Element element : urlElement) {
            factory.getUrlList().add(element.attr("abs:href"));
        }

        int minSize = Math.min(factory.getTitleList().size(), factory.getUrlList().size());

        Elements imageElement = document.select("img.thumb-img");
        for(Element element : imageElement){
            String imgUrl = element.attr("abs:src");
            factory.getCoverList().add(imgUrl);
        }

        Elements chapterElement = document.select("div.series-content a");
        for (int i = 0; i < chapterElement.size(); i += 2) {
            factory.getChapterUrlList().add(chapterElement.get(i).attr("abs:href"));
        }

        Elements chapterNameElement = chapterElement.select("div.series-chapter-item").select("span.series-badge");
        for (int i = 0; i < chapterNameElement.size(); i += 2) {
            factory.getChapterList().add(chapterNameElement.get(i).text());
        }

        List<ComicModel> comicList = new ArrayList<>();
        for (int i = 0; i < minSize; i++) {
            ComicModel comicModel = new ComicModel(
                    factory.getTitleList().get(i),
                    factory.getUrlList().get(i),
                    factory.getCoverList().get(i),
                    factory.getChapterList().get(i),
                    factory.getChapterUrlList().get(i),
                    0
            );
            comicList.add(comicModel);
        }

        return comicList;
    }

    private int count = 0;
    private int countFilter = 0;


    public List<ComicModel> scrapBy(String by, int page, boolean isMultiple) throws IOException {
        String url = String.format("https://shinigami.id/semua-series/page/%d/?m_orderby=%s", page, by);
        return scrapBy(url, by, page, isMultiple);
    }

    public List<ComicModel> scrapBy(String url, String by, int page, boolean isMultiple) throws IOException {
        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();
        }catch (HttpStatusException e){
            e.printStackTrace();
            return List.of();
        }

        ComicFactory factory = new ComicFactory();

        Elements comicElement = document.select("h3.h5").select("a");
        for (Element element : comicElement) {
            factory.getTitleList().add(element.text());
            factory.getUrlList().add(element.attr("abs:href"));
        }

        int minSize = Math.min(factory.getTitleList().size(), factory.getUrlList().size());

        Elements ratingElement = document.select("span.score");
        for (Element element : ratingElement){
            factory.getRatingList().add(Float.parseFloat(element.text()));
        }

        Elements imageElement = document.select("img.img-responsive");
        for(Element element : imageElement){
            String imgUrl = element.attr("abs:data-src");
            if (imgUrl.isEmpty()){
                continue;
            }

            factory.getCoverList().add(imgUrl);
        }

        Elements chapterElement = document.select("span.chapter a.btn-link");

        for (int i = 0; i < chapterElement.size(); i += 2) {
            Element element = chapterElement.get(i);

            factory.getChapterList().add(element.text());
            factory.getChapterUrlList().add(element.attr("abs:href"));
        }

        List<ComicModel> comicList = new ArrayList<>();

        for (int i = 0; i < minSize; i++) {
            String chapter = "";
            String chapterUrl = "";

            if (i < factory.getChapterList().size()) {
                chapter = factory.getChapterList().get(i);
                chapterUrl = factory.getChapterUrlList().get(i);
            }

            ComicModel comicModel = new ComicModel(
                    factory.getTitleList().get(i),
                    factory.getUrlList().get(i),
                    factory.getCoverList().get(i),
                    chapter,
                    chapterUrl,
                    factory.getRatingList().get(i)
            );
            comicList.add(comicModel);
        }

        if (isMultiple && count < 1){
            count += 1;

            comicList.addAll(scrapBy(by, page + 1, isMultiple));
        }else{
            count = 0;
        }

        return comicList;
    }

    public ComicDetailModel scrapDetail(String url) throws IOException {
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();

        ComicDetailFactory comicDetailFactory = new ComicDetailFactory();

        StringBuilder synopsisSb = new StringBuilder();
        Elements synopsisElement = document.select("div.summary__content p");
        for (Element element : synopsisElement){
            synopsisSb.append(element.text()).append("\n\n");
        }

        comicDetailFactory.getDetailList().add(new ComicDetailModel.Detail("rating_num", document.select("div.post-rating div.post-total-rating span.score").text()));

        Elements detailElement = document.select("div.post-content div.post-content_item");
        for (Element element : detailElement){
            String name = element.select("div.summary-heading h5").text();
            String value = element.select("div.summary-content").text();

            comicDetailFactory.getDetailList().add(new ComicDetailModel.Detail(name, value));
        }
;
        String cleaned = url.endsWith("/") ? url : url + "/";

        Document chapterDocument = Jsoup.connect(String.format("%sajax/chapters/", cleaned))
                .userAgent("Mozilla/5.0")
                .post();

        Elements chapterElement = chapterDocument.select("li.wp-manga-chapter");
        for (Element element : chapterElement){
            String chapterCover = element.select("img.thumb").attr("abs:src");
            String chapterUrl = element.select("div.chapter-link a").attr("abs:href");
            String chapterTitle = element.select("p.chapter-manhwa-title").text();
            String releaseDate = element.select("span.chapter-release-date").text();

            comicDetailFactory.getChapterList().add(new ChapterModel(
                    chapterTitle,
                    chapterUrl,
                    chapterCover,
                    releaseDate
            ));
        }

        Elements relatedElement = document.select("div.related-reading-img");
        for (Element element : relatedElement){
            String relatedTitle = element.select("a").attr("title");
            String relatedUrl = element.select("a").attr("abs:href");
            String relatedCover = element.select("img").attr("abs:data-src");

            comicDetailFactory.getRelatedList().add(new ComicModel(
                    relatedTitle, relatedUrl, relatedCover
            ));
        }

        return new ComicDetailModel(synopsisSb.toString(), comicDetailFactory.getDetailList(), comicDetailFactory.getChapterList(), comicDetailFactory.getRelatedList());
    }

    public ChapterDetailModel scrapChapter(String url) throws IOException {
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();

        List<String> imgList = new ArrayList<>();
        Elements imgElement = document.select("div.page-break img");
        for(Element element : imgElement){
            String img = element.attr("abs:data-src");

            imgList.add(img);
        }

        return new ChapterDetailModel(imgList);
    }

    public List<ComicModel> scrapSearch(String keyword, int page) throws IOException {
        String url = String.format("https://shinigami.id/page/%d/?s=%s&post_type=wp-manga", page, keyword);

        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();
        }catch (HttpStatusException e){
            e.printStackTrace();
            return List.of();
        }

        ComicFactory factory = new ComicFactory();

        Elements comicElement = document.select("h3.h4").select("a");
        for (Element element : comicElement) {
            factory.getTitleList().add(element.text());
            factory.getUrlList().add(element.attr("abs:href"));
        }

        int minSize = Math.min(factory.getTitleList().size(), factory.getUrlList().size());

        Elements ratingElement = document.select("span.score.total_votes");
        for (Element element : ratingElement){
            factory.getRatingList().add(Float.parseFloat(element.text()));
        }

        Elements imageElement = document.select("div.tab-thumb.c-image-hover img");
        for(Element element : imageElement){
            String imgUrl = element.attr("abs:data-src");
            factory.getCoverList().add(imgUrl);
        }

        Elements chapterElement = document.select("span.font-meta.chapter a");

        for (Element element : chapterElement) {
            factory.getChapterList().add(element.text());
            factory.getChapterUrlList().add(element.attr("abs:href"));
        }

        List<ComicModel> comicList = new ArrayList<>();

        for (int i = 0; i < minSize; i++) {
            ComicModel comicModel = new ComicModel(
                    factory.getTitleList().get(i),
                    factory.getUrlList().get(i),
                    factory.getCoverList().get(i),
                    factory.getChapterList().get(i),
                    factory.getChapterUrlList().get(i),
                    factory.getRatingList().get(i)
            );
            comicList.add(comicModel);
        }

        return comicList;
    }

    public List<ComicModel> scrapFilter(FilterDto filterDto, int page) throws IOException {
        String url = String.format("https://shinigami.id/genre/%s/page/%d/?m_orderby=%s", filterDto.getGenre(), page, filterDto.getSortBy());

        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();
        }catch (HttpStatusException e){
            e.printStackTrace();
            return List.of();
        }

        ComicFactory factory = new ComicFactory();

        Elements comicElement = document.select("h3.h5").select("a");
        for (Element element : comicElement) {
            factory.getTitleList().add(element.text());
            factory.getUrlList().add(element.attr("abs:href"));
        }

        int minSize = Math.min(factory.getTitleList().size(), factory.getUrlList().size());

        Elements ratingElement = document.select("span.score");
        for (Element element : ratingElement){
            factory.getRatingList().add(Float.parseFloat(element.text()));
        }

        Elements imageElement = document.select("div.item-thumb.c-image-hover img");
        for(Element element : imageElement){
            String imgUrl = element.attr("abs:data-src");
            factory.getCoverList().add(imgUrl);
        }

        Elements chapterElement = document.select("span.chapter a.btn-link");

        for (int i = 0; i < chapterElement.size(); i += 2) {
            Element element = chapterElement.get(i);

            factory.getChapterList().add(element.text());
            factory.getChapterUrlList().add(element.attr("abs:href"));
        }

        List<ComicModel> comicList = new ArrayList<>();

        for (int i = 0; i < minSize; i++) {
            ComicModel comicModel = new ComicModel(
                    factory.getTitleList().get(i),
                    factory.getUrlList().get(i),
                    factory.getCoverList().get(i),
                    factory.getChapterList().get(i),
                    factory.getChapterUrlList().get(i),
                    factory.getRatingList().get(i)
            );
            comicList.add(comicModel);
        }

        if (countFilter < 1){
            countFilter += 1;
            comicList.addAll(scrapFilter(filterDto,page + 1));
        }else{
            countFilter = 0;
        }

        return comicList;
    }
}

package com.shinigami.api.service;

import com.shinigami.api.factory.ComicDetailFactory;
import com.shinigami.api.factory.ComicFactory;
import com.shinigami.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScrapService {

    public BrowseModel scrapBrowse() throws IOException {
        Document document = Jsoup.connect("https://shinigami.id/")
                .userAgent("Mozilla/5.0")
                .get();


        List<ComicModel> all = scrapHome(document);
        List<ComicModel> hotList = all.size() >= 8 ? all.stream().limit(8).toList() : all;
        List<ComicModel> newsList = all.size() >= 15 ? all.stream().skip(8).limit(15).toList() : List.of();

        List<ComicModel> trendingList = scrapTrending(1);

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

    public List<ComicModel> scrapTrending(int page) throws IOException {
        String url = String.format("https://shinigami.id/semua-series/page/%d/?m_orderby=trending", page);
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();

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

        Elements chapterElement = document.select("a.btn-link");

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
            log.info("sinop: {}", element.text());
            synopsisSb.append(element.text()).append("\n\n");
        }

        comicDetailFactory.getDetailList().add(new ComicDetailModel.Detail("rating_num", document.select("div.post-rating div.post-total-rating span.score").text()));

        Elements detailElement = document.select("div.post-content div.post-content_item");
        for (Element element : detailElement){
            String name = element.select("div.summary-heading h5").text();
            String value = element.select("div.summary-content").text();

            log.info("name: {} = {}", name, value);

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

            log.info("img: {}", chapterCover);
            log.info("chapUrl: {}", chapterUrl);
            log.info("title: {}", chapterTitle);

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

            log.info("related title: {}", relatedTitle);
            log.info("related url: {}", relatedUrl);
            log.info("related img: {}", relatedCover);

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
            log.info("img: {}", img);

            imgList.add(img);
        }

        return new ChapterDetailModel(imgList);
    }
}

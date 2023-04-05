package com.shinigami.api.service;

import com.shinigami.api.factory.ComicFactory;
import com.shinigami.api.model.BrowseModel;
import com.shinigami.api.model.ComicModel;
import lombok.extern.slf4j.Slf4j;
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

        Elements chapterElement = document.select("div.series-content");
        for (int i = 0; i < chapterElement.size(); i++) {
            log.info("chapter: {}" ,chapterElement.get(i).attr("a abs:href"));
        }

        Elements chapterNameElement = chapterElement.select("div.series-chapter-item").select("span.series-badge");
        for (int i = 0; i < chapterNameElement.size(); i++) {
            log.info("chapterName: {}", chapterNameElement.get(i).text());
        }

        List<ComicModel> comicList = new ArrayList<>();
        for (int i = 0; i < minSize; i++) {
            ComicModel comicModel = new ComicModel(
                    factory.getTitleList().get(i),
                    factory.getUrlList().get(i),
                    factory.getCoverList().get(i),
                    "",
                    "",
                    -1
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

}

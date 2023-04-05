package com.shinigami.api.service;

import com.shinigami.api.model.BrowseModel;
import com.shinigami.api.model.TrendingModel;
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

        Elements elements = document.select("a.series-link");

        List<String> all = new ArrayList<>();

        for (Element element : elements) {
            all.add(element.attr("abs:href"));
        }

        List<String> hotList = all.size() >= 8 ? all.stream().limit(8).toList() : all;
        List<String> newsList = all.size() >= 16 ? all.stream().skip(8).limit(16).toList() : List.of();
//        List<String> udpateList = all.size() >= 16 ? all.stream().skip(8).limit(16).toList() : List.of();

        return new BrowseModel(hotList, newsList, null);
    }

    public List<TrendingModel> scrapTrending(int page) throws IOException {
        String url = String.format("https://shinigami.id/semua-series/page/%d/?m_orderby=trending", page);
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();

        List<String> titleList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        List<Float> ratingList = new ArrayList<>();
        List<String> coverList = new ArrayList<>();
        List<String> chapterList = new ArrayList<>();
        List<String> chapterUrlList = new ArrayList<>();

        Elements comicElement = document.select("h3.h5").select("a");
        for (Element element : comicElement) {
            titleList.add(element.text());
            urlList.add(element.attr("abs:href"));

            log.info("title: {}", element.text());
            log.info("url: {}\n", element.attr("abs:href"));
        }

        int minSize = Math.min(titleList.size(), urlList.size());

        Elements ratingElement = document.select("span.score");
        for (Element element : ratingElement){
            ratingList.add(Float.parseFloat(element.text()));

            log.info("rating: {}", element.text());
        }

        Elements imageElement = document.select("img.img-responsive");
        for(Element element : imageElement){
            String imgUrl = element.attr("abs:data-src");
            if (imgUrl.isEmpty()){
                continue;
            }

            coverList.add(imgUrl);

            log.info("image-url: {}", imgUrl);
        }

        Elements chapterElement = document.select("a.btn-link");

        for (int i = 0; i < chapterElement.size(); i += 2) {
            Element element = chapterElement.get(i);

            chapterList.add(element.text());
            chapterUrlList.add(element.attr("abs:href"));

            log.info("chapter: {}", element.text());
            log.info("chapter-url: {}\n", element.attr("abs:href"));
        }

        List<TrendingModel> trendingList = new ArrayList<>();
        for (int i = 0; i < minSize; i++) {
            TrendingModel trendingModel = new TrendingModel(
                    titleList.get(i),
                    urlList.get(i),
                    coverList.get(i),
                    chapterList.get(i),
                    chapterUrlList.get(i),
                    ratingList.get(i)
            );
            trendingList.add(trendingModel);
        }

        return trendingList;
    }

}

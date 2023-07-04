/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service.reactive;

import com.shinigami.api.dto.FilterDto;
import com.shinigami.api.exception.ScrapFailException;
import com.shinigami.api.factory.ComicDetailFactory;
import com.shinigami.api.factory.ComicFactory;
import com.shinigami.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.StreamEndEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.*;

@Service
@Slf4j
public class ScrapServiceR {

    public static final String TRENDING = "trending";
    public static final String LATEST = "latest";
    public static final String AZ = "alphabet";
    public static final String RATING = "rating";
    public static final String VIEWS = "views";
    public static final String NEW = "new-manga";

    public Mono<BrowseModel> scrapBrowseV3(){
        return Mono.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                return Jsoup.connect("https://shinigami.id/")
                        .userAgent("Mozilla/5.0")
                        .get();
            }
        }).flatMap(new Function<Document, Mono<? extends BrowseModel>>() {
            @Override
            public Mono<? extends BrowseModel> apply(Document document) {
                return scrapHome(document).zipWith(scrapBy(TRENDING, 1, false)).map(new Function<Tuple2<List<ComicModel>, List<ComicModel>>, BrowseModel>() {
                    @Override
                    public BrowseModel apply(Tuple2<List<ComicModel>, List<ComicModel>> objects) {
                        List<ComicModel> all = objects.getT1();

                        List<ComicModel> hotList = all.size() >= 8 ? all.stream().limit(8).toList() : all;
                        List<ComicModel> newsList = all.size() >= 15 ? all.stream().skip(8).limit(15).toList() : List.of();
                        List<ComicModel> trendingList = objects.getT2();

                        return new BrowseModel(hotList, newsList, trendingList);
                    }
                });
            }
        }).onErrorResume(new Function<Throwable, Mono<? extends BrowseModel>>() {
            @Override
            public Mono<? extends BrowseModel> apply(Throwable throwable) {
                return Mono.just(new BrowseModel(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private int count = 0;
    private int countFilter = 0;

    public Mono<List<ComicModel>> scrapBy(String by, int page, boolean isMultiple) {
        try {
            String url = String.format("https://shinigami.id/semua-series/page/%d/?m_orderby=%s", page, by);
            return scrapBy(url, by, page, isMultiple);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public Mono<List<ComicModel>> scrapBy(String url, String by, int page, boolean isMultiple) throws IOException {
        return Mono.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                return Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .get();
            }
        }).map(new Function<Document, List<ComicModel>>() {
            @Override
            public List<ComicModel> apply(Document document) {
                return processScrapBy(document, page, isMultiple);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private List<ComicModel> processScrapBy(Document document, int page, boolean isMultiple){
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

            comicList.addAll(processScrapBy(document, page + 1, isMultiple));
        }else{
            count = 0;
        }
        return comicList;
    }

    private Mono<List<ComicModel>> scrapHome(Document document) {
        return Mono.just(document).map(new Function<Document, List<ComicModel>>() {
            @Override
            public List<ComicModel> apply(Document document) {

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
        });
    }

    public Mono<ComicDetailModel> scrapDetail(String url) {
        return Mono.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                return Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .get();
            }
        }).zipWith(Mono.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                String cleaned = url.endsWith("/") ? url : url + "/";

                 return Jsoup.connect(String.format("%sajax/chapters/", cleaned))
                        .userAgent("Mozilla/5.0")
                        .post();
            }
        })).flatMap(new Function<Tuple2<Document, Document>, Mono<? extends ComicDetailModel>>() {
            @Override
            public Mono<? extends ComicDetailModel> apply(Tuple2<Document, Document> objects) {
                ComicDetailFactory comicDetailFactory = new ComicDetailFactory();

                Document document = objects.getT1();
                Document chapterDocument = objects.getT2();

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

                return Mono.just(new ComicDetailModel(synopsisSb.toString(), comicDetailFactory.getDetailList(), comicDetailFactory.getChapterList(), comicDetailFactory.getRelatedList()));
            }
        })
                // ketika tidak menggunkan onError() maka error yg occur pada Mono.fromCallable()
                // akan disebarkan ke controllerAdvice jika itu memang ada, jika tidak maka spring akan return 500 pada res code
//                .onErrorResume(new Function<Throwable, Mono<? extends ComicDetailModel>>() {
//            @Override
//            public Mono<? extends ComicDetailModel> apply(Throwable throwable) {
//                return null;
//            }
//        }
        .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<ChapterDetailModel> scrapChapter(String url) throws IOException {
        return Mono.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                return Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .get();
            }
        }).map(new Function<Document, ChapterDetailModel>() {
            @Override
            public ChapterDetailModel apply(Document document) {
                List<String> imgList = new ArrayList<>();
                Elements imgElement = document.select("div.page-break img");
                for(Element element : imgElement){
                    String img = element.attr("abs:data-src");

                    imgList.add(img);
                }

                return new ChapterDetailModel(imgList);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<List<ComicModel>> scrapSearch(String keyword, int page) throws IOException {
        return Mono.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                String url = String.format("https://shinigami.id/page/%d/?s=%s&post_type=wp-manga", page, keyword);

                return Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .get();
            }
        }).map(new Function<Document, List<ComicModel>>() {
            @Override
            public List<ComicModel> apply(Document document) {
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
        }).onErrorReturn(List.of()).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<List<ComicModel>> scrapFilter(FilterDto filterDto, int page) throws IOException {
        return Mono.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                String url = String.format("https://shinigami.id/genre/%s/page/%d/?m_orderby=%s", filterDto.getGenre(), page, filterDto.getSortBy());

                return Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .get();
            }
        }).map(new Function<Document, List<ComicModel>>() {
            @Override
            public List<ComicModel> apply(Document document) {
                return processScrapFilter(document, filterDto, page);
            }

        }).onErrorReturn(List.of()).subscribeOn(Schedulers.boundedElastic());
    }


//    private Flux<List<ComicModel>> a(FilterDto filterDto, int page){
//        return Mono.fromCallable(new Callable<Document>() {
//            @Override
//            public Document call() throws Exception {
//                String url = String.format("https://shinigami.id/genre/%s/page/%d/?m_orderby=%s", filterDto.getGenre(), page, filterDto.getSortBy());
//
//                return Jsoup.connect(url)
//                        .userAgent("Mozilla/5.0")
//                        .get();
//            }
//        }).zipWith(Mono.just(0)).expand(new Function<Tuple2<Document, Integer>, Publisher<? extends Tuple3<Document, Integer, List<ComicModel>>>>() {
//            @Override
//            public Publisher<? extends Tuple3<Document, Integer, List<ComicModel>>> apply(Tuple2<Document, Integer> objects) {
//                Document document = objects.getT1();
//                int countFilter = objects.getT2();
//
//                ComicFactory factory = new ComicFactory();
//
//                Elements comicElement = document.select("h3.h5").select("a");
//                for (Element element : comicElement) {
//                    factory.getTitleList().add(element.text());
//                    factory.getUrlList().add(element.attr("abs:href"));
//                }
//
//                int minSize = Math.min(factory.getTitleList().size(), factory.getUrlList().size());
//
//                Elements ratingElement = document.select("span.score");
//                for (Element element : ratingElement){
//                    factory.getRatingList().add(Float.parseFloat(element.text()));
//                }
//
//                Elements imageElement = document.select("div.item-thumb.c-image-hover img");
//                for(Element element : imageElement){
//                    String imgUrl = element.attr("abs:data-src");
//                    factory.getCoverList().add(imgUrl);
//                }
//
//                Elements chapterElement = document.select("span.chapter a.btn-link");
//
//                for (int i = 0; i < chapterElement.size(); i += 2) {
//                    Element element = chapterElement.get(i);
//
//                    factory.getChapterList().add(element.text());
//                    factory.getChapterUrlList().add(element.attr("abs:href"));
//                }
//
//                List<ComicModel> comicList = new ArrayList<>();
//
//                for (int i = 0; i < minSize; i++) {
//                    ComicModel comicModel = new ComicModel(
//                            factory.getTitleList().get(i),
//                            factory.getUrlList().get(i),
//                            factory.getCoverList().get(i),
//                            factory.getChapterList().get(i),
//                            factory.getChapterUrlList().get(i),
//                            factory.getRatingList().get(i)
//                    );
//                    comicList.add(comicModel);
//                }
//
//                if (countFilter < 1){
////                    comicList.addAll(processScrapFilter(document, filterDto, page + 1));
//                    return Mono.just(Tuples.of(document, countFilter + 1, comicList));
//                }else{
//                    return Mono.empty();
//                }
//            }
//        }).map(new Function<Tuple2<Document, Integer>, List<ComicModel>>() {
//            @Override
//            public List<ComicModel> apply(Tuple2<Document, Integer> objects) {
//
//                return comicList;
//            }
//        });
//    }

    private List<ComicModel> processScrapFilter(Document document, FilterDto filterDto, int page) {
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
            comicList.addAll(processScrapFilter(document, filterDto, page + 1));
        }else{
            countFilter = 0;
        }

        return comicList;
    }
}

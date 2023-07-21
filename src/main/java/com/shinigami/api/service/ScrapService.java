/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service;

import com.shinigami.api.dto.FilterDto;
import com.shinigami.api.factory.ComicDetailFactory;
import com.shinigami.api.factory.ComicFactory;
import com.shinigami.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.*;

@Service
@Slf4j
public class ScrapService {

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
//                log.info("infoo massehhh");
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
                throwable.printStackTrace();
                return Mono.just(new BrowseModel(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<ComicModel> scrapShortComic(String url) {
        return Mono.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                return Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .get();
            }
        }).flatMap(new Function<Document, Mono<? extends ComicModel>>() {
            @Override
            public Mono<? extends ComicModel> apply(Document document) {
                return processScrapComicShort(url, document);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<ComicModel> processScrapComicShort(String url, Document document) {
        String title = document.select("div.post-title h1").text();
        String coverUrl = document.select("div.summary_image img").attr("abs:data-src");

        return Mono.just(new ComicModel(title, url, coverUrl));
    }

    public Mono<FullComicModel> scrapFullComic(String url){
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
        })).flatMap(new Function<Tuple2<Document, Document>, Mono<FullComicModel>>() {
            @Override
            public Mono<FullComicModel> apply(Tuple2<Document, Document> objects) {
                Document document = objects.getT1();
                Document chapterDocument = objects.getT2();

                Mono<ComicModel> comicModel = processScrapComicShort(url, document);
                Mono<ComicDetailModel> comicDetailModel = processScrapDetail(document, chapterDocument);

                return Mono.zip(comicModel, comicDetailModel).map(new Function<Tuple2<ComicModel, ComicDetailModel>, FullComicModel>() {
                    @Override
                    public FullComicModel apply(Tuple2<ComicModel, ComicDetailModel> objects) {
                        return new FullComicModel(objects.getT1(), objects.getT2());
                    }
                });
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<List<ComicModel>> scrapBy(String by, int page, boolean isMultiple) {
        String url = String.format("https://shinigami.id/semua-series/page/%d/?m_orderby=%s", page, by);
        return scrapBy(url, by, page, isMultiple);
    }

    public Mono<List<ComicModel>> scrapBy(String url, String by, int page, boolean isMultiple) {
        return Mono.fromCallable(new Callable<Tuple2<Document, Document>>() {
            @Override
            public Tuple2<Document, Document> call() throws Exception {
                Document docPage1 = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .get();

                if (isMultiple){
                    String urlPage2 = String.format("https://shinigami.id/semua-series/page/%d/?m_orderby=%s", page + 1, by);

                    Document docPage2 = Jsoup.connect(urlPage2)
                            .userAgent("Mozilla/5.0")
                            .get();

                    return Tuples.of(docPage1, docPage2);
                }

                return Tuples.of(docPage1, new Document(""));
            }
        }).map(new Function<Tuple2<Document, Document>, List<ComicModel>>() {
            @Override
            public List<ComicModel> apply(Tuple2<Document, Document> objects) {
                Document docPage1 = objects.getT1();
                List<ComicModel> comicList = processScrapBy(docPage1);

                if (isMultiple){
                    Document docPage2 = objects.getT2();
                    comicList.addAll(processScrapBy(docPage2));
                }

                return comicList;
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private List<ComicModel> processScrapBy(Document document){
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

                Document document = objects.getT1();
                Document chapterDocument = objects.getT2();

                return processScrapDetail(document, chapterDocument);
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

    private Mono<ComicDetailModel> processScrapDetail(Document document, Document chapterDocument) {
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

    public Mono<ChapterDetailModel> scrapChapter(String url) {
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

    public Mono<List<ComicModel>> scrapSearch(String keyword, int page) {
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

    public Mono<List<ComicModel>> scrapFilter(FilterDto filterDto, int page){
         return Mono.fromCallable(new Callable<Tuple2<Document, Document>>() {
             @Override
             public Tuple2<Document, Document> call() throws Exception {
                 String urlPage1 = String.format("https://shinigami.id/genre/%s/page/%d/?m_orderby=%s", filterDto.getGenre(), page, filterDto.getSortBy());
                 String urlPage2 = String.format("https://shinigami.id/genre/%s/page/%d/?m_orderby=%s", filterDto.getGenre(), page + 1, filterDto.getSortBy());

                 Document docPage1 = Jsoup.connect(urlPage1)
                         .userAgent("Mozilla/5.0")
                         .get();

                 Document docPage2 = Jsoup.connect(urlPage2)
                         .userAgent("Mozilla/5.0")
                         .get();

                 return Tuples.of(docPage1, docPage2);
             }
         }).map(new Function<Tuple2<Document, Document>, List<ComicModel>>() {
             @Override
             public List<ComicModel> apply(Tuple2<Document, Document> objects) {
                 Document page1 = objects.getT1();
                 Document page2 = objects.getT2();

                 List<ComicModel> comicList = processScrapFilter(page1);
                 comicList.addAll(processScrapFilter(page2));
                 return comicList;
             }
         }).onErrorReturn(List.of()).subscribeOn(Schedulers.boundedElastic());
    }

    private List<ComicModel> processScrapFilter(Document document){
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
        return comicList;
    }
}

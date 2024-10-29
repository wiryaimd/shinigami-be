/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller;

import com.shinigami.api.dto.FilterDto;
import com.shinigami.api.model.*;
import com.shinigami.api.service.ScrapService;
import com.shinigami.api.util.Const;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ComicController {

    private final ScrapService scrapService;

    @GetMapping("/browse")
    private Mono<BrowseModel> browse(){
        return scrapService.scrapBrowseV3();
    }

    @GetMapping("/comic")
    public Mono<ComicDetailModel> detail(@RequestParam(name = "url") String url) {
        return scrapService.scrapDetail(url);
    }

    @GetMapping("/comic/short")
    public Mono<ComicModel> comicShort(@RequestParam(name = "url") String url){
        return scrapService.scrapShortComic(url);
    }

    @GetMapping("/comic/full")
    public Mono<ComicFullModel> comicFull(@RequestParam(name = "url") String url){
        return scrapService.scrapFullComic(url);
    }

    @GetMapping("/chapter")
    public Mono<ChapterDetailModel> chapterDetail(@RequestParam(name = "url") String url) {
//        return scrapService.scrapChapter(url);
        return Mono.just(new ChapterDetailModel(List.of("https://i.postimg.cc/dtmzXCsx/Screenshot-2023-11-16-222124.png")));
    }

    @GetMapping("/search")
    public Mono<List<ComicModel>> search(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()){
            return Mono.just(new ArrayList<>());
        }
        return scrapService.scrapSearch(keyword, page);
    }

    @GetMapping("/projects")
    public Mono<List<ComicModel>> projects(@RequestParam(name = "page", defaultValue = "1") int page) {
        String url = String.format("https://" + Const.currentDomain + "/project/page/%d", page);
        return scrapService.scrapBy(url, "", page, false);
    }

    @GetMapping("/mirror")
    public Mono<List<ComicModel>> mirror(@RequestParam(name = "page", defaultValue = "1") int page) {
        String url = String.format("https://" + Const.currentDomain + "/mirror/page/%d", page);
        return scrapService.scrapBy(url, "", page, false);
    }

    @PostMapping("/filter")
    public Mono<List<ComicModel>> filter(@RequestBody FilterDto filterDto, @RequestParam(name = "page", defaultValue = "1") int page) {
        return scrapService.scrapFilter(filterDto, page);
    }

    @GetMapping("/filter/trending")
    public Mono<List<ComicModel>> trending(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapService.TRENDING, page, isMultiple);
    }

    @GetMapping("/filter/latest")
    public Mono<List<ComicModel>> latest(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        log.info("is multiple {}", isMultiple);
        return scrapService.scrapBy(ScrapService.LATEST, page, isMultiple);
    }

    @GetMapping("/filter/az")
    public Mono<List<ComicModel>> az(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapService.AZ, page, isMultiple);
    }

    @GetMapping("/filter/rating")
    public Mono<List<ComicModel>> rating(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapService.RATING, page, isMultiple);
    }

    @GetMapping("/filter/views")
    public Mono<List<ComicModel>> views(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapService.VIEWS, page, isMultiple);
    }

    @GetMapping("/filter/new")
    public Mono<List<ComicModel>> newManga(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapService.NEW, page, isMultiple);
    }

    @PostMapping("/promote")
    public ResponseEntity<Void> promoteLul(@RequestBody List<String> list, @RequestParam(name = "auth") String auth){
        if (!auth.equalsIgnoreCase("still crushing lianmi 8ce117da")){
            return ResponseEntity.status(404).build();
        }

        scrapService.savePromote(list);
        return ResponseEntity.ok(null);
    }

}

/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller.reactive;

import com.shinigami.api.dto.FilterDto;
import com.shinigami.api.model.BrowseModel;
import com.shinigami.api.model.ChapterDetailModel;
import com.shinigami.api.model.ComicDetailModel;
import com.shinigami.api.model.ComicModel;
import com.shinigami.api.service.ScrapService;
import com.shinigami.api.service.reactive.ScrapServiceR;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Slf4j
public class ComicControllerR {

    private final ScrapServiceR scrapService;

    @GetMapping("/browse")
    private Mono<BrowseModel> browse(){
        return scrapService.scrapBrowseV3();
    }

    @GetMapping("/comic")
    public Mono<ComicDetailModel> detail(@RequestParam(name = "url") String url) {
        return scrapService.scrapDetail(url);
    }

    @GetMapping("/chapter")
    public Mono<ChapterDetailModel> chapterDetail(@RequestParam(name = "url") String url) {
        return scrapService.scrapChapter(url);
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
        String url = String.format("https://shinigami.id/project/page/%d", page);
        return scrapService.scrapBy(url, "", page, false);
    }

    @GetMapping("/mirror")
    public Mono<List<ComicModel>> mirror(@RequestParam(name = "page", defaultValue = "1") int page) {
        String url = String.format("https://shinigami.id/mirror/page/%d", page);
        return scrapService.scrapBy(url, "", page, false);
    }

    @PostMapping("/filter")
    public Mono<List<ComicModel>> filter(@RequestBody FilterDto filterDto, @RequestParam(name = "page", defaultValue = "1") int page) {
        return scrapService.scrapFilter(filterDto, page);
    }

    @GetMapping("/filter/trending")
    public Mono<List<ComicModel>> trending(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapServiceR.TRENDING, page, isMultiple);
    }

    @GetMapping("/filter/latest")
    public Mono<List<ComicModel>> latest(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        log.info("is multiple {}", isMultiple);
        return scrapService.scrapBy(ScrapServiceR.LATEST, page, isMultiple);
    }

    @GetMapping("/filter/az")
    public Mono<List<ComicModel>> az(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapServiceR.AZ, page, isMultiple);
    }

    @GetMapping("/filter/rating")
    public Mono<List<ComicModel>> rating(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapServiceR.RATING, page, isMultiple);
    }

    @GetMapping("/filter/views")
    public Mono<List<ComicModel>> views(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapServiceR.VIEWS, page, isMultiple);
    }

    @GetMapping("/filter/new")
    public Mono<List<ComicModel>> newManga(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) {
        return scrapService.scrapBy(ScrapServiceR.NEW, page, isMultiple);
    }

}

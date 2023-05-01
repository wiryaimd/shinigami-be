/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller;

import com.shinigami.api.dto.FilterDto;
import com.shinigami.api.model.BrowseModel;
import com.shinigami.api.model.ChapterDetailModel;
import com.shinigami.api.model.ComicDetailModel;
import com.shinigami.api.model.ComicModel;
import com.shinigami.api.service.ScrapService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class ComicController {

    private ScrapService scrapService;

    @GetMapping("/browse")
    public ResponseEntity<BrowseModel> home() throws IOException {
        return ResponseEntity.ok(scrapService.scrapBrowse());
    }

    @GetMapping("/comic")
    public ResponseEntity<ComicDetailModel> detail(@RequestParam(name = "url") String url) throws IOException {
        return ResponseEntity.ok(scrapService.scrapDetail(url));
    }

    @GetMapping("/chapter")
    public ResponseEntity<ChapterDetailModel> chapterDetail(@RequestParam(name = "url") String url) throws IOException {
        return ResponseEntity.ok(scrapService.scrapChapter(url));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ComicModel>> search(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "keyword") String keyword) throws IOException {
        if (keyword == null || keyword.trim().isEmpty()){
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(scrapService.scrapSearch(keyword, page));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ComicModel>> projects(@RequestParam(name = "page", defaultValue = "1") int page) throws IOException {
        String url = String.format("https://shinigami.id/project/page/%d", page);
        return ResponseEntity.ok(scrapService.scrapBy(url, "", page, false));
    }

    @GetMapping("/mirror")
    public ResponseEntity<List<ComicModel>> mirror(@RequestParam(name = "page", defaultValue = "1") int page) throws IOException {
        String url = String.format("https://shinigami.id/mirror/page/%d", page);
        return ResponseEntity.ok(scrapService.scrapBy(url, "", page, false));
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ComicModel>> filter(@RequestBody FilterDto filterDto, @RequestParam(name = "page", defaultValue = "1") int page) throws IOException {
        return ResponseEntity.ok(scrapService.scrapFilter(filterDto, page));
    }

    @GetMapping("/filter/trending")
    public ResponseEntity<List<ComicModel>> trending(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) throws IOException {
        return ResponseEntity.ok(scrapService.scrapBy(ScrapService.TRENDING, page, isMultiple));
    }

    @GetMapping("/filter/latest")
    public ResponseEntity<List<ComicModel>> latest(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) throws IOException {
        log.info("is multiple {}", isMultiple);
        return ResponseEntity.ok(scrapService.scrapBy(ScrapService.LATEST, page, isMultiple));
    }

    @GetMapping("/filter/az")
    public ResponseEntity<List<ComicModel>> az(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) throws IOException {
        return ResponseEntity.ok(scrapService.scrapBy(ScrapService.AZ, page, isMultiple));
    }

    @GetMapping("/filter/rating")
    public ResponseEntity<List<ComicModel>> rating(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) throws IOException {
        return ResponseEntity.ok(scrapService.scrapBy(ScrapService.RATING, page, isMultiple));
    }

    @GetMapping("/filter/views")
    public ResponseEntity<List<ComicModel>> views(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) throws IOException {
        return ResponseEntity.ok(scrapService.scrapBy(ScrapService.VIEWS, page, isMultiple));
    }

    @GetMapping("/filter/new")
    public ResponseEntity<List<ComicModel>> newManga(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "multiple", required = false) boolean isMultiple) throws IOException {
        return ResponseEntity.ok(scrapService.scrapBy(ScrapService.NEW, page, isMultiple));
    }

}

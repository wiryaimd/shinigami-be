package com.shinigami.api.controller;

import com.shinigami.api.model.BrowseModel;
import com.shinigami.api.model.ChapterDetailModel;
import com.shinigami.api.model.ComicDetailModel;
import com.shinigami.api.model.ComicModel;
import com.shinigami.api.service.ScrapService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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

}

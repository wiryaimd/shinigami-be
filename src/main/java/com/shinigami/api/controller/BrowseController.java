package com.shinigami.api.controller;

import com.shinigami.api.model.BrowseModel;
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
public class BrowseController {

    private ScrapService scrapService;

    @GetMapping("/browse")
    public ResponseEntity<BrowseModel> home() throws IOException {
        return ResponseEntity.ok(scrapService.scrapBrowse());
    }

    @GetMapping("/trending")
    public ResponseEntity<List<ComicModel>> trending(@RequestParam(name = "page", defaultValue = "1") int page) throws IOException {
        return ResponseEntity.ok(scrapService.scrapTrending(page));
    }

}

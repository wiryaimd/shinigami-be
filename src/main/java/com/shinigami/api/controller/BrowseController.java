package com.shinigami.api.controller;

import com.shinigami.api.model.BrowseModel;
import com.shinigami.api.model.TrendingModel;
import com.shinigami.api.service.ScrapService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class BrowseController {

    private ScrapService scrapService;

    @GetMapping("/browse")
    public ResponseEntity<BrowseModel> home() throws IOException {
        return ResponseEntity.ok(scrapService.scrapBrowse());
    }

    @GetMapping("/trending")
    public ResponseEntity<List<TrendingModel>> trending() throws IOException {
        return ResponseEntity.ok(scrapService.scrapTrending(1));
    }

}

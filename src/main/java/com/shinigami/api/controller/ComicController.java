package com.shinigami.api.controller;

import com.shinigami.api.model.ComicDetailModel;
import com.shinigami.api.service.ScrapService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class ComicController {

    private ScrapService scrapService;

    @GetMapping("/comic")
    public ResponseEntity<ComicDetailModel> detail(@RequestParam(name = "url") String url) throws IOException {
        return ResponseEntity.ok(scrapService.scrapDetail("https://shinigami.id/series/i-obtained-a-mythic-item"));
    }

}

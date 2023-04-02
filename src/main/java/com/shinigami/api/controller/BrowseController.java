package com.shinigami.api.controller;

import com.shinigami.api.model.BrowseModel;
import com.shinigami.api.service.BrowseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class BrowseController {

    private BrowseService browseService;

    @GetMapping("/browse")
    public ResponseEntity<BrowseModel> home() throws IOException {
        return ResponseEntity.ok(browseService.scrapBrowse());
    }

}

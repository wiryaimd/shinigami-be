/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller.reactive;

import com.shinigami.api.model.BrowseModel;
import com.shinigami.api.service.reactive.ScrapServiceR;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ComicControllerR {

    private final ScrapServiceR scrapService;

    @GetMapping("/browse")
    private Mono<BrowseModel> browse(){
        return scrapService.scrapBrowseV3();
    }

}

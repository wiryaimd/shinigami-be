package com.shinigami.api.controller;

import com.shinigami.api.model.ChapterDetailModel;
import com.shinigami.api.service.ScrapService;
import com.shinigami.api.service.UserService;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Slf4j
public class ComicV2Controller {

    private final ScrapService scrapService;
    private final UserService userService;

    @GetMapping("/chapter")
    public Mono<ChapterDetailModel> chapterDetail(@RequestParam(name = "url") String url, @RequestParam(name = "id", required = false) String userId) {
        if (userId != null && !userId.isEmpty()){
            return Mono.just(new ChapterDetailModel(List.of("https://i.postimg.cc/7h5pdtL1/Screenshot-2023-11-16-221837.png")));
        }
        return scrapService.scrapChapter(url);
    }

    @GetMapping("/chapter/premium")
    public Mono<ChapterDetailModel> chapterDetailPremiium(@RequestParam(name = "url") String url, @RequestParam(name = "id") String userId) {
        boolean isValid = userService.validatePremium(userId);
        if (!isValid){
            return Mono.just(new ChapterDetailModel(List.of("https://i.postimg.cc/7h5pdtL1/Screenshot-2023-11-16-221837.png")));
        }

        return scrapService.scrapChapter(url);
    }
}

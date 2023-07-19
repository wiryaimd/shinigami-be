/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api;

import com.shinigami.api.model.ComicModel;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.service.ScrapService;
import com.shinigami.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

@SpringBootTest(classes = Main.class)
// perlu annotasi ini dgn classes = Main class yg memiliki anot @SpringBootApplication untuk menentukan app nya ada di bagian mana dan
// anotasi ini digunakan agar bisa pake @Autowired, etc
@Slf4j
//@ExtendWith(SpringExtension.class)
public class Test2 {

//    @Autowired
//    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ScrapService scrapService;

    @Test
    void test1(){
        UserModel userModel = userService.checkUser("adnyasutha003@gmail.com");
        log.info("user: {}", userModel.getEmail());

        assertThat(userModel.getUserId()).isEqualTo("ApnEpv2cRjMfz5ibR68MPScsN3k2");
    }

    @Test
    void test2(){
        List<ComicModel> comicList = scrapService.scrapSearch("oshi", 1).block();
        log.info("comic size: {}", comicList.size());
    }

}

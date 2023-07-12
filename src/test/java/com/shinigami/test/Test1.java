/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.test;

import com.shinigami.api.dto.UserDto;
import com.shinigami.api.model.ComicModel;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.repositories.UserHistoryRepository;
import com.shinigami.api.repositories.UserRepository;
import com.shinigami.api.service.ScrapService;
import com.shinigami.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class Test1 {

    private UserRepository userRepository;
    private UserHistoryRepository userHistoryRepository;

    @BeforeAll
    static void iyhdah(){
        log.info("init dulu yekan");
    }

    @BeforeEach
    void anjim(){
        log.info("alo ngap");

        // memanggil mock() setiap each before test, agar mock dari repo ini fresh
        // tanpa adanya data state yg habis digunakan oleh test lain
        userRepository = Mockito.mock(UserRepository.class);
        userHistoryRepository = Mockito.mock(UserHistoryRepository.class);
    }

    @AfterAll
    static void uwh(){
        log.info("after lul");
    }

    @Test
    void bjrid() throws IOException {

//        Assertions.assertEquals(2 * 3, 6);
        log.info("iyh");
    }

    @Test
    void iyh(){
        String strActual = "DEEP IN YOUR MIND";

//        Assertions.assertEquals("deep in your mind".toUpperCase(), strActual);
        log.info("afa iyh");

    }

//    @Test // ketika menggunakan mockito
//    void iydeh() throws IOException {
//
//        Document document = Jsoup.connect("https://shinigami.id/").get();
//
//        ScrapService scrapService = Mockito.mock(ScrapService.class); // menggunakan Mockito.moc() untuk membuat mock scrapService
//        // dengan menggukana mock() ini nanti class yg di return itu bisa dipakai di Mockito.when()
//
//        List<ComicModel> comicList = new ArrayList<>();
//        comicList.add(new ComicModel("hooh", "apsi", "anjime"));
//
//        // ini berfungsi ketika/when scrapService.scrapHome() dipanggil, maka
//        // return custom data yaitu list yang telah dibuat diatas
//        Mockito.when(scrapService.scrapHome(document)).thenReturn(comicList); // thenReturn ini gitu
//
//        // jadi ketika scrapService.scrapHome() di call, maka yg di return itu data yg diatas
//        // sehingga hasil getTitle() == true yaitu "hooh"
//        Assertions.assertThat(scrapService.scrapHome(document).get(0).getTitle()).isEqualTo("hooh");
//
//        // mockito ini digunakan untuk meng mocking hasil return yg ada pada suatu class
//    }

    @Test // contoh mock 2
    void mock2(){
        // bisa juga mock class List

        List<String> lul = Mockito.mock(List.class);

        Mockito.when(lul.get(0)).thenReturn("shall we look at the moon");

        log.info("msg: {}", lul.get(0));

    }

    @Test // contoh mock 3
    void mock3(){
        // mock repository

        // pemanggilan mock untuk userRepository dan userHistoryRepo sudah dilakukan di method @BeforeEach, agar tidak boilerplate
        UserService userService = new UserService(userRepository, userHistoryRepository);

        Mockito.when(userRepository.findByUserId("asede")).thenReturn(Optional.of(new UserModel("asede", "asedekuntul@gmail.com", false)));

        String NAuserId = "bukan_asede";
        Assertions.assertThat(
                userService.saveUser(new UserDto(NAuserId, "loveris@gmail.com", false, -1, LocalDateTime.now()))
                        .getUserId())
                .isNotEqualTo("asede");

    }

    @Test
    void mock4(){
        UserService userService = new UserService(userRepository, userHistoryRepository);

        // jika userRepo.findByUserId == asede maka return UserModel dengan isPremium = true
        Mockito.when(userRepository.findByUserId("asede")).thenReturn(Optional.of(new UserModel("asede", "", true)));

        String isAsede = "asede";
        UserDto userDto = new UserDto(isAsede, "", false, -1, LocalDateTime.now());

        // lalu ketika saveUser dengan id asede yg mana sama dengan hasil mock findByUserId
        // maka akan return UserModel yg id = asede dan isPremium = true
        // jika tidak/else maka return new UserModel dengan isPremium = false
        UserDto user = userService.saveUser(userDto);

        Assertions.assertThat(user.getUserId()).isEqualTo("asede");
        Assertions.assertThat(user.isPremium()).isTrue(); // karena saveUser() dengan id asede, maka isPremium true


    }


}

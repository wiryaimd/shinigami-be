/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller;

import com.google.gson.Gson;
import com.shinigami.api.controller.UserController;
import com.shinigami.api.dto.PremiumDto;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

// @SpringBootTest // anot untuk test yg lebih general (cek notion note)
// ini termasuk integration test (bukan unit test)
@WebMvcTest(UserController.class) // untuk test lebih yg ada pada web layer (eg controller), dgn param UserController.class untuk menentukan controller mana yg akan di send requestnya
@Slf4j
public class UserControllerTest {

    private static Gson gson;

    @BeforeAll
    static void init(){
        gson = new Gson();
    }

//    @Mock // fungsinya mungking sama dgn Mockito.mock(UserRepository.class)
//    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

//    @Mock // menggunakan @Mock untuk class yg tidak berkaitan dgn spring
    @MockBean // mengunakan @MockBean untuk mendambahkan object mock ini ke Spring application context, sehingga bisa dideteksi oleh spring
    // perlu mock service ini karena UserController memerlukan UserService pada param constructor nya
    // sehingga spring akan memakai mock bean ini untuk inisiasi UserController
    private UserService userService;

    @Test
    void testAllUser() throws Exception {
//        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(LocalDateTime.class, new TimeTypeAdapter()) // penggunaan TypeAdapter untuk LocalDateTime
//                .create();

        List<UserModel> userList = new ArrayList<>();
//        LocalDateTime time = LocalDateTime.of(2023, 1, 1, 1, 1);
        LocalDateTime time = LocalDateTime.now();

        userList.add(new UserModel(1, "id1", "em1", false, time, -1, null, null, null));
        userList.add(new UserModel(2, "id2", "em2", false, time, -1, null, null, null));
        userList.add(new UserModel(3, "id3", "em3", false, time, -1, null, null, null));

//        String jsonUsers = gson.toJson(userList).replaceAll("isPremium", "premium");
//        log.info("users json: {}", jsonUsers);

        Mockito.when(userService.allUser()).thenReturn(userList);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("head1", "head2")
                        .param("auth", "aselolejosjos")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.content().json(jsonUsers))
                .andReturn(); // bisa di return untuk di test menggunakan assert4j/assertions


    }

    @Test
    void testUpdatePremium() throws Exception {

        String auth = Base64.getEncoder().encodeToString("wiryaimd:smvk8ce117da".getBytes(StandardCharsets.UTF_8));

        PremiumDto premiumDto = new PremiumDto(
                "mnyaaw@gmail.com",
                1
        );
        String contentJson = gson.toJson(premiumDto);

        UserModel userModel = new UserModel(5, "u5", premiumDto.getEmail(), true, null, 30, null, null, null);
        Mockito.when(userService.setPremium(premiumDto.getEmail(), premiumDto.getMonth() * 30)).thenReturn(userModel);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + auth)
                        .content(contentJson)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(premiumDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premium").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premiumDay").isNumber())
                .andDo(MockMvcResultHandlers.print()) // agar diprint data/detail testnya (kalo ga isi ini ketika test passed, detail testnya ga di print)
                .andReturn();

        Assertions.assertThat(mvcResult.getResponse().getContentType()).isEqualTo("application/json");

    }

    @Test
    void testCheckEmail() throws Exception {

        String availableEmail = "adnyasutha003@gmail.com";
        String naEmail = "touchme@gmail.com";

        Mockito.when(userService.checkUser(availableEmail)).thenReturn(new UserModel("u1", availableEmail, false));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/check/{email}", availableEmail);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/api/v1/users/check/{email}", naEmail);
        mockMvc.perform(requestBuilder2)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void testExportToCsv() throws Exception {

        String sampleRes = "aselole;josjos;yahhayah;hayuw";
        Mockito.when(userService.exportPremium()).thenReturn(sampleRes.getBytes());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/export/premium")
                        .param("auth", "jaoganmanaluwh")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("Content-Disposition"))
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Aselolul"))
                .andExpect(MockMvcResultMatchers.content().bytes(sampleRes.getBytes()))
                .andDo(MockMvcResultHandlers.print());

    }

}

/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service;

import static org.assertj.core.api.Assertions.*;

import com.shinigami.api.dto.HistoryDto;
import com.shinigami.api.model.UserHistoryModel;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.repositories.UserHistoryRepository;
import com.shinigami.api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserHistoryRepository userHistoryRepository;

    @Test
    void testSaveHistory() throws Throwable {

        String expectedUid = "u1";
        HistoryDto expectedHistoryDto = new HistoryDto(
                "comicUrl1", "chapTitle1", "chapUrl1"
        );

        when(userRepository.findByUserId("123")).thenReturn(Optional.of(new UserModel(expectedUid, "em1@gm.c", false)));

        userService.saveHistory("123", new HistoryDto("", "", ""));

        verify(userRepository, times(1)).findByUserId("123");

//        when(userRepository.findByUserId(expectedUid)).thenReturn(Optional.of(new UserModel(22, expectedUid, "em1@gm.c", false, null, -1, null, null, null)));
//        when(userHistoryRepository.findByChapterUrlAndUserModel_Id(expectedHistoryDto.chapterUrl(), 22)).thenReturn(Optional.of(new UserHistoryModel()));

//        assertThat(userHistoryRepository).isNotNull(); // lul things to do
//        verify()
    }

}

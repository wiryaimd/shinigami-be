/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service;

import com.shinigami.api.dto.HistoryDto;
import com.shinigami.api.dto.UserDto;
import com.shinigami.api.exception.ElementNotFoundException;
import com.shinigami.api.model.ChapterHistoryModel;
import com.shinigami.api.model.ComicHistoryModel;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.repositories.ChapterHistoryRepository;
import com.shinigami.api.repositories.ComicHistoryRepository;
import com.shinigami.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private ComicHistoryRepository comicHistoryRepository;
    private ChapterHistoryRepository chapterHistoryRepository;

    public UserDto saveUser(UserDto userDto){
        UserModel userExist = userRepository.findByUserId(userDto.getUserId()).orElse(null);
        if(userExist != null){
            return new UserDto(
                    userExist.getUserId(),
                    userExist.getEmail(),
                    userExist.isPremium(),
                    userExist.getPremiumDay(),
                    userExist.getPremiumDate()
            );
        }

        UserModel userModel = new UserModel(
                userDto.getUserId(),
                userDto.getEmail(),
                false
        );
        userRepository.save(userModel);

        return new UserDto(
                userDto.getUserId(),
                userDto.getEmail(),
                false,
                -1,
                null
        );
    }

    public UserDto checkPremium(String email){
        UserModel userModel = userRepository.findByEmail(email).orElse(null);
        if (userModel == null){
            return null;
        }

        LocalDateTime now = LocalDateTime.now();

        String time = now.getMonthValue() + "-" + now.getDayOfMonth() + " " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        log.info("user: {} {} checked ---- {}", userModel.getEmail(), userModel.isPremium(), time);

        if (!userModel.isPremium()){
            return new UserDto(userModel.getUserId(), userModel.getEmail(), userModel.isPremium(), userModel.getPremiumDay(), userModel.getPremiumDate());
        }

        boolean isExpired = userModel.getPremiumDate().plusDays(userModel.getPremiumDay()).isBefore(now);

        log.info("isExpired: " + isExpired);
        if (isExpired){
            userModel.setPremium(false);
            userModel.setPremiumDate(null);
            userRepository.save(userModel);
        }

        return new UserDto(userModel.getUserId(), userModel.getEmail(), userModel.isPremium(), userModel.getPremiumDay(), userModel.getPremiumDate());
    }

    public UserModel setPremium(String email, int day) {
        UserModel userModel = userRepository.findByEmail(email).orElse(null);
        if (userModel == null){
            return null;
        }

        userModel.setPremium(true);
        userModel.setPremiumDate(LocalDateTime.now());
        userModel.setPremiumDay(day);

        return userRepository.save(userModel);
    }

    public void saveHistory(String userId, HistoryDto historyDto) throws Throwable {
        UserModel userModel = userRepository.findByUserId(userId).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return new ElementNotFoundException("UserId tidak ditemukan");
            }
        });

        ComicHistoryModel comicHistoryModel = comicHistoryRepository.findByComicUrl(historyDto.comicUrl()).orElse(new ComicHistoryModel(historyDto.comicUrl(), userModel));
        comicHistoryRepository.save(comicHistoryModel);

        ChapterHistoryModel chapterHistoryModel = new ChapterHistoryModel(
                historyDto.chapterTitle(),
                historyDto.chapterUrl(),
                comicHistoryModel,
                userModel
        );
        chapterHistoryRepository.save(chapterHistoryModel);
//
//        for (int i = 0; i < userModel.getComicHistoryList().size(); i++) {
//            ComicHistoryModel comicHistoryModel = userModel.getComicHistoryList().get(i);
//            if (comicHistoryModel.getComicUrl().equalsIgnoreCase(historyDto.comicUrl())){
//                ChapterHistoryModel chapterHistoryModel = new ChapterHistoryModel(
//                        historyDto.chapterTitle(),
//                        historyDto.chapterUrl(),
//                        comicHistoryModel
//                );
//
//                isExist = true;
//                chapterHistoryRepository.save(chapterHistoryModel);
//
//                log.info("history save");
//                break;
//            }
//        }
//
//        if (!isExist){
//            log.info("comic history n exist");
//
//            ComicHistoryModel comicHistoryModel = new ComicHistoryModel(
//                    historyDto.comicUrl(),
//                    userModel
//            );


//        }

        userRepository.save(userModel);
        log.info("save history check");
    }

    public UserModel checkUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public byte[] exportPremium() {
        List<UserModel> userList = userRepository.findAll().stream().filter(new Predicate<UserModel>() {
            @Override
            public boolean test(UserModel userModel) {
                return userModel.isPremium();
            }
        }).toList();

        StringBuilder sb = new StringBuilder();
        sb.append("USER ID;EMAIL;PREMIUM;PREMIUM_DATE;PREMIUM_DAY;\n\n");
        for (int i = 0; i < userList.size(); i++) {
            UserModel userModel = userList.get(i);

            sb.append(userModel.getUserId()).append(";\n")
                    .append(userModel.getEmail()).append(";\n")
                    .append(userModel.isPremium()).append(";\n")
                    .append(userModel.getPremiumDate().toString()).append(";\n")
                    .append(userModel.getPremiumDay()).append(";");
        }

        return sb.toString().getBytes();
    }
}

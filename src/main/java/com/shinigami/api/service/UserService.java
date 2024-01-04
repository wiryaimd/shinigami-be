/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service;

import com.shinigami.api.dto.HistoryComicDto;
import com.shinigami.api.dto.HistoryDto;
import com.shinigami.api.dto.UserDto;
import com.shinigami.api.exception.ElementNotFoundException;
import com.shinigami.api.model.UserHistoryModel;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.repositories.UserHistoryRepository;
import com.shinigami.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private UserRepository userRepository;
    private UserHistoryRepository userHistoryRepository;

    public UserDto saveUser(UserDto userDto){
        UserModel userExist = userRepository.findByUserId(userDto.getUserId()).orElse(null);
        if(userExist != null){
            log.info("wa happ: {}", userExist.getUserId());
            return new UserDto(
                    userExist.getUserId(),
                    userExist.getEmail(),
                    userExist.isPremium(),
                    userExist.getPremiumDay(),
                    userExist.getPremiumDate()
            );
        }else{
            log.info("user null");
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
//            userModel.setPremiumDate(null);
            userModel.setPremiumDay(-1);
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

    public UserModel setPremium(String email, int day, LocalDateTime startTime){
        UserModel userModel = userRepository.findByEmail(email).orElse(null);
        if (userModel == null){
            return null;
        }

        userModel.setPremium(true);
        userModel.setPremiumDate(startTime);
        userModel.setPremiumDay(day);

        return userRepository.save(userModel);
    }

    public void saveHistory(String userId, HistoryDto historyDto) throws Throwable {
        log.info("history save--------");
        UserModel userModel = userRepository.findByUserId(userId).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return new ElementNotFoundException("UserId tidak ditemukan");
            }
        });

        UserHistoryModel userHistoryModel = userHistoryRepository.findByChapterUrlAndUserModel_Id(historyDto.chapterUrl(), userModel.getId())
                .orElse(new UserHistoryModel(
                        historyDto.comicUrl(),
                        historyDto.chapterTitle(),
                        historyDto.chapterUrl(),
                        userModel)
                );

        userHistoryRepository.save(userHistoryModel);
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

            sb.append(userModel.getUserId()).append(";")
                    .append(userModel.getEmail()).append(";")
                    .append(userModel.isPremium()).append(";")
                    .append(userModel.getPremiumDate() == null ? "null" : userModel.getPremiumDate().toString()).append(";")
                    .append(userModel.getPremiumDay()).append(";\n");
        }

        return sb.toString().getBytes();
    }

    public List<UserHistoryModel> historyComic(String userId, HistoryComicDto historyComicDto) {
        return userHistoryRepository.findAllByComicUrlAndUserModel_UserId(historyComicDto.getComicUrl(), userId).orElse(new ArrayList<>());
    }

    public List<UserHistoryModel> historyUser(String userId) {
//        log.info("history----");
        return userHistoryRepository.findAllByUserModel_UserId(userId).orElse(new ArrayList<>());
    }

    public List<UserModel> allUser(){
        return userRepository.findAll();
    }

    public boolean validatePremium(String userId) {
        UserModel userModel = userRepository.findByUserId(userId).orElse(null);

        if(userModel == null){
            return false;
        }

        return userModel.isPremium();
    }
}

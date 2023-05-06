/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service;

import com.shinigami.api.dto.UserDto;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;

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

    public void setPremium(String email, int day) {
        UserModel userModel = userRepository.findByEmail(email).orElse(null);
        if (userModel == null){
            return;
        }

        userModel.setPremium(true);
        userModel.setPremiumDate(LocalDateTime.now());
        userModel.setPremiumDay(day);

        userRepository.save(userModel);
    }
}

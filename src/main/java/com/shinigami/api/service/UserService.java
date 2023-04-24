package com.shinigami.api.service;

import com.shinigami.api.dto.UserDto;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;

    public void saveUser(UserDto userDto){
        if(userRepository.findByUserId(userDto.getUserId()).isPresent()){
            return;
        }

        UserModel userModel = new UserModel(
                userDto.getUserId(),
                userDto.getEmail(),
                userDto.isPremium()
        );
        userRepository.save(userModel);
    }

    public void setPremium(String email) {
        UserModel userModel = userRepository.findByEmail(email).orElseGet(null);
        if (userModel == null){
            return;
        }

        userModel.setPremium(true);
        userRepository.save(userModel);
    }
}

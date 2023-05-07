/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller;

import com.shinigami.api.dto.PremiumDto;
import com.shinigami.api.dto.UserDto;
import com.shinigami.api.model.UserModel;
import com.shinigami.api.repositories.UserRepository;
import com.shinigami.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.saveUser(userDto));
    }

    @GetMapping("/premium/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable String email, @RequestParam int day, @RequestParam("auth") String auth){
        if (!auth.equals("smvk8ce117da")){
            return ResponseEntity.notFound().build();
        }
        userService.setPremium(email, day);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/premium")
    public ResponseEntity<UserModel> updateUser(@RequestBody PremiumDto premiumDto, HttpServletRequest request){
        String basic = request.getHeader("Authorization");
        if (basic == null){
            return ResponseEntity.badRequest().build();
        }

        String encoded = Base64.getEncoder().encodeToString("wiryaimd:smvk8ce117da".getBytes(StandardCharsets.UTF_8));
        if (!basic.substring(6).equals(encoded)){
            log.info("not match");
            return ResponseEntity.status(401).build();
        }

        UserModel userModel = userService.setPremium(premiumDto.getEmail(), premiumDto.getMonth() * 30);

        if (userModel == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userModel);
    }

    @GetMapping("/check/{email}")
    public ResponseEntity<Void> checkEmail(@PathVariable String email){
        UserModel userModel = userService.checkUser(email);
        if (userModel == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/premium/check/{email}")
    public ResponseEntity<UserDto> checkPremium(@PathVariable String email){
        return ResponseEntity.ok(userService.checkPremium(email));
    }

}

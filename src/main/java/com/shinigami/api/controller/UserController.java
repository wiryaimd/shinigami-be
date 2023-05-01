/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller;

import com.shinigami.api.dto.UserDto;
import com.shinigami.api.repositories.UserRepository;
import com.shinigami.api.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("premium/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable String email, @RequestParam int day, @RequestParam("auth") String auth){
        if (!auth.equals("users_wiryaimd_8ce117da")){
            return ResponseEntity.notFound().build();
        }
        userService.setPremium(email, day);
        return ResponseEntity.ok(null);
    }

    @GetMapping("premium/check/{email}")
    public ResponseEntity<Void> checkPremium(@PathVariable String email){
        userService.checkPremium(email);
        return ResponseEntity.ok(null);
    }

}

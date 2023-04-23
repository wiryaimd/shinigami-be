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
    public ResponseEntity<Void> saveUser(@RequestBody UserDto userDto){
        userService.saveUser(userDto);
        return ResponseEntity.ok(null);
    }

    @GetMapping("premium/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable String email, @RequestParam("auth") String auth){
        if (!auth.equals("auth_wiryaimd_8ce117da")){
            return ResponseEntity.notFound().build();
        }
        userService.setPremium(email);
        return ResponseEntity.ok(null);
    }

}

package org.example.controller;

import org.example.dto.UserCredentials;
import org.example.service.UserCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserCacheService userCacheService;

    public UserController(UserCacheService userCacheService) {
        this.userCacheService = userCacheService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> cacheUser(@RequestBody UserCredentials request) {
        userCacheService.cacheUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserCredentials request) {
        boolean authenticated = userCacheService.authenticate(request);
        return ResponseEntity.ok(authenticated ? "YES" : "NO");
    }

    @GetMapping("/users")
    public ResponseEntity<List<String>> getAllUsers() {
        List<String> usernames = userCacheService.getAllUsernames();
        return ResponseEntity.ok(usernames);
    }
}
package org.example.auth.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.auth.user.domain.dto.RegisterRequest;
import org.example.auth.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        userService.signUp(registerRequest);
        return ResponseEntity.ok().build();
    }
}

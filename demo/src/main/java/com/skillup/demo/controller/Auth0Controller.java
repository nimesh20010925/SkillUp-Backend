package com.skillup.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.demo.exception.UserException;
import com.skillup.demo.model.User;
import com.skillup.demo.services.Auth0UserService;

@RestController
@RequestMapping("/api/auth")
public class Auth0Controller {

    @Autowired
    private Auth0UserService auth0UserService;

    @GetMapping("/user")
    public ResponseEntity<User> getUserProfile(@AuthenticationPrincipal Jwt jwt) throws UserException {
        User user = auth0UserService.getCurrentUser();
        return ResponseEntity.ok(user);
    }
}

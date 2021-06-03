package com.codesquad.airbnb.controller;

import com.codesquad.airbnb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private static final String ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String CLIENT_ID = "";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth/google/callback";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/oauth/google/callback")
    public ResponseEntity oauthLogin(String code) {
        userService.oauthLogin(code);
        return new ResponseEntity("로그인 성공", HttpStatus.OK);
    }
}

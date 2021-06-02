package com.codesquad.airbnb.controller;

import com.codesquad.airbnb.oauth.GoogleUser;
import com.codesquad.airbnb.oauth.OAuthToken;
import com.codesquad.airbnb.domain.User;
import com.codesquad.airbnb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/oauth/google/callback")
    public ResponseEntity oauthLogin(String code) {
        ResponseEntity<String> accessTokenResponse = userService.createPost(code);
        OAuthToken oAuthToken = userService.getAccessToken(accessTokenResponse);
        logger.info("Access Token: {}", oAuthToken.getAccessToken());

        ResponseEntity<String> userInfoResponse = userService.createGet(oAuthToken);
        GoogleUser googleUser = userService.getUserInfo(userInfoResponse);
        logger.info("Google User Name: {}", googleUser.getName());
        
        if (!userService.isJoinedUser(googleUser)) {
            User user = googleUser.toUser(oAuthToken.getAccessToken());
            userService.save(user);
        }

        return new ResponseEntity("로그인 성공", HttpStatus.OK);
    }
}

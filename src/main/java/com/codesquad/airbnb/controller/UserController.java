package com.codesquad.airbnb.controller;

import com.codesquad.TokenResponse;
import com.codesquad.airbnb.domain.User;
import com.codesquad.airbnb.oauth.GoogleUser;
import com.codesquad.airbnb.oauth.OAuthToken;
import com.codesquad.airbnb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/oauth/google/callback")
    public ResponseEntity<TokenResponse> oauthLogin(String code) {
        ResponseEntity<String> accessTokenResponse = userService.createPost(code);
        OAuthToken oAuthToken = userService.getAccessToken(accessTokenResponse);
        logger.info("Access Token: {}", oAuthToken.getAccessToken());

        ResponseEntity<String> userInfoResponse = userService.createGet(oAuthToken);
        GoogleUser googleUser = userService.getUserInfo(userInfoResponse);
        logger.info("Google User Name: {}", googleUser.getName());

        User user = googleUser.toUser(oAuthToken.getAccessToken());

        if (!userService.isJoinedUser(googleUser)) {
            userService.save(user);
        }

        String token = userService.createToken(user);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer"));
    }

    @GetMapping("/info")
    public ResponseEntity<User> getUserFromToken(HttpServletRequest request) {
        String name = (String) request.getAttribute("name");
        User user = userService.findByName(name);
        return ResponseEntity.ok().body(user);
    }
}

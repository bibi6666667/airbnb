package com.codesquad.airbnb.controller;

import com.codesquad.airbnb.dto.GoogleUser;
import com.codesquad.airbnb.dto.OauthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final String CLIENT_ID = "";
    private final String CLIENT_SECRET = "";
    private final String REDIRECT_URI = "http://localhost:8080/oauth/google/callback";
    private final String GRANT_TYPE = "authorization_code";

    @GetMapping("/oauth/google/callback")
    public String getAccessToGoogleAPI(@RequestParam String code) {
        logger.info("Authorization code: {}", code);
        ResponseEntity<String> oauthTokenResponse = createPost(code);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(oauthTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        logger.info("Access Token: {}", oauthToken.getAccessToken());

        String accessToken = oauthToken.getAccessToken();

        ResponseEntity<String> userInfo = createGet(accessToken);
        GoogleUser googleUser = null;
        try {
            googleUser = objectMapper.readValue(userInfo.getBody(), GoogleUser.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "redirect:/api/rooms";
    }

    // post 요청해서 access token 받아오기
    private ResponseEntity<String> createPost(String code) {
        // body 만들기
        String url = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", GRANT_TYPE);

        // header 만들기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        // 헤더와 바디 합치기
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        // Post 요청 날리기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
        );
        return response;
    }

    private ResponseEntity<String> createGet(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        return response;
    }
}

package com.codesquad.airbnb.service;

import com.codesquad.airbnb.oauth.GoogleUser;
import com.codesquad.airbnb.oauth.OAuthToken;
import com.codesquad.airbnb.domain.User;
import com.codesquad.airbnb.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth/google/callback";
    private static final String GRANT_TYPE = "authorization_code";

    public UserService(UserRepository userRepository, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> createPost(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", GRANT_TYPE);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        String url = "https://oauth2.googleapis.com/token";
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

    public OAuthToken getAccessToken(ResponseEntity<String> response) {
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oAuthToken;
    }

    public ResponseEntity<String> createGet(OAuthToken oAuthToken) {
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse) {
        GoogleUser googleUser = null;
        try {
            googleUser = objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return googleUser;
    }

    public void save(User user) {
        userRepository.insert(user);
    }

    public boolean isJoinedUser(GoogleUser googleUser) {
        List<User> users = userRepository.findByEmail(googleUser.getEmail());
        logger.info("Joined User: {}", users.stream().findAny());
        return !users.isEmpty();
    }
}

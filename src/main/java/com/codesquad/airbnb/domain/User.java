package com.codesquad.airbnb.domain;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    private Long id;

    private String email;
    private String name;
    private String accessToken;

    public User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User(String email, String name, String accessToken) {
        this.email = email;
        this.name = name;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}

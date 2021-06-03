package com.codesquad.airbnb.exception;

public class UserNotFoundException extends NotFoundException{
    private static final String USER_NOT_FOUND = "해당 유저가 존재하지 않습니다";

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }
}

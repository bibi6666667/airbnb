package com.codesquad.airbnb.exception;

public class TokenEmptyException extends RuntimeException {
    private static final String TOKEN_EMPTY = "token이 비어있습니다.";

    public TokenEmptyException() {
        super(TOKEN_EMPTY);
    }
}

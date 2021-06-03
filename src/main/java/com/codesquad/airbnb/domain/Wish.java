package com.codesquad.airbnb.domain;

import org.springframework.data.annotation.Id;

public class Wish {

    @Id
    private Long id;

    private Long roomId;

    public Wish(Long id, Long roomId) {
        this.id = id;
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}

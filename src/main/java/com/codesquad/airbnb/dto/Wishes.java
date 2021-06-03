package com.codesquad.airbnb.dto;

import java.util.List;

public class Wishes {

    private List<Long> wishList;

    public Wishes(List<Long> wishList) {
        this.wishList = wishList;
    }

    public List<Long> getWishList() {
        return wishList;
    }
}

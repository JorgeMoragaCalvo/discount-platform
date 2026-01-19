package com.mygroup.discountplatform.entities.enums;

import java.util.Arrays;

public enum Genre {

    MALE(1),
    FEMALE(2),
    OTHER(3);

    private final int code;

    Genre(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Genre fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        // Finds genre by code or throws exception
        return Arrays.stream(Genre.values())
                .filter(genre -> genre.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Genre code: " + code));
    }
}

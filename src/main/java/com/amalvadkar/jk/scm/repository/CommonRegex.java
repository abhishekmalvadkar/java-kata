package com.amalvadkar.jk.scm.repository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum CommonRegex {
    ANY_NO_OF_WHITE_SPACE("\\s+");
    private final String value;

    public String value() {
        return value;
    }
}
package com.amalvadkar.jk.scm.repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonSymbol {
    SPACE(" ");
    private final String value;

    public String value() {
        return value;
    }
}
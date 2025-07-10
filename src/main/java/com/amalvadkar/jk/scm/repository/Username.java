package com.amalvadkar.jk.scm.repository;

public record Username(String value) {
    public static Username of(String username){
        return new Username(username);
    }
}

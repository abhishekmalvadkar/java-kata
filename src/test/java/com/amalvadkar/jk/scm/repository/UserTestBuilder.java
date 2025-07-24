package com.amalvadkar.jk.scm.repository;

public class UserTestBuilder {
    private Username username = Username.of("xyz");

    public static UserTestBuilder aUsername(){
        return new UserTestBuilder();
    }

    public UserTestBuilder withUsername(Username username) {
        this.username = username;
        return this;
    }

    public Username build(){
        return Username.of(username.value());
    }
}

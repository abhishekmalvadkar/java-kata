package com.amalvadkar.jk.scm.repository;

public class UsernameTestBuilder {
    private Username username = Username.of("xyz");

    public static UsernameTestBuilder aUsername(){
        return new UsernameTestBuilder();
    }

    public UsernameTestBuilder withUsername(Username username) {
        this.username = username;
        return this;
    }

    public Username build(){
        return Username.of(username.value());
    }
}

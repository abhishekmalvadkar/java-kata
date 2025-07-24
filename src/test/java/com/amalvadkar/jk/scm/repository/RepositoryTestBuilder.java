package com.amalvadkar.jk.scm.repository;

public class RepositoryTestBuilder {

    private String name = "java-kata";
    private String description = "Java Kata Practices";
    private Username username = Username.of("abhishekmalvadkar");

    public static RepositoryTestBuilder aRepository(){
        return new RepositoryTestBuilder();
    }

    public RepositoryTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RepositoryTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public RepositoryTestBuilder withUsername(Username username) {
        this.username = username;
        return this;
    }

    public Repository build(){
        return new Repository(name, description, username);
    }
}

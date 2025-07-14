package com.amalvadkar.jk.scm.repository;

public record Repository(String name, String description, Username username) {
    public Repository withNewName(String newRepoName) {
        return new Repository(newRepoName, description, username);
    }
}

package com.amalvadkar.jk.scm.repository;

public class RepoAlreadyExistsException extends RuntimeException {
    private RepoAlreadyExistsException(String message) {
        super(message);
    }

    public static RepoAlreadyExistsException instance() {
        return new RepoAlreadyExistsException("Repo already exists with given name");
    }
}

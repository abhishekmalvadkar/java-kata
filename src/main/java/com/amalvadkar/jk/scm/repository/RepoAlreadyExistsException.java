package com.amalvadkar.jk.scm.repository;

public class RepoAlreadyExistsException extends RuntimeException {
    public RepoAlreadyExistsException(String message) {
        super(message);
    }
}

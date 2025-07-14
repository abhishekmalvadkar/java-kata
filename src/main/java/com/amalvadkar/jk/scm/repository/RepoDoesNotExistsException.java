package com.amalvadkar.jk.scm.repository;

public class RepoDoesNotExistsException extends RuntimeException {

    public RepoDoesNotExistsException(String message) {
        super(message);
    }
}

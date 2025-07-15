package com.amalvadkar.jk.scm.repository;

public class RepoDoesNotExistsException extends RuntimeException {

    public RepoDoesNotExistsException() {
        super("repo does not exists");
    }
}

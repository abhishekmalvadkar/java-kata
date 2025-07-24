package com.amalvadkar.jk.scm.repository;

public record SearchRepoInput(String searchText, Username username) {
    public SearchRepoInput {
        searchText = searchText.trim();
    }
}
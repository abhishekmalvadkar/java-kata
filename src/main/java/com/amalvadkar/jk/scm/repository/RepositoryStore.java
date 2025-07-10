package com.amalvadkar.jk.scm.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RepositoryStore {

    private static final Map<Username, List<Repository>> USER_NAME_TO_REPOS_MAP
            = new ConcurrentHashMap<>();

    public static void add(Repository newRepo) {
        List<Repository> userRepos = findReposOf(newRepo.username());
        userRepos.add(newRepo);
        USER_NAME_TO_REPOS_MAP.put(newRepo.username(), userRepos);
    }

    private static List<Repository> findReposOf(Username username) {
        return USER_NAME_TO_REPOS_MAP.getOrDefault(username, new ArrayList<>());
    }

    public static long totalReposOf(Username username) {
        List<Repository> userRepos = findReposOf(username);
        return userRepos.size();
    }
}

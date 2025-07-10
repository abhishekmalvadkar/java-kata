package com.amalvadkar.jk.scm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RepositoryStore {

    private static final Map<String, List<Repository>> USER_NAME_TO_REPOS
            = new ConcurrentHashMap<>();

    public static void add(Repository newGithubrepo) {
        List<Repository> repositories = USER_NAME_TO_REPOS.get(newGithubrepo.username());
        if (repositories == null) {
            repositories = new ArrayList<>();
            repositories.add(newGithubrepo);
            USER_NAME_TO_REPOS.put(newGithubrepo.username(), repositories);
        } else {
            repositories.add(newGithubrepo);
        }
    }

    public static long totalRepoOf(String username) {
        List<Repository> repositories = USER_NAME_TO_REPOS.get(username);
        if (repositories == null) {
            return 0;
        }
        return repositories.size();
    }
}

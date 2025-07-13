package com.amalvadkar.jk.scm.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class RepositoryStore {

    private static final Map<Username, List<Repository>> USER_NAME_TO_REPOS_MAP
            = new ConcurrentHashMap<>();

    public static void add(Repository newRepo) {
        List<Repository> userRepos = findReposOf(newRepo.username());
        checkForRepoNameExists(newRepo, userRepos);
        userRepos.add(newRepo);
        USER_NAME_TO_REPOS_MAP.put(newRepo.username(), userRepos);
    }

    public static Optional<Repository> findRepoByNameForGivenUsername(String repoName, Username username) {
        List<Repository> userRepos = findReposOf(username);
        return userRepos.stream()
                .filter(by(repoName))
                .findFirst();
    }

    public static long totalReposOf(Username username) {
        List<Repository> userRepos = findReposOf(username);
        return userRepos.size();
    }

    public static void clear() {
        USER_NAME_TO_REPOS_MAP.clear();
    }

    private static boolean userHasRepoWithSameName(Repository newRepo, List<Repository> userRepos) {
        return userRepos.stream()
                .anyMatch(withNameOf(newRepo));
    }

    private static Predicate<Repository> withNameOf(Repository newRepo) {
        return repo -> repo.name().equals(newRepo.name());
    }

    private static List<Repository> findReposOf(Username username) {
        return USER_NAME_TO_REPOS_MAP.getOrDefault(username, new ArrayList<>());
    }

    private static Predicate<Repository> by(String repoName) {
        return repo -> repoName.equals(repo.name());
    }

    private static void checkForRepoNameExists(Repository newRepo, List<Repository> userRepos) {
        if (userHasRepoWithSameName(newRepo, userRepos)) {
            throw RepoAlreadyExistsException.instance();
        }
    }
}

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

    public static void rename(String oldRepoName, String newRepoName, Username username) {
        Repository oldRepo = findRepoByNameOrThrow(oldRepoName, username);
        List<Repository> userRepos = findReposOf(username);
        userRepos.remove(oldRepo);
        Repository renamedRepo = oldRepo.withNewName(newRepoName);
        userRepos.add(renamedRepo);
        USER_NAME_TO_REPOS_MAP.put(username, userRepos);
    }

    public static void delete(String repoName, Username username) {
        Repository repoToBeDeleted = findRepoByNameOrThrow(repoName, username);
        List<Repository> userRepos = findReposOf(username);
        userRepos.remove(repoToBeDeleted);
        USER_NAME_TO_REPOS_MAP.put(username, userRepos);
    }

    public static long totalReposOf(Username username) {
        List<Repository> userRepos = findReposOf(username);
        return userRepos.size();
    }

    public static void clear() {
        USER_NAME_TO_REPOS_MAP.clear();
    }

    private static Repository findRepoByNameOrThrow(String oldRepoName, Username username) {
        return findRepoByNameForGivenUsername(oldRepoName, username)
                .orElseThrow(RepoDoesNotExistsException::new);
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

    public static List<Repository> search(String searchText, Username username) {
        return List.of();
    }
}

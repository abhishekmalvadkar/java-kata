package com.amalvadkar.jk.scm.repository;

import com.amalvadkar.jk.common.AbstractUT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryStoreTest extends AbstractUT {

    @BeforeEach
    void setUp() {
        RepositoryStore.clear();
    }

    @Test
    void should_create_new_github_repo() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        Username username = Username.of("abhishekmalvadkar");
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);

        long totalRepo = RepositoryStore.totalReposOf(username);

        assertThat(totalRepo).isOne();
    }

    @Test
    void should_return_total_repo_zero_if_user_does_not_have_any_repo() {
        Username username = Username.of("xyz");

        long totalRepo = RepositoryStore.totalReposOf(username);

        assertThat(totalRepo).isZero();
    }

    @Test
    void should_return_repo_by_name_of_user() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        Username username = Username.of("abhishekmalvadkar");
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);

        Optional<Repository> userRepo = RepositoryStore.findRepoByNameFor(username);

        assertThat(userRepo).isPresent();
        Repository actualRepo = userRepo.get();
        assertThatRepo(actualRepo, repository);
    }

    private static void assertThatRepo(Repository actualRepo, Repository expected) {
        assertThat(actualRepo.name()).isEqualTo(expected.username());
        assertThat(actualRepo.description()).isEqualTo(expected.description());
        assertThat(actualRepo.username()).isEqualTo(expected.username());
    }
}

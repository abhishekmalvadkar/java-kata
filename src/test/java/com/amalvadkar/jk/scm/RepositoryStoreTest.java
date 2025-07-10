package com.amalvadkar.jk.scm;

import com.amalvadkar.jk.common.AbstractUT;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryStoreTest extends AbstractUT {

    @Test
    void should_create_new_github_repo() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        String username = "abhishekmalvadkar";
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);
        long totalRepo = RepositoryStore.totalRepoOf(username);
        assertThat(totalRepo).isOne();
    }

    @Test
    void should_return_total_repo_zero_if_user_does_not_have_any_repo() {
        String username = "xyz";
        long totalRepo = RepositoryStore.totalRepoOf(username);
        assertThat(totalRepo).isZero();
    }
}

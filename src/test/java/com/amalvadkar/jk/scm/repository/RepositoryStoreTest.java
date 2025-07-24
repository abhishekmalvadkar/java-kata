package com.amalvadkar.jk.scm.repository;

import com.amalvadkar.jk.common.AbstractUT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.amalvadkar.jk.scm.repository.RepositoryStore.findRepoByNameForGivenUsername;
import static com.amalvadkar.jk.scm.repository.RepositoryStore.totalReposOf;
import static com.amalvadkar.jk.scm.repository.RepositoryTestBuilder.aRepository;
import static com.amalvadkar.jk.scm.repository.UsernameTestBuilder.aUsername;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RepositoryStoreTest extends AbstractUT {

    @BeforeEach
    void setUp() {
        RepositoryStore.clear();
    }

    @Test
    void should_create_new_repo_for_given_user() {
        Repository javaKataRepo = aRepository().build();

        RepositoryStore.add(javaKataRepo);

        long totalRepo = totalReposOf(javaKataRepo.username());
        assertThat(totalRepo).isOne();
    }

    @Test
    void should_return_total_repo_zero_if_user_does_not_have_any_repo() {
        Username username = aUsername().build();

        long totalRepo = totalReposOf(username);

        assertThat(totalRepo).isZero();
    }

    @Test
    void should_return_repo_by_name_of_user() {
        Repository javaKataRepo = aRepository().build();
        RepositoryStore.add(javaKataRepo);

        Optional<Repository> userRepo = findRepoByNameForGivenUsername(javaKataRepo.name(), javaKataRepo.username());

        assertThat(userRepo).isPresent();
        Repository actualRepo = userRepo.get();
        assertThatRepo(actualRepo, javaKataRepo);
    }

    @Test
    void should_return_empty_if_asked_repo_username_does_not_have() {
        Repository javaKataRepo = aRepository().build();

        Optional<Repository> userRepo = findRepoByNameForGivenUsername(javaKataRepo.name(), javaKataRepo.username());

        assertThat(userRepo).isEmpty();
    }

    @Test
    void should_throw_exception_with_message_repo_already_exists_if_user_try_to_create_repo_with_same_name() {
        Repository javaKataRepo = aRepository().build();
        RepositoryStore.add(javaKataRepo);

        assertThatThrownBy(() -> RepositoryStore.add(javaKataRepo))
                .isInstanceOf(RepoAlreadyExistsException.class)
                .hasMessage("Repo already exists with given name");

        assertThat(totalReposOf(javaKataRepo.username())).isOne();

    }

    @Test
    void should_rename_existing_repo() {
        Repository javaKataRepo = aRepository().build();
        RepositoryStore.add(javaKataRepo);

        String newRepoName = "java-kata-practices";
        RepositoryStore.rename(javaKataRepo.name(), newRepoName, javaKataRepo.username());

        assertThat(findRepoByNameForGivenUsername(javaKataRepo.name(), javaKataRepo.username())).isEmpty();
        Optional<Repository> renamedRepoOpt = findRepoByNameForGivenUsername(newRepoName, javaKataRepo.username());
        assertThat(renamedRepoOpt).isPresent();
        assertThat(totalReposOf(javaKataRepo.username())).isOne();

    }

    @Test
    void should_throw_exception_that_repo_does_not_exists_if_passed_invalid_existing_repo_name_during_rename_existing_repo() {
        Repository javaKataRepo = aRepository().build();
        RepositoryStore.add(javaKataRepo);

        String newRepoName = "java-kata-practices";
        String existingRepoName = "java-kata-list";
        assertThatThrownBy(() -> RepositoryStore.rename(existingRepoName, newRepoName, javaKataRepo.username()))
                .isInstanceOf(RepoDoesNotExistsException.class)
                .hasMessage("repo does not exists");

    }

    @Test
    void should_delete_repo() {
        Repository javaKataRepo = aRepository().build();
        RepositoryStore.add(javaKataRepo);
        assertThat(totalReposOf(javaKataRepo.username())).isOne();

        RepositoryStore.delete(javaKataRepo.name(), javaKataRepo.username());

        assertThat(totalReposOf(javaKataRepo.username())).isZero();
    }

    @Test
    void should_return_list_of_repo_if_repo_name_has_that_user_entered_search_text() {
        Repository javaKataRepo = aRepository().build();
        RepositoryStore.add(javaKataRepo);

        Repository sqlKataRepo = aRepository()
                .withName("sql-kata")
                .withDescription("SQL Kata Practices")
                .build();
        RepositoryStore.add(sqlKataRepo);

        Repository springKataRepo = aRepository()
                .withName("spring-kata")
                .withDescription("Spring Kata Practices")
                .build();
        RepositoryStore.add(springKataRepo);

        Repository springBootKataRepo = aRepository()
                .withName("spring-boot-kata")
                .withDescription("Spring Boot Kata Practices")
                .build();
        RepositoryStore.add(springBootKataRepo);

        assertThat(totalReposOf(javaKataRepo.username())).isEqualTo(4);

        String searchText = "spring";
        List<Repository> searchedRepos = RepositoryStore.search(new SearchRepoInput(searchText, javaKataRepo.username()));

        assertThat(searchedRepos).hasSize(2);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "spring-boot-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Spring Kata Practices", "Spring Boot Kata Practices");

    }

    @Test
    void should_return_list_of_repo_if_repo_description_has_that_user_entered_search_text() {
        Repository javaKataRepo = aRepository()
                .withDescription("Java Kata Practices for spring developer as well")
                .build();
        RepositoryStore.add(javaKataRepo);

        Repository sqlKataRepo = aRepository()
                .withName("sql-kata")
                .withDescription("SQL Kata Practices")
                .build();
        RepositoryStore.add(sqlKataRepo);

        Repository springKataRepo = aRepository()
                .withName("spring-kata")
                .withDescription("Spring Kata Practices")
                .build();
        RepositoryStore.add(springKataRepo);

        Repository springBootKataRepo = aRepository()
                .withName("spring-boot-kata")
                .withDescription("Spring Boot Kata Practices")
                .build();
        RepositoryStore.add(springBootKataRepo);

        assertThat(totalReposOf(javaKataRepo.username())).isEqualTo(4);

        String searchText = "spring";
        List<Repository> searchedRepos = RepositoryStore.search(new SearchRepoInput(searchText, javaKataRepo.username()));

        assertThat(searchedRepos).hasSize(3);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "spring-boot-kata", "java-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Spring Kata Practices", "Spring Boot Kata Practices",
                        "Java Kata Practices for spring developer as well");

    }

    @Test
    void should_return_list_of_repo_which_has_words_from_search_text_if_searched_text_has_multiple_words_space_separated() {
        Repository javaKataRepo = aRepository()
                .withDescription("Java Kata Practices for spring developer as well")
                .build();
        RepositoryStore.add(javaKataRepo);

        Repository sqlKataRepo = aRepository()
                .withName("sql-kata")
                .withDescription("SQL Kata Practices")
                .build();
        RepositoryStore.add(sqlKataRepo);

        Repository springKataRepo = aRepository()
                .withName("spring-kata")
                .withDescription("Best Kata Practices")
                .build();
        RepositoryStore.add(springKataRepo);

        Repository bootKataRepo = aRepository()
                .withName("boot-kata")
                .withDescription("Nice Kata Practices")
                .build();
        RepositoryStore.add(bootKataRepo);

        assertThat(totalReposOf(javaKataRepo.username())).isEqualTo(4);

        String searchText = "spring boot";
        List<Repository> searchedRepos = RepositoryStore.search(new SearchRepoInput(searchText, javaKataRepo.username()));

        assertThat(searchedRepos).hasSize(3);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "boot-kata", "java-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Nice Kata Practices", "Best Kata Practices",
                        "Java Kata Practices for spring developer as well");

    }

    @Test
    void should_return_list_of_repo_which_has_words_from_search_text_if_searched_text_has_multiple_words__with_many_space_separated() {
        Repository javaKataRepo = aRepository()
                .withDescription("Java Kata Practices for spring developer as well")
                .build();
        RepositoryStore.add(javaKataRepo);

        Repository sqlKataRepo = aRepository()
                .withName("sql-kata")
                .withDescription("SQL Kata Practices")
                .build();
        RepositoryStore.add(sqlKataRepo);

        Repository springKataRepo = aRepository()
                .withName("spring-kata")
                .withDescription("Best Kata Practices")
                .build();
        RepositoryStore.add(springKataRepo);

        Repository bootKataRepo = aRepository()
                .withName("boot-kata")
                .withDescription("Nice Kata Practices")
                .build();
        RepositoryStore.add(bootKataRepo);

        assertThat(totalReposOf(javaKataRepo.username())).isEqualTo(4);

        String searchText = "spring                       boot";
        List<Repository> searchedRepos = RepositoryStore.search(new SearchRepoInput(searchText, javaKataRepo.username()));

        assertThat(searchedRepos).hasSize(3);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "boot-kata", "java-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Nice Kata Practices", "Best Kata Practices",
                        "Java Kata Practices for spring developer as well");

    }

    @Test
    void should_return_list_of_repo_which_has_words_from_search_text_if_searched_text_has_multiple_words__with_many_space_separated_in_between_and_also_many_spaces_in_start_and_end_of_search_text() {
        Repository javaKataRepo = aRepository()
                .withDescription("Java Kata Practices for spring developer as well")
                .build();
        RepositoryStore.add(javaKataRepo);

        Repository sqlKataRepo = aRepository()
                .withName("sql-kata")
                .withDescription("SQL Kata Practices")
                .build();
        RepositoryStore.add(sqlKataRepo);

        Repository springKataRepo = aRepository()
                .withName("spring-kata")
                .withDescription("Best Kata Practices")
                .build();
        RepositoryStore.add(springKataRepo);

        Repository bootKataRepo = aRepository()
                .withName("boot-kata")
                .withDescription("Nice Kata Practices")
                .build();
        RepositoryStore.add(bootKataRepo);

        assertThat(totalReposOf(javaKataRepo.username())).isEqualTo(4);

        String searchText = "                spring                       boot               ";
        List<Repository> searchedRepos = RepositoryStore.search(new SearchRepoInput(searchText, javaKataRepo.username()));

        assertThat(searchedRepos).hasSize(3);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "boot-kata", "java-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Nice Kata Practices", "Best Kata Practices",
                        "Java Kata Practices for spring developer as well");

    }

    private static void assertThatRepo(Repository actualRepo, Repository expectedRepo) {
        assertThat(actualRepo.name()).isEqualTo(expectedRepo.name());
        assertThat(actualRepo.description()).isEqualTo(expectedRepo.description());
        assertThat(actualRepo.username()).isEqualTo(expectedRepo.username());
    }
}

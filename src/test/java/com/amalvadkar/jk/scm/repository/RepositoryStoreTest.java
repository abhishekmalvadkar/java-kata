package com.amalvadkar.jk.scm.repository;

import com.amalvadkar.jk.common.AbstractUT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.amalvadkar.jk.scm.repository.RepositoryStore.findRepoByNameForGivenUsername;
import static com.amalvadkar.jk.scm.repository.RepositoryStore.totalReposOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RepositoryStoreTest extends AbstractUT {

    @BeforeEach
    void setUp() {
        RepositoryStore.clear();
    }

    @Test
    void should_create_new_repo_for_given_user() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        Username username = Username.of("abhishekmalvadkar");
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);

        long totalRepo = totalReposOf(username);

        assertThat(totalRepo).isOne();
    }

    @Test
    void should_return_total_repo_zero_if_user_does_not_have_any_repo() {
        Username username = Username.of("xyz");

        long totalRepo = totalReposOf(username);

        assertThat(totalRepo).isZero();
    }

    @Test
    void should_return_repo_by_name_of_user() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        Username username = Username.of("abhishekmalvadkar");
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);

        Optional<Repository> userRepo = findRepoByNameForGivenUsername(repoName, username);

        assertThat(userRepo).isPresent();
        Repository actualRepo = userRepo.get();
        assertThatRepo(actualRepo, repository);
    }

    @Test
    void should_return_empty_if_asked_repo_username_does_not_have() {
        String repoName = "java-kata";
        Username username = Username.of("abhishekmalvadkar");

        Optional<Repository> userRepo = findRepoByNameForGivenUsername(repoName, username);

        assertThat(userRepo).isEmpty();
    }

    @Test
    void should_throw_exception_with_message_repo_already_exists_if_user_try_to_create_repo_with_same_name() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        Username username = Username.of("abhishekmalvadkar");
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);

        String anotherRepoName = "java-kata";
        String anotherRepoDescription = "Java Kata Practices Another";
        Repository anotherRepo = new Repository(anotherRepoName, anotherRepoDescription, username);

        assertThatThrownBy(() -> RepositoryStore.add(anotherRepo))
                .isInstanceOf(RepoAlreadyExistsException.class)
                .hasMessage("Repo already exists with given name");

        assertThat(totalReposOf(username)).isOne();

    }

    @Test
    void should_rename_existing_repo() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        Username username = Username.of("abhishekmalvadkar");
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);

        String newRepoName = "java-kata-practices";
        RepositoryStore.rename(repoName, newRepoName, username);

        assertThat(findRepoByNameForGivenUsername(repoName, username)).isEmpty();
        Optional<Repository> renamedRepoOpt = findRepoByNameForGivenUsername(newRepoName, username);
        assertThat(renamedRepoOpt).isPresent();
        assertThat(totalReposOf(username)).isOne();

    }

    @Test
    void should_throw_exception_that_repo_does_not_exists_if_passed_invalid_existing_repo_name_during_rename_existing_repo() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        Username username = Username.of("abhishekmalvadkar");
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);

        String newRepoName = "java-kata-practices";
        String existingRepoName = "java-kata-list";
        assertThatThrownBy(() -> RepositoryStore.rename(existingRepoName, newRepoName, username))
                .isInstanceOf(RepoDoesNotExistsException.class)
                .hasMessage("repo does not exists");

    }

    @Test
    void should_delete_repo() {
        String repoName = "java-kata";
        String repoDescription = "Java Kata Practices";
        Username username = Username.of("abhishekmalvadkar");
        Repository repository = new Repository(repoName, repoDescription, username);
        RepositoryStore.add(repository);
        assertThat(totalReposOf(username)).isOne();

        RepositoryStore.delete(repoName, username);

        assertThat(totalReposOf(username)).isZero();
    }

    @Test
    void should_return_list_of_repo_if_repo_name_has_that_user_entered_search_text() {

        Username username = Username.of("abhishekmalvadkar");
        Repository repoOne = createRepo("java-kata",
                "Java Kata Practices",
                username);
        RepositoryStore.add(repoOne);

        Repository repoTwo = createRepo("sql-kata",
                "SQL Kata Practices",
                username);
        RepositoryStore.add(repoTwo);

        Repository repoThree = createRepo("spring-kata",
                "Spring Kata Practices",
                username);
        RepositoryStore.add(repoThree);

        Repository repoFour = createRepo("spring-boot--kata",
                "Spring Boot Kata Practices",
                username);
        RepositoryStore.add(repoFour);

        assertThat(totalReposOf(username)).isEqualTo(4);

        String searchText = "spring";
        List<Repository> searchedRepos = RepositoryStore.search(searchText, username);

        assertThat(searchedRepos).hasSize(2);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "spring-boot--kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Spring Kata Practices", "Spring Boot Kata Practices");

    }

    @Test
    void should_return_list_of_repo_if_repo_description_has_that_user_entered_search_text() {

        Username username = Username.of("abhishekmalvadkar");
        Repository repoOne = createRepo("java-kata",
                "Java Kata Practices for spring developer as well",
                username);
        RepositoryStore.add(repoOne);

        Repository repoTwo = createRepo("sql-kata",
                "SQL Kata Practices",
                username);
        RepositoryStore.add(repoTwo);

        Repository repoThree = createRepo("spring-kata",
                "Spring Kata Practices",
                username);
        RepositoryStore.add(repoThree);

        Repository repoFour = createRepo("spring-boot--kata",
                "Spring Boot Kata Practices",
                username);
        RepositoryStore.add(repoFour);

        assertThat(totalReposOf(username)).isEqualTo(4);

        String searchText = "spring";
        List<Repository> searchedRepos = RepositoryStore.search(searchText, username);

        assertThat(searchedRepos).hasSize(3);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "spring-boot--kata", "java-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Spring Kata Practices", "Spring Boot Kata Practices",
                        "Java Kata Practices for spring developer as well");

    }

    @Test
    void should_return_list_of_repo_which_has_words_from_search_text_if_searched_text_has_multiple_words_space_separated() {

        Username username = Username.of("abhishekmalvadkar");
        Repository repoOne = createRepo("java-kata",
                "Java Kata Practices for spring developer as well",
                username);
        RepositoryStore.add(repoOne);

        Repository repoTwo = createRepo("sql-kata",
                "SQL Kata Practices",
                username);
        RepositoryStore.add(repoTwo);

        Repository repoThree = createRepo("spring-kata",
                "Best Kata Practices",
                username);
        RepositoryStore.add(repoThree);

        Repository repoFour = createRepo("boot--kata",
                "Nice Kata Practices",
                username);
        RepositoryStore.add(repoFour);

        assertThat(totalReposOf(username)).isEqualTo(4);

        String searchText = "spring boot";
        List<Repository> searchedRepos = RepositoryStore.search(searchText, username);

        assertThat(searchedRepos).hasSize(3);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "boot--kata", "java-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Nice Kata Practices", "Best Kata Practices",
                        "Java Kata Practices for spring developer as well");

    }

    @Test
    void should_return_list_of_repo_which_has_words_from_search_text_if_searched_text_has_multiple_words__with_many_space_separated() {

        Username username = Username.of("abhishekmalvadkar");
        Repository repoOne = createRepo("java-kata",
                "Java Kata Practices for spring developer as well",
                username);
        RepositoryStore.add(repoOne);

        Repository repoTwo = createRepo("sql-kata",
                "SQL Kata Practices",
                username);
        RepositoryStore.add(repoTwo);

        Repository repoThree = createRepo("spring-kata",
                "Best Kata Practices",
                username);
        RepositoryStore.add(repoThree);

        Repository repoFour = createRepo("boot--kata",
                "Nice Kata Practices",
                username);
        RepositoryStore.add(repoFour);

        assertThat(totalReposOf(username)).isEqualTo(4);

        String searchText = "spring                       boot";
        List<Repository> searchedRepos = RepositoryStore.search(searchText, username);

        assertThat(searchedRepos).hasSize(3);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "boot--kata", "java-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Nice Kata Practices", "Best Kata Practices",
                        "Java Kata Practices for spring developer as well");

    }

    @Test
    void should_return_list_of_repo_which_has_words_from_search_text_if_searched_text_has_multiple_words__with_many_space_separated_in_between_and_also_many_spaces_in_start_and_end_of_search_text() {

        Username username = Username.of("abhishekmalvadkar");
        Repository repoOne = createRepo("java-kata",
                "Java Kata Practices for spring developer as well",
                username);
        RepositoryStore.add(repoOne);

        Repository repoTwo = createRepo("sql-kata",
                "SQL Kata Practices",
                username);
        RepositoryStore.add(repoTwo);

        Repository repoThree = createRepo("spring-kata",
                "Best Kata Practices",
                username);
        RepositoryStore.add(repoThree);

        Repository repoFour = createRepo("boot--kata",
                "Nice Kata Practices",
                username);
        RepositoryStore.add(repoFour);

        assertThat(totalReposOf(username)).isEqualTo(4);

        String searchText = "                spring                       boot               ";
        List<Repository> searchedRepos = RepositoryStore.search(searchText, username);

        assertThat(searchedRepos).hasSize(3);
        assertThat(searchedRepos).extracting("name")
                .containsExactlyInAnyOrder("spring-kata", "boot--kata", "java-kata");
        assertThat(searchedRepos).extracting("description")
                .containsExactlyInAnyOrder("Nice Kata Practices", "Best Kata Practices",
                        "Java Kata Practices for spring developer as well");

    }

    private static Repository createRepo(String repoName, String repoDescription, Username username) {
        return new Repository(repoName, repoDescription, username);
    }

    private static void assertThatRepo(Repository actualRepo, Repository expectedRepo) {
        assertThat(actualRepo.name()).isEqualTo(expectedRepo.name());
        assertThat(actualRepo.description()).isEqualTo(expectedRepo.description());
        assertThat(actualRepo.username()).isEqualTo(expectedRepo.username());
    }
}

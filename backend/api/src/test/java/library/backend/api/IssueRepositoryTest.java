package library.backend.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import library.backend.api.models.Issue;
import library.backend.api.models.User;
import library.backend.api.repositories.IssueRepository;
import library.backend.api.repositories.UserRepository;

@DataJpaTest
@ActiveProfiles(profiles = { "test" })
public class IssueRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private TestEntityManager entityManager;

    User user1;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .email("test@example.com")
                .password("1234")
                .phoneNo("8178610509")
                .name("test")
                .role("USER")
                .build();

        Issue issue1 = Issue.builder()
                .issueDate(LocalDate.of(2024, 2, 2))
                .issuePeriod(14)
                .user(user1)
                .build();

        Issue issue2 = Issue.builder()
                .issueDate(LocalDate.of(2024, 2, 2))
                .issuePeriod(14)
                .user(user1)
                .build();

        Issue issue3 = Issue.builder()
                .issueDate(LocalDate.of(2024, 1, 2))
                .issuePeriod(14)
                .user(user1)
                .build();

        userRepository.save(user1);

        issueRepository.save(issue1);
        issueRepository.save(issue2);
        issueRepository.save(issue3);

        entityManager.clear();

    }

    @Test
    void IssuesWithAssociatedUserAreReturned() {
        List<Issue> issues = issueRepository.findByUserId(user1.getId());
        assertNotNull(issues, () -> "Returned issues shouldn't be null.");
        assertEquals(3, issues.size());
    }

    @Test
    void IncompleteIssuesHaveNullReturnedDates() {
        List<Issue> issues = issueRepository.findIssuesByUserIdWhereReturnDateIsNull(user1.getId());
        assertNotNull(issues, () -> "Returned issues shouldn't be null.");
        issues.stream()
                .map(Issue::getReturnDate)
                .forEach(Assertions::assertNull);
    }

    @Test
    void CompleteIssuesHaveNonNullReturnedDate() {
        List<Issue> issues = issueRepository.findIssuesByUserIdWhereReturnDateIsNotNull(user1.getId());
        assertNotNull(issues, () -> "Returned issues shouldn't be null.");
        issues.stream()
                .map(Issue::getReturnDate)
                .forEach(Assertions::assertNotNull);
    }
}

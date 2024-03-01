package library.backend.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import library.backend.api.models.Issue;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    // Query for listing all book issues by a user
    List<Issue> findByUserId(Long userId);

    // Query for listing all book issues by a user where book has been returned
    @Query("SELECT i from Issue i WHERE i.returnDate IS NOT NULL and i.user.id =?1")
    List<Issue> findIssuesByUserIdWhereReturnDateIsNotNull(Long userId);

    // Query for listing all book issues by a user where book hasn't been returned
    @Query("SELECT i from Issue i WHERE i.returnDate IS NULL and i.user.id = ?1")
    List<Issue> findIssuesByUserIdWhereReturnDateIsNull(Long userId);
}

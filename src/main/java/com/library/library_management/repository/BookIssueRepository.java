
package com.library.library_management.repository;

import com.library.library_management.entity.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookIssueRepository
        extends JpaRepository<BookIssue, Long> {

    List<BookIssue> findByStudentStudentId(Long studentId);

    long countByStudentStudentIdAndStatus(
        Long studentId, String status
    );

    List<BookIssue> findByStatusAndDueDateBefore(
        String status, LocalDate date
    );

    boolean existsByBookBookIdAndStatus(
        Long bookId, String status
    );
}
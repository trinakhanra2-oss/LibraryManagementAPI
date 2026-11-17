
package com.library.library_management.service;

import com.library.library_management.entity.Book;
import com.library.library_management.entity.Student;
import com.library.library_management.entity.BookIssue;

import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.StudentRepository;
import com.library.library_management.repository.BookIssueRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookIssueService {

    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final BookIssueRepository issueRepository;

    public BookIssueService(BookRepository bookRepository,
                            StudentRepository studentRepository,
                            BookIssueRepository issueRepository) {
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.issueRepository = issueRepository;
    }

    @Transactional
    public BookIssue issueBook(Long studentId, Long bookId) {

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Student not found")
            );

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Book not found")
            );

        long activeIssues =
            issueRepository.countByStudentStudentIdAndStatus(
                studentId, "ISSUED"
            );

        if (activeIssues >= 3) {
            throw new IllegalArgumentException(
                "A student can issue maximum 3 active books"
            );
        }

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException(
                "No available copies"
            );
        }

        book.setAvailableCopies(
            book.getAvailableCopies() - 1
        );

        bookRepository.save(book);

        BookIssue issue = new BookIssue();

        issue.setStudent(student);
        issue.setBook(book);
        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(LocalDate.now().plusDays(14));
        issue.setReturnDate(null);
        issue.setStatus("ISSUED");

        return issueRepository.save(issue);
    }

    public List<BookIssue> getAllIssues() {
        return issueRepository.findAll();
    }

    public List<BookIssue> getIssuesByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException(
                "Student not found"
            );
        }

        return issueRepository.findByStudentStudentId(studentId);
    }

    @Transactional
    public BookIssue returnBook(Long issueId) {

        BookIssue issue = issueRepository.findById(issueId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Issue not found")
            );

        if ("RETURNED".equals(issue.getStatus())) {
            throw new IllegalArgumentException(
                "Book has already been returned"
            );
        }

        issue.setReturnDate(LocalDate.now());
        issue.setStatus("RETURNED");

        Book book = issue.getBook();

        book.setAvailableCopies(
            book.getAvailableCopies() + 1
        );

        bookRepository.save(book);

        return issueRepository.save(issue);
    }

    public List<BookIssue> getOverdueBooks() {

        return issueRepository.findByStatusAndDueDateBefore(
            "ISSUED", LocalDate.now()
        );
    }
}
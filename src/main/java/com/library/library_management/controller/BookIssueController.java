
package com.library.library_management.controller;

import com.library.library_management.entity.BookIssue;
import com.library.library_management.service.BookIssueService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class BookIssueController {

    private final BookIssueService issueService;

    public BookIssueController(BookIssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping("/{studentId}/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookIssue issueBook(
            @PathVariable Long studentId,
            @PathVariable Long bookId) {
        return issueService.issueBook(studentId, bookId);
    }

    @GetMapping
    public List<BookIssue> getAllIssues() {
        return issueService.getAllIssues();
    }

    @GetMapping("/student/{studentId}")
    public List<BookIssue> getIssuesByStudent(
            @PathVariable Long studentId) {
        return issueService.getIssuesByStudent(studentId);
    }

    @PutMapping("/return/{issueId}")
    public BookIssue returnBook(
            @PathVariable Long issueId) {
        return issueService.returnBook(issueId);
    }

    @GetMapping("/overdue")
    public List<BookIssue> getOverdueBooks() {
        return issueService.getOverdueBooks();
    }
}

package com.library.library_management.service;

import com.library.library_management.entity.Book;
import com.library.library_management.exception.DuplicateResourceException;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BookIssueRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookIssueRepository issueRepository;

    public BookService(BookRepository bookRepository,
                       BookIssueRepository issueRepository) {
        this.bookRepository = bookRepository;
        this.issueRepository = issueRepository;
    }

    public Book addBook(Book book) {

        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new DuplicateResourceException(
                "ISBN already exists"
            );
        }

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new IllegalArgumentException(
                "Available copies cannot exceed total copies"
            );
        }

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Book not found: " + id)
            );
    }

    @Transactional
    public Book updateBook(Long id, Book updatedBook) {

        Book book = getBookById(id);

        bookRepository.findByIsbn(updatedBook.getIsbn())
            .filter(existing -> !existing.getBookId().equals(id))
            .ifPresent(existing -> {
                throw new DuplicateResourceException(
                    "ISBN already exists"
                );
            });

        int issuedCopies =
            book.getTotalCopies() - book.getAvailableCopies();

        if (updatedBook.getTotalCopies() < issuedCopies) {
            throw new IllegalArgumentException(
                "Total copies cannot be less than issued copies"
            );
        }

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setCategory(updatedBook.getCategory());
        book.setTotalCopies(updatedBook.getTotalCopies());
        book.setAvailableCopies(
            updatedBook.getTotalCopies() - issuedCopies
        );

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {

        Book book = getBookById(id);

        if (issueRepository.existsByBookBookIdAndStatus(
                id, "ISSUED")) {
            throw new IllegalArgumentException(
                "Cannot delete a book with an active issue"
            );
        }

        bookRepository.delete(book);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryIgnoreCase(category);
    }
}
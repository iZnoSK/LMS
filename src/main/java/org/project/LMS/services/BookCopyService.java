package org.project.LMS.services;

import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.model.Book;
import org.project.LMS.model.BookCopy;
import org.project.LMS.repositories.BookCopyRepository;
import org.project.LMS.repositories.BookRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCopyService {
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public BookCopyService(BookRepository bookRepository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    public List<BookCopy> getCopiesForBook(Long bookId) {
        Book book = getBookOrThrow(bookId);
        return bookCopyRepository.findByBook(book);
    }

    private Book getBookOrThrow(Long bookId) {
        return this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + bookId + " not found."));
    }

    private BookCopy getBookCopyOrThrow(Long copyId) {
        return this.bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ResourceNotFoundException("Book copy with id " + copyId + " not found."));
    }

    public BookCopy addCopy(Long bookId) {
        Book book = getBookOrThrow(bookId);
        BookCopy copy = new BookCopy(null, book, true);
        return this.bookCopyRepository.save(copy);
    }

    public BookCopy updateAvailability(Long bookId, Long copyId, Boolean available) {
        Book book = getBookOrThrow(bookId);
        BookCopy copy = getBookCopyOrThrow(copyId);

        if (!copy.getBook().getId().equals(book.getId())) {
            throw new IllegalArgumentException("This copy with id " + copyId +
                    " does not belong to the book with id " + bookId + ".");
        }

        copy.setAvailable(available);
        return this.bookCopyRepository.save(copy);
    }
}

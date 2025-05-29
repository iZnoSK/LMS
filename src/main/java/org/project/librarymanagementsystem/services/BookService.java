package org.project.librarymanagementsystem.services;

import org.project.librarymanagementsystem.dto.book.BookUpdateRequest;
import org.project.librarymanagementsystem.exceptions.ResourceNotFoundException;
import org.project.librarymanagementsystem.model.Book;
import org.project.librarymanagementsystem.model.BookCopy;
import org.project.librarymanagementsystem.repositories.BookCopyRepository;
import org.project.librarymanagementsystem.repositories.BookRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public BookService(BookRepository bookRepository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    //public List<Book> getAllBooks() {
    //    return this.bookRepository.findAll();
    //}

    public Page<Book> getAllBooks(Pageable pageable) {
        return this.bookRepository.findAll(pageable);
    }

    public Book addBook(Book book) {
        return this.bookRepository.save(book);
    }

    public Book getBookById(Long bookId) {
        return getBookOrThrow(bookId);
    }

    public List<BookCopy> getBookCopies(Book book) {
        return this.bookCopyRepository.findByBook(book);
    }

    public Book updateBook(BookUpdateRequest bookReq, long bookId) {
        Book book = getBookOrThrow(bookId);

        if (bookReq.title() != null) book.setTitle(bookReq.title());
        if (bookReq.author() != null) book.setAuthor(bookReq.author());
        if (bookReq.isbn() != null) book.setIsbn(bookReq.isbn());
        if (bookReq.publishedYear() != null) book.setPublishedYear(bookReq.publishedYear());

        return this.bookRepository.save(book);
    }

    public void deleteBook(Long bookId) {
        Book book = getBookOrThrow(bookId);
        this.bookRepository.delete(book);
    }

    private Book getBookOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + bookId + " not found."));
    }
}
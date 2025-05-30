package org.project.LMS.serviceTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.model.Book;
import org.project.LMS.model.BookCopy;
import org.project.LMS.repositories.BookCopyRepository;
import org.project.LMS.repositories.BookRepository;
import org.project.LMS.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteBookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookCopyRepository bookCopyRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("This method deletes book when it exists")
    void deleteBook_whenExists() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Test Book", "Author", "978-0134685991", 2023);
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        bookService.deleteBook(bookId);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    @DisplayName("Should delete book and its copies when book exists")
    public void deleteBook_WhenBookExists_ShouldDeleteBookAndCopies() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Test Book", "Author", "978-0134685991", 2023);

        BookCopy copy1 = new BookCopy(1L, book,true);
        BookCopy copy2 = new BookCopy(2L, book,false);
        List<BookCopy> copies = List.of(copy1, copy2);

        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(bookCopyRepository.findByBook(book)).willReturn(copies);

        bookService.deleteBook(bookId);

        verify(bookCopyRepository).deleteAll(copies);
        verify(bookRepository).delete(book);
    }

    @Test
    @DisplayName("this method throws ResourceNotFoundException when book does not exist")
    void dontDeleteBook_whenBookNotFound() {
        Long bookId = 99L;
        given(bookRepository.findById(bookId)).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(bookId));
        verify(bookRepository, never()).delete(any(Book.class));
    }
}

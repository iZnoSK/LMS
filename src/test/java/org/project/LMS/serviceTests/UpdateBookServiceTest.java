package org.project.LMS.serviceTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.LMS.dto.book.BookUpdateRequest;
import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.model.Book;
import org.project.LMS.repositories.BookRepository;
import org.project.LMS.services.BookService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UpdateBookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("Should update book fields and save it")
    public void updateBook_whenValidRequest() {
        Long bookId = 1L;
        Book existingBook = new Book(bookId, "Spring", "Kane Wayc", "978-0134685991", 2000);
        BookUpdateRequest request = new BookUpdateRequest("Spring in Action", "Craig Walls", "978-0569456841", 2022);

        given(bookRepository.findById(bookId)).willReturn(Optional.of(existingBook));
        given(bookRepository.save(any(Book.class))).willAnswer(invocation -> invocation.getArgument(0));

        Book updatedBook = bookService.updateBook(request, bookId);

        assertNotNull(updatedBook);
        assertEquals("Spring in Action", updatedBook.getTitle());
        assertEquals("Craig Walls", updatedBook.getAuthor());
        assertEquals("978-0569456841", updatedBook.getIsbn());
        assertEquals(2022, updatedBook.getPublishedYear());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when book does not exist")
    public void throwException_whenBookNotFound() {
        long bookId = 99L;
        BookUpdateRequest request = new BookUpdateRequest("Spring in Action", "Craig Walls", "ISBN", 2023);

        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.updateBook(request, bookId));

        assertEquals("Book with id 99 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Should only update non-null fields (old one stay the same)")
    public void updateBook_onlyNonNullFields() {
        Long bookId = 1L;
        Book existingBook = new Book(bookId, "Spring in Action", "Kane Wayc", "978-0134685991", 2000);
        BookUpdateRequest request = new BookUpdateRequest(null, "Craig Walls", null, null);

        given(bookRepository.findById(bookId)).willReturn(Optional.of(existingBook));
        given(bookRepository.save(any(Book.class))).willAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.updateBook(request, bookId);

        assertEquals("Spring in Action", result.getTitle());
        assertEquals("Craig Walls", result.getAuthor());
        assertEquals("978-0134685991", result.getIsbn());
        assertEquals(2000, result.getPublishedYear());
    }
}


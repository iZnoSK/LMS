package org.project.LMS.serviceTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.model.Book;
import org.project.LMS.repositories.BookRepository;
import org.project.LMS.services.BookService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetBookByIdServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @DisplayName("Test if method getBookById returns book, when book exists")
    @Test
    public void getBook_whenBookExists() {
        Long bookId = 1L;
        Book book_created = new Book(bookId, "Spring in Action",
                "Craig Walls", "978-1617294945", 2018);

        given(bookRepository.findById(bookId)).willReturn(Optional.of(book_created));

        Book book_resultOfMethod = bookService.getBookById(bookId);

        assertNotNull(book_resultOfMethod);
        assertEquals(book_created, book_resultOfMethod);
    }

    @DisplayName("Test if method getBookById throws exception, when book does not exist")
    @Test
    public void throwException_whenBookDoesNotExist() {
        given(bookRepository.findById(anyLong())).willReturn(Optional.empty());

        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class,
                () -> bookService.getBookById(1L));

        assertEquals("Book with id 1 not found.", thrownException.getMessage());
    }
}
package org.project.LMS.serviceTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.LMS.model.Book;
import org.project.LMS.repositories.BookRepository;
import org.project.LMS.services.BookService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AddBookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @DisplayName("Test if method saves book successfully when title and ISBN are unique")
    @Test
    public void saveBook_whenValidBook() {
        Book book = new Book(null, "Effective Java", "Joshua Bloch", "978-0134685991", 2018);
        Book savedBook = new Book(1L, "Effective Java", "Joshua Bloch", "978-0134685991", 2018);

        given(bookRepository.save(book)).willReturn(savedBook);

        Book resultOfMethod = bookService.addBook(book);

        assertNotNull(resultOfMethod);
        assertEquals(1L, resultOfMethod.getId());
        assertEquals("Effective Java", resultOfMethod.getTitle());
        verify(bookRepository).save(book);
    }
}

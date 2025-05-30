package org.project.LMS.serviceTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.LMS.model.Book;
import org.project.LMS.model.BookCopy;
import org.project.LMS.repositories.BookCopyRepository;
import org.project.LMS.services.BookService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetBookCopiesServiceTest {

    @Mock
    private BookCopyRepository bookCopyRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("This method returns list of book copies for a given book")
    void getBookCopies_whenBookHasCopies() {
        Book book = new Book(1L, "Spring in Action", "Craig Walls", "978-1617294945", 2018);
        BookCopy copy1 = new BookCopy(1L, book, true);
        BookCopy copy2 = new BookCopy(2L, book, false);
        List<BookCopy> expectedCopies = List.of(copy1, copy2);

        when(bookCopyRepository.findByBook(book)).thenReturn(expectedCopies);

        List<BookCopy> actualCopies = bookService.getBookCopies(book);

        assertEquals(2, actualCopies.size());
        assertEquals(true, actualCopies.get(0).getAvailable());
        assertEquals(false, actualCopies.get(1).getAvailable());
    }

    @Test
    @DisplayName("this method returns empty list when book has no copies")
    void getBookCopies_whenNoCopies_shouldReturnEmptyList() {
        Book book = new Book(2L, "Clean Code", "Robert C. Martin", "978-0132350884", 2008);

        when(bookCopyRepository.findByBook(book)).thenReturn(Collections.emptyList());

        List<BookCopy> actualCopies = bookService.getBookCopies(book);

        assertNotNull(actualCopies);
        assertTrue(actualCopies.isEmpty());
    }
}

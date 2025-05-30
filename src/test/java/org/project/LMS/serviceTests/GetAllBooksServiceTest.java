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
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GetAllBooksServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @DisplayName("Test if method getAllBooks returns paged list of books")
    @Test
    public void returnAllBooks_whenBooksExist() {
        Book book1 = new Book(1L, "Spring in Action", "Craig Walls", "978-1617294945", 2018);
        Book book2 = new Book(2L, "Clean Code", "Robert C. Martin", "978-0132350884", 2008);
        List<Book> books = Arrays.asList(book1, book2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
        Page<Book> expectedPage = new PageImpl<>(books, pageable, books.size());

        given(bookRepository.findAll(any(Pageable.class))).willReturn(expectedPage);

        Page<Book> resultOfMethod = bookService.getAllBooks(pageable);

        assertNotNull(resultOfMethod);
        assertEquals(2, resultOfMethod.getTotalElements());
        assertEquals(book1, resultOfMethod.getContent().get(0));
        assertEquals(book2, resultOfMethod.getContent().get(1));
    }

    @DisplayName("Test if method getAllBooks returns empty page when no books exist")
    @Test
    public void returnEmptyPage_whenNoBooksExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> emptyPage = Page.empty(pageable);

        given(bookRepository.findAll(pageable)).willReturn(emptyPage);

        Page<Book> resultOfMethod = bookService.getAllBooks(pageable);

        assertNotNull(resultOfMethod);
        assertTrue(resultOfMethod.isEmpty());
        assertEquals(0, resultOfMethod.getTotalElements());
    }
}

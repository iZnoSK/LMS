package org.project.librarymanagementsystem.controllerTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.project.librarymanagementsystem.controllers.BookController;
import org.project.librarymanagementsystem.dto.book.BookWithCopiesResponse;
import org.project.librarymanagementsystem.exceptions.ResourceNotFoundException;
import org.project.librarymanagementsystem.mappers.BookMapper;
import org.project.librarymanagementsystem.model.Book;
import org.project.librarymanagementsystem.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

@WebMvcTest(BookController.class)
public class GetBookByIdControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookMapper bookMapper;

    @DisplayName("Test if GET /api/books/1 returns book with ID 1 in response, when book exists")
    @Test
    public void returnBook_whenBookExists() throws Exception {
        Long bookId = 1L;
        String bookTitle = "Spring in Action";
        String author = "Craig Walls";
        String isbn = "978-1617294945";
        Integer publishedYear = 2018;
        Book book_created = new Book(bookId, bookTitle,author, isbn, publishedYear);

        BookWithCopiesResponse response = new BookWithCopiesResponse(bookId, bookTitle,author, isbn, publishedYear, Collections.emptyList());

        given(bookService.getBookById(bookId)).willReturn(book_created);
        given(bookService.getBookCopies(book_created)).willReturn(Collections.emptyList());
        given(bookMapper.toResponseWithCopies(Mockito.eq(book_created), Mockito.anyList())).willReturn(response);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value(bookTitle))
                .andExpect(jsonPath("$.author").value(author))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.publishedYear").value(publishedYear))
                .andExpect(jsonPath("$.copies").isArray())
                .andExpect(jsonPath("$.copies").isEmpty());
    }

    @DisplayName("Test if HTTP request getBookById throws exception, when book does not exist")
    @Test
    public void getBookById_ShouldReturn404_WhenNotFound() throws Exception {
        given(bookService.getBookById(anyLong()))
                .willThrow(new ResourceNotFoundException("Book with id 1 not found."));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Book with id 1 not found."));
    }
}
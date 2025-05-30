package org.project.LMS.controllerTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.LMS.controllers.BookController;
import org.project.LMS.dto.book.BookResponse;
import org.project.LMS.mappers.BookMapper;
import org.project.LMS.model.Book;
import org.project.LMS.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(BookController.class)
public class GetAllBooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookMapper bookMapper;

    @DisplayName("Test if GET /api/books returns paginated and sorted books")
    @Test
    public void returnBooksPage_whenBooksExist() throws Exception {
        // Vstupné parametre
        int page = 0;
        int size = 2;
        String sortBy = "title";
        String direction = "asc";

        // Vstupný Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        // Príprava dát: 2 knihy
        Book book1 = new Book(1L, "Spring in Action", "Craig Walls", "978-1617294945", 2018);
        Book book2 = new Book(2L, "Clean Code", "Robert C. Martin", "978-0132350884", 2008);
        List<Book> books = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        // Očakávané DTO odpovede
        BookResponse bookResponse1 = new BookResponse(1L, "Spring in Action", "Craig Walls", "978-1617294945", 2018);
        BookResponse bookResponse2 = new BookResponse(2L, "Clean Code", "Robert C. Martin", "978-0132350884", 2008);

        // Mock správanie
        given(bookService.getAllBooks(pageable)).willReturn(bookPage);
        given(bookMapper.toResponse(book1)).willReturn(bookResponse1);
        given(bookMapper.toResponse(book2)).willReturn(bookResponse2);

        // Volanie API a overenie výstupu
        mockMvc.perform(get("/api/books")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Spring in Action"))
                .andExpect(jsonPath("$.content[1].title").value("Clean Code"));
    }
}


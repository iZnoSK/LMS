package org.project.LMS.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.LMS.controllers.BookController;
import org.project.LMS.dto.book.BookCreateRequest;
import org.project.LMS.dto.book.BookResponse;
import org.project.LMS.mappers.BookMapper;
import org.project.LMS.model.Book;
import org.project.LMS.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class AddBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookMapper bookMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test if method saves book successfully when title and ISBN are unique")
    public void saveBook_whenValidArgs() throws Exception {
        BookCreateRequest request = new BookCreateRequest("Effective Java", "Joshua Bloch", "978-0134685991", 2018);
        Book book = new Book(1L, request.title(), request.author(), request.isbn(), request.publishedYear());
        BookResponse response = new BookResponse(1L, request.title(), request.author(), request.isbn(), request.publishedYear());

        given(bookService.addBook(any())).willReturn(book);
        given(bookMapper.toBook(request)).willReturn(book);
        given(bookMapper.toResponse(book)).willReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"))
                .andExpect(jsonPath("$.isbn").value("978-0134685991"))
                .andExpect(jsonPath("$.publishedYear").value(2018));
    }

    @Test
    @DisplayName("Test if method throws exception because of invalid request.")
    public void saveBook_whenInvalidRequest() throws Exception {
        BookCreateRequest invalidRequest = new BookCreateRequest("", "Author", "", 2020);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}

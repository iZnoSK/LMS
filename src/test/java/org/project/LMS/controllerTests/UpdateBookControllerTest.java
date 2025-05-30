package org.project.LMS.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.LMS.controllers.BookController;
import org.project.LMS.dto.book.BookResponse;
import org.project.LMS.dto.book.BookUpdateRequest;
import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.mappers.BookMapper;
import org.project.LMS.model.Book;
import org.project.LMS.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class UpdateBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookMapper bookMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should return 200 OK when book is successfully updated")
    void shouldReturn200_whenBookUpdated() throws Exception {
        Long bookId = 1L;

        BookUpdateRequest updateRequest = new BookUpdateRequest("Spring in Action", "Craig Walls", "978-0134685991", 2022);
        Book updatedBook = new Book(bookId, "Spring in Action", "Craig Walls", "978-0134685991", 2022);
        BookResponse updatedResponse = new BookResponse(bookId, "Spring in Action", "Craig Walls", "978-0134685991", 2022);

        given(bookService.updateBook(eq(updateRequest), eq(bookId))).willReturn(updatedBook);
        given(bookMapper.toResponse(updatedBook)).willReturn(updatedResponse);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Spring in Action"))
                .andExpect(jsonPath("$.author").value("Craig Walls"))
                .andExpect(jsonPath("$.isbn").value("978-0134685991"))
                .andExpect(jsonPath("$.publishedYear").value(2022));
    }

    @Test
    @DisplayName("Test if method returns 'not found' when book id is not in the database")
    void returnNotFound_bookIsNotInDB() throws Exception {
        Long bookId = 99L;
        BookUpdateRequest updateRequest = new BookUpdateRequest("Spring in Action", "Craig Walls", "978-0134685991", 2020);

        given(bookService.updateBook(eq(updateRequest), eq(bookId)))
                .willThrow(new ResourceNotFoundException("Book with id 99 not found."));

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Book with id 99 not found."));
    }

    @Test
    @DisplayName("Test if method returns bad request after validation fails")
    void isBadRequest_whenValidationFails() throws Exception {
        BookUpdateRequest invalidRequest = new BookUpdateRequest("", "", "bad", 1);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}

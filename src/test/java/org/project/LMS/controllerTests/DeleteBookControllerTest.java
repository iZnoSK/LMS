package org.project.LMS.controllerTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.LMS.controllers.BookController;
import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.mappers.BookMapper;
import org.project.LMS.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class DeleteBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookMapper bookMapper;

    @Test
    @DisplayName("This method returns No Content when book is successfully deleted")
    void deleteBook_WhenBookExists() throws Exception {
        Long bookId = 1L;

        doNothing().when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("This method returns Not Found when book does not exist")
    void deleteBook_WhenBookNotFound() throws Exception {
        Long bookId = 99L;

        doThrow(new ResourceNotFoundException("Book with id " + bookId + " not found."))
                .when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Book with id " + bookId + " not found."));
    }
}

package org.project.LMS.controllerTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.LMS.controllers.BookCopyController;
import org.project.LMS.dto.bookCopy.BookCopyResponse;
import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.mappers.BookCopyMapper;
import org.project.LMS.model.Book;
import org.project.LMS.model.BookCopy;
import org.project.LMS.services.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookCopyController.class)
@AutoConfigureMockMvc
class UpdateAvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookCopyService bookCopyService;

    @MockitoBean
    private BookCopyMapper bookCopyMapper;

    @Test
    @DisplayName("This method returns 200 OK and updates BookCopyResponse")
    void updateAvailability_whenSuccessful() throws Exception {
        Long bookId = 1L;
        Long copyId = 10L;
        boolean newAvailability = false;

        BookCopy updatedCopy = new BookCopy(copyId, new Book(bookId, "Title 1", "Author 1", "978-0134685991", 2020), newAvailability);
        BookCopyResponse response = new BookCopyResponse(copyId, false);

        given(bookCopyService.updateAvailability(bookId, copyId, newAvailability)).willReturn(updatedCopy);
        given(bookCopyMapper.toResponse(updatedCopy)).willReturn(response);

        String requestBody = """
                {
                    "available": false
                }
                """;

        mockMvc.perform(put("/api/books/{id}/copies/{copyId}", bookId, copyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(copyId))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    @DisplayName("this method returns exception when book or copy were not found")
    void updateAvailability_notFound() throws Exception {
        Long bookId = 1L;
        Long copyId = 999L;

        given(bookCopyService.updateAvailability(eq(bookId), eq(copyId), eq(true)))
                .willThrow(new ResourceNotFoundException("Copy not found"));

        String requestBody = """
                {
                    "available": true
                }
                """;

        mockMvc.perform(put("/api/books/{id}/copies/{copyId}", bookId, copyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("this method sends bad reguest when request body is invalid")
    void updateAvailability_invalidRequest() throws Exception {
        String invalidRequest = "{}";

        mockMvc.perform(put("/api/books/{id}/copies/{copyId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}

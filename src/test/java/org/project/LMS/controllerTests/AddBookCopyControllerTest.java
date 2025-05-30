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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookCopyController.class)
public class AddBookCopyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookCopyService bookCopyService;

    @MockitoBean
    private BookCopyMapper bookCopyMapper;

    @Test
    @DisplayName("This method adds book copy to book if that book exists")
    public void addBookCopy_WhenBookExists() throws Exception {
        Long bookId = 1L;
        BookCopy copy = new BookCopy(10L, new Book(bookId, "Title", "Author", "978-0134685991", 2020), true);
        BookCopyResponse response = new BookCopyResponse(10L, true);
        given(bookCopyService.addCopy(bookId)).willReturn(copy);
        given(bookCopyMapper.toResponse(copy)).willReturn(response);
        mockMvc.perform(post("/api/books/{id}/copies", bookId))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DisplayName("This method throws exception if book we want to add copy of, does not exist")
    public void addBookCopy_WhenBookNotFound() throws Exception {
        Long bookId = 99L;
        given(bookCopyService.addCopy(bookId))
                .willThrow(new ResourceNotFoundException("Book with id " + bookId + " not found."));
        mockMvc.perform(post("/api/books/{id}/copies", bookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Book with id 99 not found."));
    }
}

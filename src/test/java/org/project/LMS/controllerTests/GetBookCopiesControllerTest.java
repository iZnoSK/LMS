package org.project.LMS.controllerTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.LMS.controllers.BookCopyController;
import org.project.LMS.dto.bookCopy.BookCopyResponse;
import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.mappers.BookCopyMapper;
import org.project.LMS.model.BookCopy;
import org.project.LMS.services.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookCopyController.class)
public class GetBookCopiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookCopyService bookCopyService;

    @MockitoBean
    private BookCopyMapper bookCopyMapper;

    @Test
    @DisplayName("This method returns list of book copies when book exists")
    void returnCopies_whenBookExists() throws Exception {
        Long bookId = 1L;
        BookCopy copy1 = new BookCopy(1L, null, true);
        BookCopy copy2 = new BookCopy(2L, null, false);

        List<BookCopy> copies = List.of(copy1, copy2);

        BookCopyResponse resp1 = new BookCopyResponse(1L, true);
        BookCopyResponse resp2 = new BookCopyResponse(2L, false);

        when(bookCopyService.getCopiesForBook(bookId)).thenReturn(copies);
        when(bookCopyMapper.toResponseList(copies)).thenReturn(List.of(resp1, resp2));

        mockMvc.perform(get("/api/books/1/copies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].available").value(false));
    }

    @Test
    @DisplayName("This method returns 404 when book does not exist")
    void returnNotFound_whenBookDoesNotExist() throws Exception {
        Long bookId = 99L;

        when(bookCopyService.getCopiesForBook(bookId))
                .thenThrow(new ResourceNotFoundException("Book with id " + bookId + " not found."));

        mockMvc.perform(get("/api/books/99/copies"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Book with id 99 not found."));
    }
}

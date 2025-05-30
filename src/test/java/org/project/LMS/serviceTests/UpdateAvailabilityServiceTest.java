package org.project.LMS.serviceTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.LMS.exceptions.ResourceNotFoundException;
import org.project.LMS.model.Book;
import org.project.LMS.model.BookCopy;
import org.project.LMS.repositories.BookCopyRepository;
import org.project.LMS.repositories.BookRepository;
import org.project.LMS.services.BookCopyService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateAvailabilityServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookCopyRepository bookCopyRepository;

    @InjectMocks
    private BookCopyService bookCopyService;

    @Test
    @DisplayName("This method updates availability when book and copy are valid")
    void updateAvailability_whenValid() {
        Long bookId = 1L;
        Long copyId = 10L;
        Boolean newAvailability = false;

        Book book = new Book(bookId, "Test Book", "Author", "978-0134685991", 2023);
        BookCopy copy = new BookCopy(copyId, book, true);

        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(bookCopyRepository.findById(copyId)).willReturn(Optional.of(copy));
        given(bookCopyRepository.save(any(BookCopy.class))).willAnswer(inv -> inv.getArgument(0));

        BookCopy updated = bookCopyService.updateAvailability(bookId, copyId, newAvailability);

        assertNotNull(updated);
        assertEquals(copyId, updated.getId());
        assertFalse(updated.getAvailable());
    }

    @Test
    @DisplayName("This method throws exception when book is not found")
    void updateAvailability_bookNotFound() {
        given(bookRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookCopyService.updateAvailability(1L, 10L, true));
    }

    @Test
    @DisplayName("This method throws exception when book copy is not found")
    void updateAvailability_copyNotFound() {
        Long bookId = 1L;
        given(bookRepository.findById(bookId)).willReturn(Optional.of(new Book()));
        given(bookCopyRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookCopyService.updateAvailability(bookId, 10L, true));
    }

    @Test
    @DisplayName("This method throws exception when book copy does not belong to book")
    void updateAvailability_whenCopyMismatch() {
        Book book1 = new Book(1L, "Book A", "Author A", "978-0134685991", 2023);
        Book book2 = new Book(2L, "Book B", "Author B", "978-0188885991", 2021);
        BookCopy copy = new BookCopy(10L, book2, true);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book1));
        given(bookCopyRepository.findById(10L)).willReturn(Optional.of(copy));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> bookCopyService.updateAvailability(1L, 10L, false));

        assertEquals("This copy with id 10 does not belong to the book with id 1.", exception.getMessage());
    }
}

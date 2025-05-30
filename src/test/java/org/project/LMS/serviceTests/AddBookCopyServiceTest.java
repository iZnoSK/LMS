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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddBookCopyServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookCopyRepository bookCopyRepository;

    @InjectMocks
    private BookCopyService bookCopyService;

    @Test
    @DisplayName("This method adds book copy when book exists")
    public void addCopy_whenBookExists() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Spring", "Jake Bake", "978-0134685991", 2023);
        BookCopy savedCopy = new BookCopy(1L, book, true);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(savedCopy);

        BookCopy result = bookCopyService.addCopy(bookId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(true, result.getAvailable());
        assertEquals(book, result.getBook());

        verify(bookRepository).findById(bookId);
        verify(bookCopyRepository).save(any(BookCopy.class));
    }

    @Test
    @DisplayName("this method throws exception when book does not exist")
    public void addCopy_whenBookNotFound() {
        Long bookId = 99L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookCopyService.addCopy(bookId));

        verify(bookRepository).findById(bookId);
        verify(bookCopyRepository, never()).save(any());
    }
}

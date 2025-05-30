package org.project.LMS.dto.book;

import org.project.LMS.dto.bookCopy.BookCopyResponse;

import java.util.List;

public record BookWithCopiesResponse(
        Long id,
        String title,
        String author,
        String isbn,
        Integer publishedYear,
        List<BookCopyResponse> copies
) {}
package org.project.LMS.dto.book;

public record BookResponse (
        Long id,
        String title,
        String author,
        String isbn,
        Integer publishedYear
) {}

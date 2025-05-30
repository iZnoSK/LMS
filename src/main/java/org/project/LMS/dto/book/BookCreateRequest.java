package org.project.LMS.dto.book;

import jakarta.validation.constraints.*;

public record BookCreateRequest(
        @NotBlank(message = "Title of the book must be defined.")
        String title,

        @NotBlank(message = "Author of the book must be defined.")
        String author,

        @NotBlank(message = "ISBN of the book must be defined.")
        @Pattern(
                regexp = "^97[89]-\\d{10}$",
                message = "ISBN must be in format 978-XXXXXXXXXX"
        )
        String isbn,

        @NotNull(message = "Published year of the book must be defined.")
        @Min(value = 1000, message = "Published year must be >= 1000.")
        @Max(value = 2025, message = "Published year cannot be in the future.")
        Integer publishedYear
) {}

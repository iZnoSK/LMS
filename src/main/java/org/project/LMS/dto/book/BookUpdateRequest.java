package org.project.LMS.dto.book;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BookUpdateRequest(
        @Size(min = 1, message = "Title must not be blank.")
        String title,

        @Size(min = 1, message = "Author must not be blank.")
        String author,

        @Pattern(
                regexp = "^97[89]-\\d{10}$",
                message = "ISBN must be in the format 978-XXXXXXXXXX."
        )
        String isbn,

        @Min(value = 1000, message = "Published year must not be earlier than 1000.")
        @Max(value = 2025, message = "Published year must not be in the future.")
        Integer publishedYear
) {}
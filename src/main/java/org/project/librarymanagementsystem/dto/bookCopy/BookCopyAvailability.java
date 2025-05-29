package org.project.librarymanagementsystem.dto.bookCopy;

import jakarta.validation.constraints.NotNull;

public record BookCopyAvailability(
        @NotNull(message = "Availability must be defined.")
        Boolean available
) {}
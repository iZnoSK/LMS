package org.project.librarymanagementsystem.mappers;

import org.project.librarymanagementsystem.dto.bookCopy.BookCopyResponse;
import org.project.librarymanagementsystem.model.BookCopy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookCopyMapper {
    public BookCopyResponse toResponse(BookCopy copy) {
        return new BookCopyResponse(copy.getId(), copy.getAvailable());
    }

    public List<BookCopyResponse> toResponseList(List<BookCopy> copies) {
        return copies.stream()
                .map(this::toResponse)
                .toList();
    }
}
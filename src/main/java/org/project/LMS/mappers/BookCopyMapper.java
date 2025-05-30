package org.project.LMS.mappers;

import org.project.LMS.dto.bookCopy.BookCopyResponse;
import org.project.LMS.model.BookCopy;
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
package org.project.LMS.mappers;

import org.project.LMS.dto.bookCopy.BookCopyResponse;
import org.project.LMS.dto.book.BookCreateRequest;
import org.project.LMS.dto.book.BookResponse;
import org.project.LMS.dto.book.BookWithCopiesResponse;
import org.project.LMS.model.Book;
import org.project.LMS.model.BookCopy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {
    private final BookCopyMapper bookCopyMapper;

    public BookMapper(BookCopyMapper bookCopyMapper) {
        this.bookCopyMapper = bookCopyMapper;
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(),
                book.getAuthor(), book.getIsbn(), book.getPublishedYear());
    }

    public Book toBook(BookCreateRequest bookReq) {
        return new Book(null, bookReq.title(),
                bookReq.author(),bookReq.isbn(), bookReq.publishedYear());
    }

    /*
        public List<BookResponse> toResponseList(List<Book> books) {
        return books.stream()
                .map(this::toResponse)
                .toList();
    }
     */

    public BookWithCopiesResponse toResponseWithCopies(Book book, List<BookCopy> bookCopies) {
        List<BookCopyResponse> copyResponses = bookCopies != null
                ? bookCopies.stream()
                .map(this.bookCopyMapper::toResponse)
                .toList()
                : List.of();

        return new BookWithCopiesResponse(book.getId(), book.getTitle(), book.getAuthor(),
                book.getIsbn(), book.getPublishedYear(), copyResponses);
    }
}
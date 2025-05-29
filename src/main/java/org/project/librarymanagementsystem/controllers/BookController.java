package org.project.librarymanagementsystem.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.project.librarymanagementsystem.controllers.advice.ExceptionControllerAdvice;
import org.project.librarymanagementsystem.dto.book.BookCreateRequest;
import org.project.librarymanagementsystem.dto.book.BookResponse;
import org.project.librarymanagementsystem.dto.book.BookWithCopiesResponse;
import org.project.librarymanagementsystem.dto.book.BookUpdateRequest;
import org.project.librarymanagementsystem.mappers.BookMapper;
import org.project.librarymanagementsystem.model.Book;

import org.project.librarymanagementsystem.model.BookCopy;
import org.project.librarymanagementsystem.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Operations related to books")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @Operation(summary = "Get all books", description = "Retrieve all books with optional pagination and sorting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @Valid @RequestParam(defaultValue = "0") @Min(0) int page,
            @Valid @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @Valid @RequestParam(defaultValue = "id") @Pattern(regexp = "id|title|author|isbn|publishedYear") String sortBy,
            @Valid @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE) String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> bookPage = this.bookService.getAllBooks(pageable);
        Page<BookResponse> bookResponsePage = bookPage.map(this.bookMapper::toResponse);

        return ResponseEntity.ok(bookResponsePage);
    }

    @Operation(summary = "Get book by ID", description = "Retrieve book and its copies by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = BookWithCopiesResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format (e.g. non-numeric)",
                    content = @Content(schema = @Schema(example = "{ \"error\": \"Failed to convert value of type 'String' to required type 'Long'.\" }"))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionControllerAdvice.ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookWithCopiesResponse> getBookById(@PathVariable("id") Long id) {
        Book book = this.bookService.getBookById(id);
        List<BookCopy> bookCopies = this.bookService.getBookCopies(book);
        BookWithCopiesResponse response = this.bookMapper.toResponseWithCopies(book, bookCopies);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add new book", description = "Add a new book to the library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate title or ISBN", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookCreateRequest bookCreateRequest) {
        Book addedBook = this.bookService.addBook(this.bookMapper.toBook(bookCreateRequest));
        BookResponse bookResponse = this.bookMapper.toResponse(addedBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse);
    }

    @Operation(summary = "Update book", description = "Update fields of an existing book by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@Valid @RequestBody BookUpdateRequest bookUpdateRequest,
                                           @PathVariable("id") Long id) {
        Book updatedBook = this.bookService.updateBook(bookUpdateRequest, id);
        BookResponse updatedBookResponse = this.bookMapper.toResponse(updatedBook);
        return ResponseEntity.ok(updatedBookResponse);
    }

    @Operation(summary = "Delete book", description = "Delete a book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        this.bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

    /*
        @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<Book> books = this.bookService.getAllBooks();
        List<BookResponse> bookResponses = this.bookMapper.toResponseList(books);
        return ResponseEntity.ok.body(bookResponses);
    }
     */
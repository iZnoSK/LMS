package org.project.LMS.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.project.LMS.dto.bookCopy.BookCopyAvailability;
import org.project.LMS.dto.bookCopy.BookCopyResponse;
import org.project.LMS.mappers.BookCopyMapper;
import org.project.LMS.model.BookCopy;
import org.project.LMS.services.BookCopyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/{id}/copies")
@Tag(name = "BookCopies", description = "Operations related to book copies")
public class BookCopyController {
    private final BookCopyService bookCopyService;
    private final BookCopyMapper bookCopyMapper;

    public BookCopyController(BookCopyService bookCopyService, BookCopyMapper bookCopyMapper) {
        this.bookCopyService = bookCopyService;
        this.bookCopyMapper = bookCopyMapper;
    }

    @Operation(summary = "Get all copies for a specific book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of book copies returned"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping
    public ResponseEntity<List<BookCopyResponse>> getCopiesForBook(@PathVariable("id") Long id) {
        List<BookCopy> copies = this.bookCopyService.getCopiesForBook(id);
        return ResponseEntity.ok(this.bookCopyMapper.toResponseList(copies));
    }

    @Operation(summary = "Add a new copy for a specific book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book copy successfully created"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PostMapping
    public ResponseEntity<BookCopyResponse> addBookCopy(@PathVariable("id") Long id) {
        BookCopy copy = this.bookCopyService.addCopy(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.bookCopyMapper.toResponse(copy));
    }

    @Operation(summary = "Update availability status of a specific book copy")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book copy availability updated"),
            @ApiResponse(responseCode = "404", description = "Book or copy not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PutMapping("/{copyId}")
    public ResponseEntity<BookCopyResponse> updateAvailability(
            @PathVariable Long id,
            @PathVariable Long copyId,
            @Valid @RequestBody BookCopyAvailability request) {

        BookCopy updatedCopy = this.bookCopyService.updateAvailability(id, copyId, request.available());
        return ResponseEntity.ok(this.bookCopyMapper.toResponse(updatedCopy));
    }
}
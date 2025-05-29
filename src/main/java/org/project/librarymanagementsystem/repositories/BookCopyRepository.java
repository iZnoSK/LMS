package org.project.librarymanagementsystem.repositories;

import org.project.librarymanagementsystem.model.Book;
import org.project.librarymanagementsystem.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBook(Book book);
}

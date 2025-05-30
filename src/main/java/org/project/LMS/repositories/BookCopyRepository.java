package org.project.LMS.repositories;

import org.project.LMS.model.Book;
import org.project.LMS.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBook(Book book);
}

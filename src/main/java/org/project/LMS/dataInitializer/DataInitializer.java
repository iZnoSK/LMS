package org.project.LMS.dataInitializer;

import org.project.LMS.model.Book;
import org.project.LMS.model.BookCopy;
import org.project.LMS.repositories.BookCopyRepository;
import org.project.LMS.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public DataInitializer(BookRepository bookRepository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    public void run(String... args) {
        List<Book> books = List.of(
                new Book(null, "Effective Java", "Joshua Bloch", "978-0134685991", 2018),
                new Book(null, "Clean Code", "Robert C. Martin", "978-0132350884", 2008),
                new Book(null, "The Pragmatic Programmer", "Andy Hunt", "978-0201616224", 1999),
                new Book(null, "Refactoring", "Martin Fowler", "978-0201485677", 1999),
                new Book(null, "Domain-Driven Design", "Eric Evans", "978-0321125217", 2003),
                new Book(null, "Design Patterns", "Erich Gamma", "978-0201633610", 1994),
                new Book(null, "Head First Design Patterns", "Eric Freeman", "978-0596007126", 2004),
                new Book(null, "Working Effectively with Legacy Code", "Michael Feathers", "978-0131177055", 2004),
                new Book(null, "Introduction to Algorithms", "Cormen", "978-0262033848", 2009),
                new Book(null, "Test Driven Development", "Kent Beck", "978-0321146533", 2002),
                new Book(null, "Spring in Action", "Craig Walls", "978-1617294945", 2018),
                new Book(null, "Java Concurrency in Practice", "Brian Goetz", "978-0321349606", 2006),
                new Book(null, "Clean Architecture", "Robert C. Martin", "978-0134494166", 2017),
                new Book(null, "Algorithms", "Robert Sedgewick", "978-0321573513", 2011),
                new Book(null, "Patterns of Enterprise Application Architecture", "Martin Fowler", "978-0321127426", 2002),
                new Book(null, "You Don't Know JS", "Kyle Simpson", "978-1491904244", 2015),
                new Book(null, "Structure and Interpretation of Computer Programs", "Harold Abelson", "978-0262510875", 1996),
                new Book(null, "Eloquent JavaScript", "Marijn Haverbeke", "978-1593279509", 2018),
                new Book(null, "Programming Pearls", "Jon Bentley", "978-0201657883", 1999),
                new Book(null, "The Art of Computer Programming", "Donald Knuth", "978-0321751041", 2011)
        );

        this.bookRepository.saveAll(books);

        List<BookCopy> copies = new ArrayList<>();
        copies.add(new BookCopy(null, books.get(0), true));
        copies.add(new BookCopy(null, books.get(0), false));

        copies.add(new BookCopy(null, books.get(1), true));

        copies.add(new BookCopy(null, books.get(2), true));
        copies.add(new BookCopy(null, books.get(2), true));
        copies.add(new BookCopy(null, books.get(2), false));

        copies.add(new BookCopy(null, books.get(3), false));

        copies.add(new BookCopy(null, books.get(5), true));
        copies.add(new BookCopy(null, books.get(5), true));
        copies.add(new BookCopy(null, books.get(5), false));
        copies.add(new BookCopy(null, books.get(5), false));

        copies.add(new BookCopy(null, books.get(6), true));
        copies.add(new BookCopy(null, books.get(6), true));

        copies.add(new BookCopy(null, books.get(8), false));
        copies.add(new BookCopy(null, books.get(8), false));
        copies.add(new BookCopy(null, books.get(8), false));

        copies.add(new BookCopy(null, books.get(9), true));

        copies.add(new BookCopy(null, books.get(10), true));
        copies.add(new BookCopy(null, books.get(10), true));
        copies.add(new BookCopy(null, books.get(10), false));

        copies.add(new BookCopy(null, books.get(11), true));
        copies.add(new BookCopy(null, books.get(11), true));
        copies.add(new BookCopy(null, books.get(11), true));
        copies.add(new BookCopy(null, books.get(11), true));

        copies.add(new BookCopy(null, books.get(12), false));
        copies.add(new BookCopy(null, books.get(12), true));

        copies.add(new BookCopy(null, books.get(13), true));
        copies.add(new BookCopy(null, books.get(13), false));

        copies.add(new BookCopy(null, books.get(14), true));

        copies.add(new BookCopy(null, books.get(16), true));
        copies.add(new BookCopy(null, books.get(16), false));

        copies.add(new BookCopy(null, books.get(17), true));
        copies.add(new BookCopy(null, books.get(17), true));

        copies.add(new BookCopy(null, books.get(18), true));
        copies.add(new BookCopy(null, books.get(18), false));

        copies.add(new BookCopy(null, books.get(19), true));

        this.bookCopyRepository.saveAll(copies);
    }
}


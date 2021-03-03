package com.example.restexample.endpoint;


import com.example.restexample.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable("id") int id) {
        return bookService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book does not exists"));
    }

    @PostMapping("/books")
    public Book create(@RequestBody Book book) {
        if (book.getId() > 0) {
            throw new RuntimeException("Book id must be 0");
        }
        return bookService.save(book);
    }

    @PutMapping("/books/{id}")
    public Book update(@RequestBody Book book, @PathVariable("id") int id) {
        Book bookFromDB = bookService.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Book does not exists"));
        bookFromDB.setTitle(book.getTitle());
        bookFromDB.setDescription(book.getDescription());
        bookFromDB.setPrice(book.getPrice());
        bookFromDB.setAuthorName(book.getAuthorName());
        return bookService.save(bookFromDB);
    }

    @DeleteMapping("/books/{id}")
    public void delete(@PathVariable("id") int id) {
        bookService.delete(bookService.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Book does not exists")));
    }


}

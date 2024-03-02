package library.backend.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import library.backend.api.exceptions.BookNotFoundException;
import library.backend.api.models.Book;
import library.backend.api.repositories.BookRepository;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBooks(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("book/" + id + " doesn't exists."));
        return ResponseEntity.ok(book);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book newBook = bookRepository.save(book);
        return ResponseEntity.status(201).body(newBook);
    }
}

package library.backend.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import library.backend.api.services.BookService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBooks(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        return ResponseEntity.status(201).body(bookService.createBook(book));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}

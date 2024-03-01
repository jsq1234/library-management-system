package library.backend.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import library.backend.api.models.Book;
import library.backend.api.repositories.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    void createBook(Book book) {
        bookRepository.save(book);
    }

    void deleteBook(Book book) {
        bookRepository.deleteById(book.getId());
    }

    void updateBook(Book book) {
        bookRepository.save(book);
    }

    List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    List<Book> getAvailableBooks() {
        return bookRepository.findByQuantityGreaterThan(0);
    }

    List<Book> searchBooks(String title) {
        if (title == null || title.length() == 0) {
            return new ArrayList<>();
        }

        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

}

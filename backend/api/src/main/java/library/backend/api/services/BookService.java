package library.backend.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import library.backend.api.exceptions.BookNotFoundException;
import library.backend.api.models.Book;
import library.backend.api.repositories.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book getBookById(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("book/" + id + " doesn't exists."));
        return book;
    }
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)){
            throw new BookNotFoundException("book/" + id + " doesn't exists.");
        }
        bookRepository.deleteById(id);
    }

    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByQuantityGreaterThan(0);
    }

    public List<Book> searchBooks(String title) {
        if (title == null || title.length() == 0) {
            return new ArrayList<>();
        }

        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public Page<Book> getPagenatedBookList(int pageNumber, int size){
        Pageable page = PageRequest.of(pageNumber,size);
        return bookRepository.findAll(page);
    }
}

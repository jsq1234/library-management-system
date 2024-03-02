package library.backend.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import library.backend.api.config.AuthenticationConfig;
import library.backend.api.config.CustomAuthenticationEntryPoint;
import library.backend.api.config.JwtAuthenticationFilter;
import library.backend.api.config.SecurityConfig;
import library.backend.api.controllers.BookController;
import library.backend.api.models.Book;
import library.backend.api.repositories.BookRepository;
import library.backend.api.services.BookService;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {JwtAuthenticationFilter.class, SecurityConfig.class,
                        CustomAuthenticationEntryPoint.class, AuthenticationConfig.class}),},
        controllers = {BookController.class})
@ActiveProfiles(profiles = {"test"})
@AutoConfigureMockMvc(addFilters = false)
@Import(BookService.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void GetBookByIdTest() throws Exception {
        Book book = Book.builder().id(1L).title("test book").build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mvc.perform(get("/book/{id}", 1L)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(book.getId().intValue())))
                .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    public void GetNonExistingBookByIdReturnsNotFoundTest() throws Exception {
        when(bookRepository.findById(any())).thenReturn(Optional.empty());
        mvc.perform(get("/book/{id}", 1L)).andExpect(status().isNotFound());
    }


    @Test
    public void AddBookToDatabaseTest() throws Exception {
        Book book = Book.builder()
                        .title("test book")
                        .author("test author")
                        .ISBN("123-1234567890")
                        .genre("Physics").price(100.0f).quantity(5).publishDate(LocalDate.of(2024, 2, 10))
                        .build();

        mvc.perform(post("/book")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(book)))
            .andExpect(status().isCreated());

        verify(bookRepository).save(book);
    }

    @Test
    public void DeleteBookFromDatabaseTest() throws Exception{
        Book book = Book.builder()
                        .id(1L)
                        .title("test book")
                        .author("test author")
                        .ISBN("123-1234567890")
                        .genre("Physics").price(100.0f).quantity(5).publishDate(LocalDate.of(2024, 2, 10))
                        .build();
        when(bookRepository.existsById(1L)).thenReturn(true);
        
        mvc.perform(delete("/book/{id}", 1L))
            .andExpect(status().isOk());
        
        verify(bookRepository).deleteById(1L);
    }
}

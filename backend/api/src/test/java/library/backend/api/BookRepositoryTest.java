package library.backend.api;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import library.backend.api.models.Book;
import library.backend.api.repositories.BookRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest(properties = {
    "logging.level.ROOT= WARN",
    "logging.level.org.springframework.test.context.transaction= DEBUG",
})
@ActiveProfiles(profiles = {"test2"})
@AutoConfigureTestDatabase(replace=Replace.NONE)
//@Rollback(false)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private ObjectMapper mapper;
    
    @Value("classpath:books.json")
    Resource booksJson;

    List<Book> books;

    // @BeforeEach
    // void setUp() throws Exception{
        
    // }

    @Test
    void doSomething() throws Exception{
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        books = mapper.readValue(booksJson.getInputStream(),
                            new TypeReference<List<Book>>(){});
        books.forEach(entityManager::persistAndFlush);
        entityManager.clear();
        List<Book> firstSixBooks = bookRepository.findAll(PageRequest.of(0,6)).toList();
        assertEquals(6, firstSixBooks.size());
        for(int j=0; j<6; j++){
            assertEquals(books.get(j).getPublishDate(), firstSixBooks.get(j).getPublishDate(), "The dates do not match");
        }
    }

}

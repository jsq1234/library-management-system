package library.backend.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import library.backend.api.models.User;
import library.backend.api.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

@DataJpaTest
@ActiveProfiles(profiles = { "test" })
public class UserRepositoryTest {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private TestEntityManager entityManager;

        @BeforeEach
        void setUp() {
                User user1 = User.builder()
                                .email("test@example.com")
                                .password("1234")
                                .phoneNo("8178610509")
                                .name("test")
                                .role("USER")
                                .build();

                User user2 = User.builder()
                                .email("test2@example.com")
                                .password("1234")
                                .phoneNo("8178610509")
                                .name("test2")
                                .role("ADMIN")
                                .build();

                userRepository.save(user1);
                userRepository.save(user2);

                entityManager.clear();

        }

        @Test
        void insertingSameUserThrowsException() throws Exception {
                User user1 = User.builder()
                                .email("test@example.com")
                                .password("1234")
                                .phoneNo("8178610509")
                                .name("test")
                                .role("USER")
                                .build();

                assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user1));
        }
}

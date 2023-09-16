package com.example.propertymanagement.repositoryTests;

import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.UserRole;
import com.example.propertymanagement.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void userRepository_saveALl_returnSavedUser() {
        User user = User.builder()
                .email("test@gmail.com")
                .budget((long) 50000)
                .password("mishvelet")
                .role(UserRole.ADMIN)
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void userRepository_findUsersByEmailStartingWithZ() {
        User user = User.builder()
                .email("zura@gmail.com")
                .budget((long) 50000)
                .password("mishvelet")
                .role(UserRole.ADMIN)
                .build();
        User user2 = User.builder()
                .email("zaza@gmail.com")
                .budget((long) 50000)
                .password("mishvelet")
                .role(UserRole.ADMIN)
                .build();
        User user3 = User.builder()
                .email("nana@gmail.com")
                .budget((long) 50000)
                .password("mishvelet")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> users = userRepository.findUsersByEmailStartingWith("z");

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }
}

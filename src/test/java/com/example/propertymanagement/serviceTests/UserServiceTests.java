package com.example.propertymanagement.serviceTests;

import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.UserRole;
import com.example.propertymanagement.model.dto.UserDto;
import com.example.propertymanagement.repository.UserRepository;
import com.example.propertymanagement.security.PasswordEncoder;
import com.example.propertymanagement.service.implementation.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void userService_CreateUser_ReturnsUserDto() {
        User user = User.builder()
                .email("test@gmail.com")
                .role(UserRole.USER)
                .budget(60000L)
                .password(("mishvelet"))
                .properties(null)
                .build();

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

          userService.addUser(user);
            UserDto savedUserDto = userService.getUser(user.getEmail());

        Assertions.assertThat(savedUserDto).isNotNull();
        Assertions.assertThat(userService.getAllUsers().size()).isEqualTo(1);
    }
}

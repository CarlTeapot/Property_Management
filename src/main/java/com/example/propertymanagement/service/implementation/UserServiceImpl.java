package com.example.propertymanagement.service.implementation;

import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.UserRole;
import com.example.propertymanagement.model.dto.UserDto;
import com.example.propertymanagement.repository.PropertyRepository;
import com.example.propertymanagement.repository.UserRepository;
import com.example.propertymanagement.security.PasswordEncoder;
import com.example.propertymanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;
    private final PropertyRepository propertyRepository;
    private final PasswordEncoder encoder;
    private final static String EMPLOYEE_NOT_FOUND = "user with email %s not found";

    public void addUser(User user) {
        boolean exists = userRepository.findUserByEmail(user.getEmail())
                .isPresent();
        if (exists)
            throw new IllegalStateException("employee already exists");
        String encodedPassword = encoder.bCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        UserRole role = UserRole.valueOf("USER");
        user.setRole(role);
        userRepository.save(user);
    }

    public UserDto getUser(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);

        checkUser(email);

        System.out.println("user information successfully sent");
        return new UserDto(user.get());

    }

    public User getUserInformation(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);

        checkUser(email);

        return user.get();
    }

    public void checkUser(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        boolean exists = user.isPresent();
        if (!exists)
            throw new IllegalStateException("user doesn't exist");
    }

    public List<UserDto> getAllUsers() {
        System.out.println("database accessed");
        List<User> users = userRepository.findAll();
        return users
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public void addPropertyToUser(String email, String address) {
        Optional<User> user = userRepository.findUserByEmail(email);
        checkUser(email);

        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        boolean exists = property.isPresent();
        if (!exists)
            throw new IllegalStateException("properties by that address is not on the database");

        Set<Property> properties;
        properties = user.get().getProperties();
        properties.add(property.get());
        user.get().setProperties(properties);
        userRepository.save(user.get());
    }
    public void removePropertyFromUser(String email, String address) {
        Optional<User> user = userRepository.findUserByEmail(email);
        checkUser(email);

        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        boolean exists = property.isPresent();
        if (!exists)
            throw new IllegalStateException("properties by that address is not on the database");

        Set<Property> properties;
        properties = user.get().getProperties();
        properties.remove(property.get());
        user.get().setProperties(properties);
        userRepository.save(user.get());
    }
    public void removePropertyFromAllUsers(List<User> users, String address) {
        for (User user : users) {
            removePropertyFromUser(user.getEmail(), address);
        }
    }
    public void changePasswordAsAdmin(String email, String password) {
        Optional<User> user = userRepository.findUserByEmail(email);
        checkUser(email);
        User user1 = user.get();
        String encodedPassword = encoder.bCryptPasswordEncoder().encode(user1.getPassword());
        user1.setPassword(encodedPassword);

        userRepository.save(user1);
    }
    public void changePassword(@NonNull HttpServletRequest request,
                               String password) {
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(jwt);

            Optional<User> user = userRepository.findUserByEmail(userEmail);
            checkUser(userEmail);
            String encodedPassword = encoder.bCryptPasswordEncoder().encode(user.get().getPassword());
            user.get().setPassword(encodedPassword);

            userRepository.save(user.get());
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(EMPLOYEE_NOT_FOUND, email)));
    }
    public List<Property> getUserProperties(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        boolean exists = user.isPresent();
        if (!exists)
            throw new IllegalStateException("user does not exist");
        return propertyRepository.findPropertiesByUsersId(user.get().getId());

    }
    public void changeRole(String email, UserRole role) {
        Optional<User> user = userRepository.findUserByEmail(email);
        boolean exists = user.isPresent();
        if (!exists)
            throw new IllegalStateException("user does not exist");
        user.get().setRole(role);
        userRepository.save(user.get());
    }

    @Override
    public void changeBudget(@NonNull HttpServletRequest request, Long budget) {
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(jwt);

            Optional<User> user = userRepository.findUserByEmail(userEmail);
            checkUser(userEmail);

            user.get().setBudget(budget);
            userRepository.save(user.get());
        }
    @Override
    public void changeBudgetAsAdmin(String email, Long budget) {
        Optional<User> user = userRepository.findUserByEmail(email);
        checkUser(email);

        user.get().setBudget(budget);
        userRepository.save(user.get());


    }
    @Override
    public List<User> getUsersStartingWith(String letter) {
        List<User> users = userRepository.findUsersByEmailStartingWith(letter);
        return users;
    }

}


package com.blogsite.service;

import com.blogsite.exception.ResourceNotFoundException;
import com.blogsite.model.User;
import com.blogsite.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ---------------- CREATE USER ----------------

    @Test
    void createUser_success() {
        User user = new User();
        user.setUserEmailId("test@mail.com");
        user.setUserPassword("plainPassword");

        when(userRepository.findByUserEmailId("test@mail.com"))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.createUser(user);

        assertNotNull(savedUser);
        assertNotEquals("plainPassword", savedUser.getUserPassword());
        assertTrue(passwordEncoder.matches("plainPassword", savedUser.getUserPassword()));
    }

    @Test
    void createUser_emailAlreadyExists() {
        User user = new User();
        user.setUserEmailId("test@mail.com");

        when(userRepository.findByUserEmailId("test@mail.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(ResourceNotFoundException.class,
                () -> userService.createUser(user));
    }

    // ---------------- GET ALL USERS ----------------

    @Test
    void getAllUsers_success() {
        when(userRepository.findAll())
                .thenReturn(List.of(new User(), new User()));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    // ---------------- GET USER BY ID ----------------

    @Test
    void getUserById_success() {
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
    }

    // ---------------- GET USER BY EMAIL ----------------

    @Test
    void getUserByEmail_success() {
        User user = new User();
        user.setUserEmailId("test@mail.com");

        when(userRepository.findByUserEmailId("test@mail.com"))
                .thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("test@mail.com");

        assertNotNull(result);
        assertEquals("test@mail.com", result.getUserEmailId());
    }

    @Test
    void getUserByEmail_notFound() {
        when(userRepository.findByUserEmailId("test@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByEmail("test@mail.com"));
    }

    // ---------------- UPDATE USER ----------------

    @Test
    void updateUser_success() {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUserPassword(passwordEncoder.encode("oldPass"));

        User updatedUser = new User();
        updatedUser.setUserName("New Name");
        updatedUser.setUserEmailId("new@mail.com");
        updatedUser.setUserPassword("newPass");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updatedUser);

        assertEquals("New Name", result.getUserName());
        assertEquals("new@mail.com", result.getUserEmailId());
        assertTrue(passwordEncoder.matches("newPass", result.getUserPassword()));
    }

    @Test
    void updateUser_notFound() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(1L, new User()));
    }

    // ---------------- DELETE USER ----------------

    @Test
    void deleteUser_success() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));

        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(1L));
    }

    // ---------------- LOGIN USER ----------------

    @Test
    void loginUser_success() {
        User savedUser = new User();
        savedUser.setUserEmailId("test@mail.com");
        savedUser.setUserPassword(passwordEncoder.encode("password"));

        User loginRequest = new User();
        loginRequest.setUserEmailId("test@mail.com");
        loginRequest.setUserPassword("password");

        when(userRepository.findByUserEmailId("test@mail.com"))
                .thenReturn(Optional.of(savedUser));

        User result = userService.loginUser(loginRequest);

        assertNotNull(result);
        assertEquals("test@mail.com", result.getUserEmailId());
    }

    @Test
    void loginUser_wrongPassword() {
        User savedUser = new User();
        savedUser.setUserEmailId("test@mail.com");
        savedUser.setUserPassword(passwordEncoder.encode("password"));

        User loginRequest = new User();
        loginRequest.setUserEmailId("test@mail.com");
        loginRequest.setUserPassword("wrongPassword");

        when(userRepository.findByUserEmailId("test@mail.com"))
                .thenReturn(Optional.of(savedUser));

        assertThrows(ResourceNotFoundException.class,
                () -> userService.loginUser(loginRequest));
    }

    @Test
    void loginUser_userNotFound() {
        User loginRequest = new User();
        loginRequest.setUserEmailId("test@mail.com");

        when(userRepository.findByUserEmailId("test@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.loginUser(loginRequest));
    }
}

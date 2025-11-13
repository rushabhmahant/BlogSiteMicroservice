package com.blogsite.service;

import java.util.List;
import java.util.Optional;

import com.blogsite.model.User;

public interface UserService {
	
	// Create new user
    public User createUser(User user);

    // Get all users
    public List<User> getAllUsers();

    // Get user by ID
    public Optional<User> getUserById(Long id);

    // Get user by email
    public User getUserByEmail(String email);

    // Update user
    public User updateUser(Long id, User updatedUser);

    // Delete user
    public void deleteUser(Long id);

}

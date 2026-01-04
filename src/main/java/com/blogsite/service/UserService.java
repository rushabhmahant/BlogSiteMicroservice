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
    public User getUserById(Long id);

    // Get user by email
    public User getUserByEmail(String email);
    
 // Create new user
    public User loginUser(User user);

    // Update user
    public User updateUser(Long id, User updatedUser);

    // Delete user
    public void deleteUser(Long id);

}

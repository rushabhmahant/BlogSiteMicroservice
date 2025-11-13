package com.blogsite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogsite.model.User;
import com.blogsite.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserRepository userRepository;
	
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Create new user
    public User createUser(User user) {
    		String encodedPassword = passwordEncoder.encode(user.getUserPassword());
    		user.setUserPassword(encodedPassword);
    		return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByUserEmailId(email);
    }

    // Update user
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUserName(updatedUser.getUserName());
                    user.setUserEmailId(updatedUser.getUserEmailId());
                    user.setUserPassword(passwordEncoder.encode(updatedUser.getUserPassword()));
                    return userRepository.save(user);
                }).orElse(null);
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

	@Override
	public User loginUser(User user) {
		User savedUser = userRepository.findByUserEmailId(user.getUserEmailId());
		if(savedUser != null) {
			boolean passwordMatched = passwordEncoder.matches(user.getUserPassword(), savedUser.getUserPassword());
			if(passwordMatched) {
				return savedUser;
			}
		}
		return null;
	}

}

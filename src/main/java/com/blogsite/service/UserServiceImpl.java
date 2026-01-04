package com.blogsite.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogsite.exception.ResourceNotFoundException;
import com.blogsite.model.User;
import com.blogsite.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// Create new user
	public User createUser(User user) {

		Optional<User> presentUser = userRepository.findByUserEmailId(user.getUserEmailId());
		if (presentUser.isPresent()) {
			log.error("User is already present with email id: " + user.getUserEmailId());
			throw new ResourceNotFoundException("User aleady registered with email id: " + user.getUserEmailId());
		}

		String encodedPassword = passwordEncoder.encode(user.getUserPassword());
		user.setUserPassword(encodedPassword);
		log.info("Registering user: " + user.getUserEmailId());
		return userRepository.save(user);
	}

	// Get all users
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Get user by ID
	public User getUserById(Long id) {
		log.info("Fetching user by id: " + id);
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}

	// Get user by email
	public User getUserByEmail(String email) {
		log.info("Fetching user by email id: " + email);
		return userRepository.findByUserEmailId(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email id: " + email));
	}

	// Update user
	public User updateUser(Long id, User updatedUser) {
		return userRepository.findById(id).map(user -> {
			log.info("Found user by id: " + id + ". Updating with nrew details");
			user.setUserName(updatedUser.getUserName());
			user.setUserEmailId(updatedUser.getUserEmailId());
			user.setUserPassword(passwordEncoder.encode(updatedUser.getUserPassword()));
			return userRepository.save(user);
		}).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}

	// Delete user
	public void deleteUser(Long id) {
		userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		userRepository.deleteById(id);
	}

	// Login user
	@Override
	public User loginUser(User user) {
		return userRepository.findByUserEmailId(user.getUserEmailId()).map(savedUser -> {
			log.info("User found for email: " + user.getUserEmailId());
			boolean passwordMatched = passwordEncoder.matches(user.getUserPassword(), savedUser.getUserPassword());
			if (passwordMatched) {
				log.info("User credentials verified.");
				return savedUser;
			} else {
				log.error("Invalid user credentials for: " + user.getUserEmailId());
				throw new ResourceNotFoundException(
						"User credentials incorrect for email id: " + user.getUserEmailId());
			}
		}).orElseThrow(() -> new ResourceNotFoundException("User not found with email id: " + user.getUserEmailId()));

	}

}

package com.blogsite.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsite.model.Blog;
import com.blogsite.model.User;
import com.blogsite.service.BlogService;
import com.blogsite.service.UserService;

@RestController
@RequestMapping("/api/v1/blogsite")
public class BlogSiteController {

	private static final Logger log = LoggerFactory.getLogger(BlogSiteController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private BlogService blogService;

	// Create new user
	@PostMapping("/user/register")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		log.info("Registering user with email id: " + user.getUserEmailId());
		User savedUser = userService.createUser(user);
		return ResponseEntity.ok(savedUser);
	}

	// Get all users
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		log.info("Fetching all users");
		return ResponseEntity.ok(userService.getAllUsers());
	}

	// Get user by ID
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		log.info("Fetching user by id: " + id);
		User user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	// Get user by email
	@GetMapping("/user/email/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		log.info("Fetching user by email id: " + email);
		User user = userService.getUserByEmail(email);
		return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
	}

	// User Login
	@PostMapping("/user/login")
	public ResponseEntity<User> loginUser(@RequestBody User user) {
		log.info("Logging in user: " + user.getUserEmailId());
		User savedUser = userService.loginUser(user);
		return (savedUser != null) ? ResponseEntity.ok(savedUser) : ResponseEntity.notFound().build();
	}

	// Blog Services

	// Create a new blog for a given user
	@PostMapping("/user/blog/add/{userId}")
	public ResponseEntity<Blog> createBlog(@PathVariable Long userId, @RequestBody Blog blog) {
		log.info("Creating blog: " + blog.getBlogName() + " for user id: " + userId);
		Blog createdBlog = blogService.createBlog(userId, blog);
		return ResponseEntity.ok(createdBlog);
	}

	// Get all blogs
	@GetMapping("/blogs")
	public ResponseEntity<List<Blog>> getAllBlogs() {
		log.info("Fetching all blogs");
		return ResponseEntity.ok(blogService.getAllBlogs());
	}

	// Get a blog by ID
	@GetMapping("/blog/{id}")
	public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
		log.info("Fetching blog by id: " + id);
		Blog blog = blogService.getBlogById(id);
		return ResponseEntity.ok(blog);
	}

	// Get all blogs for a specific user
	@GetMapping("/blog/user/{userId}")
	public ResponseEntity<List<Blog>> getBlogsByUserId(@PathVariable Long userId) {
		log.info("Fetching all blogs of user id: " + userId);
		return ResponseEntity.ok(blogService.getBlogsByUserId(userId));
	}

	// Get all blogs by a category
	@GetMapping("/blogs/get/{category}")
	public ResponseEntity<List<Blog>> getBlogById(@PathVariable String category) {
		log.info("Fetching blogs by category: " + category);
		List<Blog> foundBlogs = blogService.getBlogsByBlogCategory(category);
		log.info("Found blogs by category: " + foundBlogs);
		return !foundBlogs.isEmpty() ? ResponseEntity.ok(foundBlogs) : ResponseEntity.notFound().build();
	}

	// Get all blogs created in a duration
	@GetMapping("/blogs/get/{durationFrom}/{durationTo}")
	public ResponseEntity<List<Blog>> getBlogsInDuration(@PathVariable String durationFrom,
			@PathVariable String durationTo) {
		log.info("Fetching blogs created between " + durationFrom + " and " + durationTo);
		List<Blog> foundBlogs = blogService.getBlogsByCreationDuration(LocalDateTime.parse(durationFrom),
				LocalDateTime.parse(durationTo));
		log.info("Found blogs by duration: " + foundBlogs);
		return !foundBlogs.isEmpty() ? ResponseEntity.ok(foundBlogs) : ResponseEntity.notFound().build();
	}

	// Update a blog
	@PutMapping("/blog/{id}")
	public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
		log.info("Updating blog " + blog.getBlogName() + " having blog id: " + blog.getBlogId());
		Blog updated = blogService.updateBlog(id, blog);
		return ResponseEntity.ok(updated);
	}

	// Delete a blog
	@DeleteMapping("/blog/{id}")
	public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
		log.info("Deleting blog id: " + id);
		blogService.deleteBlog(id);
		return ResponseEntity.noContent().build();
	}

}

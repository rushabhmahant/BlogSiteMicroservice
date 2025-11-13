package com.blogsite.controller;

import java.util.List;

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
	
	@Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;
	
	// Create new user
    @PostMapping("/user/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by ID
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get user by email
    @GetMapping("/user/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
    
    // User Login
    @PostMapping("/user/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        User savedUser = userService.loginUser(user);
        return (savedUser != null) ? ResponseEntity.ok(savedUser) : ResponseEntity.notFound().build();
    }
    
    
    
    // Blog Services

    // Create a new blog for a given user
    @PostMapping("/user/blog/add/{userId}")
    public ResponseEntity<Blog> createBlog(@PathVariable Long userId, @RequestBody Blog blog) {
        Blog createdBlog = blogService.createBlog(userId, blog);
        return ResponseEntity.ok(createdBlog);
    }

    // Get all blogs
    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    // Get a blog by ID
    @GetMapping("/blog/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        return blogService.getBlogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all blogs for a specific user
    @GetMapping("/blog/user/{userId}")
    public ResponseEntity<List<Blog>> getBlogsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(blogService.getBlogsByUserId(userId));
    }
    
    // Get all blogs by a category
    @GetMapping("/blogs/get/{category}")
    public ResponseEntity<List<Blog>> getBlogById(@PathVariable String category) {
    		List<Blog> foundBlogs = blogService.getBlogsByBlogCategory(category);
    		return !foundBlogs.isEmpty() ? ResponseEntity.ok(foundBlogs) : ResponseEntity.notFound().build();
    }

    // Update a blog
    @PutMapping("/blog/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
        Blog updated = blogService.updateBlog(id, blog);
        return ResponseEntity.ok(updated);
    }

    // Delete a blog
    @DeleteMapping("/blog/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

}

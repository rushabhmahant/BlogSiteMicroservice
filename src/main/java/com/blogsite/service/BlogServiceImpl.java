package com.blogsite.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogsite.model.Blog;
import com.blogsite.model.User;
import com.blogsite.repository.BlogRepository;
import com.blogsite.repository.UserRepository;

@Service
public class BlogServiceImpl implements BlogService {
	
	@Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new blog
    public Blog createBlog(Long userId, Blog blog) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOptional.get();
        blog.setUser(user);
        blog.setBlogCreationTime(LocalDateTime.now());
        return blogRepository.save(blog);
    }

    // Get all blogs
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    // Get a blog by ID
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    // Get all blogs for a specific user
    public List<Blog> getBlogsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return blogRepository.findByUser(userOptional.get());
    }

    // Update a blog
    public Blog updateBlog(Long id, Blog updatedBlog) {
        return blogRepository.findById(id)
                .map(blog -> {
                    blog.setBlogName(updatedBlog.getBlogName());
                    blog.setBlogCategory(updatedBlog.getBlogCategory());
                    blog.setBlogArticle(updatedBlog.getBlogArticle());
                    blog.setBlogAuthorName(updatedBlog.getBlogAuthorName());
                    return blogRepository.save(blog);
                })
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
    }

    // Delete a blog
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

}

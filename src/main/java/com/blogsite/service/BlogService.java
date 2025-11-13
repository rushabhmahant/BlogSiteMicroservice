package com.blogsite.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.blogsite.model.Blog;

public interface BlogService {
	
	public Blog createBlog(Long userId, Blog blog);

    // Get all blogs
    public List<Blog> getAllBlogs();

    // Get a blog by ID
    public Optional<Blog> getBlogById(Long id);

    // Get all blogs for a specific user
    public List<Blog> getBlogsByUserId(Long userId);
    
    // Get all blogs for a specific category
    public List<Blog> getBlogsByBlogCategory(String blogCategory);

    // Update a blog
    public Blog updateBlog(Long id, Blog updatedBlog);

    // Delete a blog
    public void deleteBlog(Long id);

	public List<Blog> getBlogsByCreationDuration(LocalDateTime durationFrom, LocalDateTime durationTo);

}

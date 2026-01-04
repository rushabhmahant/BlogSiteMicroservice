package com.blogsite.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogsite.exception.ResourceNotFoundException;
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
    	
    		User user = userRepository.findById(userId).orElseThrow(() ->
    			new ResourceNotFoundException("User not found with id: " + userId));

        blog.setUser(user);
        blog.setBlogCreationTime(LocalDateTime.now());
        return blogRepository.save(blog);
    }

    // Get all blogs
    public List<Blog> getAllBlogs() {
    		List<Blog> allBlogs = blogRepository.findAll();
    		allBlogs.stream().forEach(b -> b.setUser(null));
        return allBlogs;
    }

    // Get a blog by ID
    public Blog getBlogById(Long id) {
    		return blogRepository.findById(id)
    				.map(blogFound -> {
    					User foundUser = userRepository.getById(blogFound.getUser().getUserId());
    					User user = new User(foundUser.getUserName(), foundUser.getUserEmailId());
    	    				user.setUserId(foundUser.getUserId());
    	    				blogFound.setUser(user);
    	    				return blogFound;
    				})
    				.orElseThrow(() ->
    					new ResourceNotFoundException("Blog not found with id: " + id));
    }

    // Get all blogs for a specific user
    public List<Blog> getBlogsByUserId(Long userId) {
    	
    		return userRepository.findById(userId)
    			.map(foundUser -> {
    				return blogRepository.findByUser(foundUser);
    			})
    		.orElseThrow(() ->
			new ResourceNotFoundException("User not found with id: " + userId));
    }

	@Override
	public List<Blog> getBlogsByBlogCategory(String blogCategory) {
		List<Blog> foundBlogs = blogRepository.findByBlogCategory(blogCategory);
		foundBlogs.stream().forEach(b -> b.setUser(null));
		return foundBlogs;
	}

	@Override
	public List<Blog> getBlogsByCreationDuration(LocalDateTime durationFrom, LocalDateTime durationTo) {
		List<Blog> foundBlogs = blogRepository.findByBlogCreationTimeBetween(durationFrom, durationTo);
		foundBlogs.stream().forEach(b -> b.setUser(null));
		return foundBlogs;
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
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
    }

    // Delete a blog
    public void deleteBlog(Long id) {
    		blogRepository.findById(id).orElseThrow(() ->
    			new ResourceNotFoundException("Blog not found with id: " + id));
        blogRepository.deleteById(id);
    }

}

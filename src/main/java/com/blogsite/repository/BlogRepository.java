package com.blogsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogsite.model.Blog;
import com.blogsite.model.User;

public interface BlogRepository extends JpaRepository<Blog, Long> {
	
	// Find all blogs written by a specific user
    List<Blog> findByUser(User user);

    // Optional: find by category
    List<Blog> findByBlogCategory(String blogCategory);

}

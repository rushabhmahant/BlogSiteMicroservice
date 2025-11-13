package com.blogsite.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsite.model.User;

@RestController
@RequestMapping("/api/v1/blogsite")
public class BlogSiteController {
	
	@PostMapping("/user/register")
	public User registerUser(User user) {
		
		return null;
	}

}

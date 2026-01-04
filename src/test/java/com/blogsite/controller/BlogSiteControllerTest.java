package com.blogsite.controller;

import com.blogsite.exception.ResourceNotFoundException;
import com.blogsite.model.Blog;
import com.blogsite.model.User;
import com.blogsite.service.BlogService;
import com.blogsite.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = BlogSiteController.class)
class BlogSiteControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BlogService blogService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------- USER APIs --------------------

    @Test
    void createUser_success() throws Exception {
        User user = new User();
        user.setUserEmailId("test@example.com");
        user.setUserName("Test User");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/blogsite/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmailId").value("test@example.com"))
                .andExpect(jsonPath("$.userName").value("Test User"));

        verify(userService).createUser(any(User.class));
    }

    @Test
    void getAllUsers_success() throws Exception {
        User user1 = new User("Alice", "alice@example.com", "pass");
        User user2 = new User("Bob", "bob@example.com", "pass");
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/blogsite/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getUserById_success() throws Exception {
        User user = new User("Alice", "alice@example.com", "pass");
        user.setUserId(1L);

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/blogsite/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("Alice"));
    }

    @Test
    void getUserByEmail_success() throws Exception {
        User user = new User("Alice", "alice@example.com", "pass");

        when(userService.getUserByEmail("alice@example.com")).thenReturn(user);

        mockMvc.perform(get("/api/v1/blogsite/user/email/alice@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Alice"));
    }

    @Test
    void loginUser_success() throws Exception {
        User user = new User("Alice", "alice@example.com", "pass");

        when(userService.loginUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/blogsite/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Alice"));
    }

    // -------------------- BLOG APIs --------------------

    @Test
    void createBlog_success() throws Exception {
        Blog blog = new Blog();
        blog.setBlogName("Test Blog");

        when(blogService.createBlog(eq(1L), any(Blog.class))).thenReturn(blog);

        mockMvc.perform(post("/api/v1/blogsite/user/blog/add/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blogName").value("Test Blog"));
    }

    @Test
    void getAllBlogs_success() throws Exception {
        Blog blog1 = new Blog();
        blog1.setBlogName("Blog 1");
        Blog blog2 = new Blog();
        blog2.setBlogName("Blog 2");

        when(blogService.getAllBlogs()).thenReturn(Arrays.asList(blog1, blog2));

        mockMvc.perform(get("/api/v1/blogsite/blogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBlogById_success() throws Exception {
        Blog blog = new Blog();
        blog.setBlogName("Blog 1");

        when(blogService.getBlogById(1L)).thenReturn(blog);

        mockMvc.perform(get("/api/v1/blogsite/blog/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blogName").value("Blog 1"));
    }

    @Test
    void getBlogsByUserId_success() throws Exception {
        Blog blog1 = new Blog();
        blog1.setBlogName("Blog 1");
        List<Blog> blogs = Arrays.asList(blog1);

        when(blogService.getBlogsByUserId(1L)).thenReturn(blogs);

        mockMvc.perform(get("/api/v1/blogsite/blog/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getBlogsByCategory_success() throws Exception {
        Blog blog1 = new Blog();
        blog1.setBlogName("Tech Blog");
        List<Blog> blogs = Arrays.asList(blog1);

        when(blogService.getBlogsByBlogCategory("Tech")).thenReturn(blogs);

        mockMvc.perform(get("/api/v1/blogsite/blogs/get/Tech"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].blogName").value("Tech Blog"));
    }

    @Test
    void getBlogsInDuration_success() throws Exception {
        Blog blog1 = new Blog();
        blog1.setBlogName("Blog Duration");
        List<Blog> blogs = Arrays.asList(blog1);

        String from = LocalDateTime.now().minusDays(5).toString();
        String to = LocalDateTime.now().toString();

        when(blogService.getBlogsByCreationDuration(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(blogs);

        mockMvc.perform(get("/api/v1/blogsite/blogs/get/" + from + "/" + to))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].blogName").value("Blog Duration"));
    }

    @Test
    void updateBlog_success() throws Exception {
        Blog blog = new Blog();
        blog.setBlogName("Updated Blog");

        when(blogService.updateBlog(eq(1L), any(Blog.class))).thenReturn(blog);

        mockMvc.perform(put("/api/v1/blogsite/blog/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blogName").value("Updated Blog"));
    }

    @Test
    void deleteBlog_success() throws Exception {
        mockMvc.perform(delete("/api/v1/blogsite/blog/1"))
                .andExpect(status().isNoContent());

        verify(blogService).deleteBlog(1L);
    }
}

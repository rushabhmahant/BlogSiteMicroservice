package com.blogsite.service;

import com.blogsite.exception.ResourceNotFoundException;
import com.blogsite.model.Blog;
import com.blogsite.model.User;
import com.blogsite.repository.BlogRepository;
import com.blogsite.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BlogServiceImpl blogService;

    // ---------------- CREATE BLOG ----------------

    @Test
    void createBlog_success() {
        User user = new User();
        user.setUserId(1L);

        Blog blog = new Blog();
        blog.setBlogName("Test Blog");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(blogRepository.save(any(Blog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Blog result = blogService.createBlog(1L, blog);

        assertNotNull(result);
        assertNotNull(result.getBlogCreationTime());
        assertEquals(user, result.getUser());
    }

    @Test
    void createBlog_userNotFound() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> blogService.createBlog(1L, new Blog()));
    }

    // ---------------- GET ALL BLOGS ----------------

    @Test
    void getAllBlogs_success() {
        Blog blog1 = new Blog();
        blog1.setUser(new User());

        Blog blog2 = new Blog();
        blog2.setUser(new User());

        when(blogRepository.findAll())
                .thenReturn(List.of(blog1, blog2));

        List<Blog> blogs = blogService.getAllBlogs();

        assertEquals(2, blogs.size());
        assertNull(blogs.get(0).getUser());
        assertNull(blogs.get(1).getUser());
    }

    // ---------------- GET BLOG BY ID ----------------

    @Test
    void getBlogById_success() {
        User dbUser = new User();
        dbUser.setUserId(1L);
        dbUser.setUserName("John");
        dbUser.setUserEmailId("john@mail.com");

        Blog blog = new Blog();
        blog.setBlogId(10L);
        blog.setBlogName("Test Blog");
        blog.setUser(dbUser);

        when(blogRepository.findById(10L))
                .thenReturn(Optional.of(blog));

        when(userRepository.getById(1L))
                .thenReturn(dbUser);

        Blog result = blogService.getBlogById(10L);

        assertNotNull(result);
        assertNotNull(result.getUser());
        assertEquals("John", result.getUser().getUserName());
        assertEquals("john@mail.com", result.getUser().getUserEmailId());
    }

    @Test
    void getBlogById_notFound() {
        when(blogRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> blogService.getBlogById(10L));
    }

    // ---------------- GET BLOGS BY USER ID ----------------

    @Test
    void getBlogsByUserId_success() {
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(blogRepository.findByUser(user))
                .thenReturn(List.of(new Blog(), new Blog()));

        List<Blog> blogs = blogService.getBlogsByUserId(1L);

        assertEquals(2, blogs.size());
    }

    @Test
    void getBlogsByUserId_userNotFound() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> blogService.getBlogsByUserId(1L));
    }

    // ---------------- GET BLOGS BY CATEGORY ----------------

    @Test
    void getBlogsByBlogCategory_success() {
        Blog blog = new Blog();
        blog.setUser(new User());

        when(blogRepository.findByBlogCategory("Tech"))
                .thenReturn(List.of(blog));

        List<Blog> blogs = blogService.getBlogsByBlogCategory("Tech");

        assertEquals(1, blogs.size());
        assertNull(blogs.get(0).getUser());
    }

    // ---------------- GET BLOGS BY CREATION DURATION ----------------

    @Test
    void getBlogsByCreationDuration_success() {
        Blog blog = new Blog();
        blog.setUser(new User());

        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        when(blogRepository.findByBlogCreationTimeBetween(from, to))
                .thenReturn(List.of(blog));

        List<Blog> blogs = blogService.getBlogsByCreationDuration(from, to);

        assertEquals(1, blogs.size());
        assertNull(blogs.get(0).getUser());
    }

    // ---------------- UPDATE BLOG ----------------

    @Test
    void updateBlog_success() {
        Blog existingBlog = new Blog();
        existingBlog.setBlogId(1L);
        existingBlog.setBlogName("Old");

        Blog updatedBlog = new Blog();
        updatedBlog.setBlogName("New");
        updatedBlog.setBlogCategory("Tech");
        updatedBlog.setBlogArticle("Article");
        updatedBlog.setBlogAuthorName("Author");

        when(blogRepository.findById(1L))
                .thenReturn(Optional.of(existingBlog));

        when(blogRepository.save(any(Blog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Blog result = blogService.updateBlog(1L, updatedBlog);

        assertEquals("New", result.getBlogName());
        assertEquals("Tech", result.getBlogCategory());
    }

    @Test
    void updateBlog_notFound() {
        when(blogRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> blogService.updateBlog(1L, new Blog()));
    }

    // ---------------- DELETE BLOG ----------------

    @Test
    void deleteBlog_success() {
        when(blogRepository.findById(1L))
                .thenReturn(Optional.of(new Blog()));

        doNothing().when(blogRepository).deleteById(1L);

        blogService.deleteBlog(1L);

        verify(blogRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBlog_notFound() {
        when(blogRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> blogService.deleteBlog(1L));
    }
}

package com.blogsite.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Blog")
public class Blog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blogId")
	private Long blogId;

	@Column(name = "blogName", nullable = false, length = 255)
	private String blogName;

	@Column(name = "blogCategory", nullable = false, length = 100)
	private String blogCategory;

	@Column(name = "blogArticle", nullable = false, length = 1000)
	private String blogArticle;

	@Column(name = "blogAuthorName", nullable = false, length = 255)
	private String blogAuthorName;

	@Column(name = "blogCreationTime", nullable = false, length = 255)
	private LocalDateTime blogCreationTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false) // FK to User table
	private User user;

	public Blog() {
		this.blogCreationTime = LocalDateTime.now(); // default creation time
	}

	public Blog(String blogName, String blogCategory, String blogArticle, String blogAuthorName, User user) {
		this.blogName = blogName;
		this.blogCategory = blogCategory;
		this.blogArticle = blogArticle;
		this.blogAuthorName = blogAuthorName;
		this.user = user;
		this.blogCreationTime = LocalDateTime.now();
	}

	public Long getBlogId() {
		return blogId;
	}

	public void setBlogId(Long blogId) {
		this.blogId = blogId;
	}

	public String getBlogName() {
		return blogName;
	}

	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	public String getBlogCategory() {
		return blogCategory;
	}

	public void setBlogCategory(String blogCategory) {
		this.blogCategory = blogCategory;
	}

	public String getBlogArticle() {
		return blogArticle;
	}

	public void setBlogArticle(String blogArticle) {
		this.blogArticle = blogArticle;
	}

	public String getBlogAuthorName() {
		return blogAuthorName;
	}

	public void setBlogAuthorName(String blogAuthorName) {
		this.blogAuthorName = blogAuthorName;
	}

	public LocalDateTime getBlogCreationTime() {
		return blogCreationTime;
	}

	public void setBlogCreationTime(LocalDateTime blogCreationTime) {
		this.blogCreationTime = blogCreationTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Blog [blogId=" + blogId + ", blogName=" + blogName + ", blogCategory=" + blogCategory + ", blogArticle="
				+ blogArticle + ", blogAuthorName=" + blogAuthorName + ", blogCreationTime=" + blogCreationTime
				+ ", user=" + user + "]";
	}

}

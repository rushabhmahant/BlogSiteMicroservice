package com.blogsite.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "User")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
	private Long userId;
	
	@Column(name = "userName", nullable = false, length = 255)
	private String userName;
	
	@Column(name = "userEmailId", nullable = false, unique = true, length = 255)
	private String userEmailId;
	
	@Column(name = "userPassword", nullable = false, length = 255)
	private String userPassword;
	
	 // Constructors
    public User() {
    }
    
    public User(String userName, String userEmailId) {
        this.userName = userName;
        this.userEmailId = userEmailId;
    }

    public User(String userName, String userEmailId, String userPassword) {
        this.userName = userName;
        this.userEmailId = userEmailId;
        this.userPassword = userPassword;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmailId='" + userEmailId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

}

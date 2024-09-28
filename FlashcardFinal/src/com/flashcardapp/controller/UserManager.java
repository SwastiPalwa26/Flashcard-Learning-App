
package com.flashcardapp.controller;

import com.flashcardapp.model.User;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager 
{
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/flashcard_app"; // Update with your database URL
    private static final String USER = "root";
    private static final String PASSWORD = "Jaishreeram"; // Update with your DB password

    
    private Map<String, User> users;

    public UserManager() 
    {
        users = new HashMap<>();
    }

    // Register a new user
    public boolean registerUser(User user) 
    {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); 

            pstmt.executeUpdate();
            return true; // Registration successful
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false; // Registration failed
        }
    }

    public boolean userExists(String username) 
    {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); 

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false; 

        }
    }
    // Login an existing user
    public User loginUser(String username, String password) 
    {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) 
            {
                String storedPassword = rs.getString("password");
                Pattern passwordPattern = Pattern.compile("\\s*([^\\u00a0\\s]+)\\s*");
                Matcher matcher = passwordPattern.matcher(password);

                if (matcher.matches() && matcher.group(1).equals(storedPassword)) 
                {
                    // Successful login, create and return a User object
                    int userId = rs.getInt("id");
                    User user = new User(userId, username, password);
                    return user;
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return null;
    }  
}

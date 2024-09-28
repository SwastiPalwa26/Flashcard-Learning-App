

package com.flashcardapp.controller;

import com.flashcardapp.model.Flashcard;
import com.flashcardapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardManager 
{

    private static final String DB_URL = "jdbc:mysql://localhost:3306/flashcard_app"; // Update with your database URL
    private static final String USER = "root"; 
    private static final String PASSWORD = "Jaishreeram"; // Update with your DB password

    // Establish a connection to the database
    private Connection connect() throws SQLException 
    {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    // Create a new flashcard
    public void addFlashcardForUser(Flashcard flashcard, User user) 
    {
        String sql = "INSERT INTO flashcards (user_id, question, answer, category) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, flashcard.getUserId()); 
            pstmt.setString(2, flashcard.getQuestion());
            pstmt.setString(3, flashcard.getAnswer());
            pstmt.setString(4, flashcard.getCategory());
            pstmt.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    // Retrieve all flashcards for a user
    public List<Flashcard> getFlashcardsForUser(User user) 
    {
        List<Flashcard> flashcards = new ArrayList<>();
        String sql = "SELECT * FROM flashcards WHERE user_id = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Flashcard flashcard = new Flashcard(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("question"),
                    rs.getString("answer"),
                    rs.getString("category")
                );
                flashcards.add(flashcard);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return flashcards;
    }

    // Update a flashcard
    public void updateFlashcard(Flashcard flashcard, User user) 
    {
        String sql = "UPDATE flashcards SET question = ?, answer = ?, category = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, flashcard.getQuestion());
            pstmt.setString(2, flashcard.getAnswer());
            pstmt.setString(3, flashcard.getCategory());
            pstmt.setInt(4, flashcard.getId());
            pstmt.setInt(5, user.getId());
            pstmt.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    // Delete a flashcard
    public void deleteFlashcardForUser(Flashcard flashcard, User user) 
    {
        String sql = "DELETE FROM flashcards WHERE id = ? AND user_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, flashcard.getId());
            pstmt.setInt(2, user.getId());
            pstmt.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}

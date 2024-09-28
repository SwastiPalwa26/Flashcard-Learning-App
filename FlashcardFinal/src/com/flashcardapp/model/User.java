package com.flashcardapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class User 
{
    private int id;
    private String username;
    private String password;
    private List<Flashcard> flashcards;

    // Constructor to initialize username, password, and flashcard list
    public User(int id, String username, String password) 
    {
        this.id=id;
        this.username = username;
        this.password = password;
        this.flashcards = new ArrayList<>();
    }
    
    // Getter for ID
    public int getId() 
    {
        return id;
    }

    // Getter for username
    public String getUsername() 
    {
        return username;
    }

    // Setter for username
    public void setUsername(String username) 
    {
        this.username = username;
    }

    // Getter for password
    public String getPassword() 
    {
        return password;
    }

    // Setter for password
    public void setPassword(String password) 
    {
        this.password = password;
    }

    // Getter for flashcards list
    public List<Flashcard> getFlashcards() 
    {
        return flashcards;
    }
    
    public void setFlashcards(List<Flashcard> flashcards) 
    {
        this.flashcards = flashcards; // Setter for flashcards
    }


    // Method to add a flashcard to the user's flashcards list
    public void addFlashcard(Flashcard flashcard) 
    {
        flashcards.add(flashcard);
    }

    // Method to remove a flashcard from the user's flashcards list
    public void removeFlashcard(Flashcard flashcard) 
    {
        flashcards.remove(flashcard);
    }

    // Method to find a flashcard by its question
    public Flashcard findFlashcardByQuestion(String question) 
    {
        for (Flashcard flashcard : flashcards) 
        {
            if (flashcard.getQuestion().equalsIgnoreCase(question)) 
                return flashcard;
        }
        return null;
    }
    
    //Method to get all flashcards for this user
    public List<Flashcard> getAllFlashcards() 
    {
        return Collections.unmodifiableList(flashcards); // Return an unmodifiable list
    }
}

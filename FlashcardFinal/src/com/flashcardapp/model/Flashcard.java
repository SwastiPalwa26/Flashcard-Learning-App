package com.flashcardapp.model;

public class Flashcard 
{
    private int id;
    private int userId;
    private String question;
    private String answer;
    private String category;

    public Flashcard(int id,int userId, String question, String answer, String category)
    {
        this.id=id;
        this.userId=userId;
        this.question=question;
        this.answer=answer;
        this.category=category;
    }
    
    //Getter and setter methods
    public String getQuestion()
    {
        return question;
    }
    
    public void setQuestion(String question)
    {
        this.question=question;
    }
    
    public String getAnswer()
    {
        return answer;
    }
    
    public void setAnswer(String answer)
    {
        this.answer=answer;
    }
    
    public String getCategory()
    {
        return category;
    }
    
    public void setCategory(String category)
    {
        this.category=category;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id=id;
    }
    
    public int getUserId() 
    {
        return userId; // Getter for userId
    }

    public void setUserId(int userId) 
    {
        this.userId = userId; // Setter for userId
    }
}

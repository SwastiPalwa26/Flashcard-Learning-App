
package com.flashcardapp.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection 
{
    private static final String URL = "jdbc:mysql://localhost:3306/flashcard_app"; 
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Jaishreeram"; 

    public static Connection getConnection() throws SQLException 
    {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

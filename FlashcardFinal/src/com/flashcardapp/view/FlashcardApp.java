package com.flashcardapp.view;

import com.flashcardapp.controller.LoginController;
import com.flashcardapp.controller.FlashcardAppController;
import com.flashcardapp.controller.UserManager;
import com.flashcardapp.model.User;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlashcardApp extends Application 
{

    private UserManager userManager;
    private Stage primaryStage; // Reference to the main stage

    public FlashcardApp() 
    {
        userManager = new UserManager(); // Initialize the UserManager
    }

    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        this.primaryStage=primaryStage;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flashcardapp/view/Login.fxml"));
        Parent root = loader.load();

        // Set the LoginController and UserManager
        LoginController loginController = loader.getController();
        loginController.setUserManager(userManager);
        loginController.setOnLoginSuccess(this::handleLoginSuccess); // Set login success callback

        // Set the scene and show the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Flashcard App");
        primaryStage.show();
    }

    private void handleLoginSuccess(User user) 
    {
        // Show the main application screen with the logged-in user
        System.out.println("Welcome, " + user.getUsername() + "!");
        showMainApplicationScreen(user);
    }
    
    private void showMainApplicationScreen(User user) 
    {
        try
        {
            // Load the main application FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flashcardapp/view/FlashcardApp.fxml"));
            Parent root =loader.load();
            
            // Set the MainApplicationController
            FlashcardAppController controller = loader.getController();
            controller.initialize(userManager, user);
            
            
            // Set the scene and show the stage
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) 
    {
        launch(args);
    }
}

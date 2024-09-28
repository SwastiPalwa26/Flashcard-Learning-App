package com.flashcardapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import com.flashcardapp.model.User;


public class LoginController 
{

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private Button loginButton, registerButton;

    private UserManager userManager;
    

    public void setUserManager(UserManager userManager) 
    {
        this.userManager = userManager;
    }
    
    
    public interface LoginSuccessCallback 
    {
        void onLoginSuccess(User user); // Define a callback interface for login success
    }
    
    private LoginSuccessCallback loginSuccessCallback;

    public void setOnLoginSuccess(LoginSuccessCallback callback) 
    {
        this.loginSuccessCallback = callback;
    }

    @FXML
    private void handleLogin() 
    {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) 
        {
            errorMessageLabel.setText("Please enter username and password.");
            return;
        }

        User user = userManager.loginUser(username, password);

        if (user != null) 
        {
            //Login successful, call the callback
            if (loginSuccessCallback != null) 
            {
                loginSuccessCallback.onLoginSuccess(user);
            } 
        } 
        else 
        {
            errorMessageLabel.setText("Invalid username or password.");
        }
    }
    
    @FXML
    private void handleRegister() 
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) 
        {
            errorMessageLabel.setText("Please enter username and password.");
            return;
        }

        // Check if username already exists
        if (userManager.userExists(username)) 
        {
            errorMessageLabel.setText("Username already exists.");
            return;
        }

        // Create a new user and register it
        User newUser = new User(0, username, password); // Assuming auto-generated ID
        boolean registrationSuccess = userManager.registerUser(newUser);

        if (registrationSuccess) 
        {
            // Successful registration, show a success message and switch to login screen
            errorMessageLabel.setText("Registration successful! Please login.");
            clearFields();
            loginButton.setDisable(false);
            registerButton.setDisable(true);
        } 
        else 
        {
            errorMessageLabel.setText("Registration failed.");
        }
    }

    private void clearFields() 
    {
        usernameField.clear();
        passwordField.clear();
    }
}



package com.flashcardapp.controller;

import com.flashcardapp.model.Flashcard;
import com.flashcardapp.model.User;

import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class FlashcardAppController 
{   
    private UserManager userManager;
    private User currentUser;
    private List<Flashcard> filteredFlashcards;
    private int currentIndex = 0;
    private Flashcard currentFlashcard;  // Store the currently viewed flashcard

    // Initialize FlashcardManager
    private FlashcardManager flashcardManager;

    @FXML
    private Label questionLabel, answerLabel, aboutLabel;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField questionInput, answerInput, categoryInput, searchField;
    @FXML
    private Button showAnswerButton, nextButton, previousButton, addFlashcardButton, searchButton, deleteButton, editButton;

    private DatabaseConnection databaseConnection;
    public FlashcardAppController()
    {
        
    }
    
    public FlashcardAppController(FlashcardManager flashcardManager) 
    {
        this.flashcardManager = flashcardManager;
    }
    
    public void initialize(UserManager userManager, User currentUser) 
    {

        this.userManager = userManager;
        this.currentUser = currentUser;

        if (flashcardManager == null) 
        {
            flashcardManager = new FlashcardManager(); // Initialize manually
        }
        
        // Fetch flashcards for the logged-in user from the database
        List<Flashcard> storedFlashcards = flashcardManager.getFlashcardsForUser(currentUser);

        // Ensure that the flashcards from the database are added to the currentUser
        currentUser.getFlashcards().addAll(storedFlashcards);

        // Set the filtered flashcards to all flashcards initially
        filteredFlashcards = new ArrayList<>(currentUser.getFlashcards());

        // Initialize the category combo box
        categoryComboBox.getItems().add("All");
        categoryComboBox.getItems().addAll(currentUser.getFlashcards().stream()
                .map(Flashcard::getCategory)
                .distinct()
                .collect(Collectors.toList()));

        categoryComboBox.setValue("All");  // Set default value to "All"
        categoryComboBox.setOnAction(e -> filterByCategory());

        // Display the first flashcard if available
        if (!filteredFlashcards.isEmpty()) 
        {
            displayFlashcard(currentIndex);
        } 
        else 
        {
            questionLabel.setText("No flashcards available.");
            answerLabel.setText("");
        }

        // Set up about label
        aboutLabel.setText("The Flashcard Learning App allows users to create, view, and manage flashcards with questions,\n" +
                "answers, and categories. Key features include filtering flashcards by category, editing flashcards, \n" +
                "deleting with confirmation, and navigating through flashcards. Future enhancements \n" +
                "include user authentication and persistent data storage.");


    }


    

    private void displayFlashcard(int index) 
    {
        if (filteredFlashcards.isEmpty() || index < 0 || index >= filteredFlashcards.size()) 
        {
            questionLabel.setText("No flashcard available.");
            answerLabel.setText("");
            return;
        }
        Flashcard flashcard = filteredFlashcards.get(index);
        
        questionLabel.setText(flashcard.getQuestion());
        answerLabel.setText(""); // Hide answer until button is clicked
        currentFlashcard = flashcard;
    }

    @FXML
    private void showAnswer() 
    {
        if (currentIndex >= 0 && currentIndex < filteredFlashcards.size()) 
        {
            answerLabel.setText(filteredFlashcards.get(currentIndex).getAnswer());
        }
    }

    @FXML
    private void nextFlashcard() 
    {
        if (currentIndex < filteredFlashcards.size() - 1) 
        {
            currentIndex++;
            displayFlashcard(currentIndex);
        }
    }

    @FXML
    private void previousFlashcard() 
    {
        if (currentIndex > 0) 
        {
            currentIndex--;
            displayFlashcard(currentIndex);
        }
    }

    @FXML
    private void addFlashcard() 
    {
        String question = questionInput.getText();
        String answer = answerInput.getText();
        String category = categoryInput.getText();

        if (question.isEmpty() || answer.isEmpty() || category.isEmpty()) 
        {
            showAlert(Alert.AlertType.ERROR,"Input Error", "Please fill in all fields before adding a flashcard.");
            return;
        }

        // Create flashcard instance
        Flashcard flashcard = new Flashcard(0, currentUser.getId(), question, answer, category); // ID will be set in the DB

        // Add flashcard to the database
        flashcardManager.addFlashcardForUser(flashcard, currentUser);
        currentUser.addFlashcard(flashcard); // Also keep it in the current user's list
        filteredFlashcards.add(flashcard); // Update the filtered flashcards

        // Update categoryComboBox with all categories from stored flashcards
        categoryComboBox.getItems().clear();
        categoryComboBox.getItems().addAll("All");
        categoryComboBox.getItems().addAll(currentUser.getFlashcards().stream()
          .map(Flashcard::getCategory)
          .distinct()
          .collect(Collectors.toList()));

        questionInput.clear();
        answerInput.clear();
        categoryInput.clear();
        displayFlashcard(currentIndex);
    }

    @FXML
    private void filterByCategory() 
    {
        String selectedCategory = categoryComboBox.getValue();
        
        // Check if the user has flashcards to avoid empty lists
        if (currentUser.getFlashcards().isEmpty()) 
        {
            filteredFlashcards.clear();
            questionLabel.setText("No flashcards available.");
            answerLabel.setText("");
            return;
        }
        
        if (selectedCategory == null || selectedCategory.equals("All")) 
        {
            filteredFlashcards = new ArrayList<>(currentUser.getFlashcards());        
        } 
        else 
        {
            filteredFlashcards = currentUser.getFlashcards().stream()
                    .filter(f -> f.getCategory().equalsIgnoreCase(selectedCategory))
                    .collect(Collectors.toList());
        }
        currentIndex = 0; // Reset index
        displayFlashcard(currentIndex);           
    }

    @FXML
    private void searchFlashcards() 
    {
        String keyword = searchField.getText().toLowerCase();
        
        // Search by both question and answer
        filteredFlashcards = currentUser.getFlashcards().stream()
                .filter(f -> f.getQuestion().toLowerCase().contains(keyword) || f.getAnswer().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        
        currentIndex = 0; // Reset index to show the first matching flashcard
        displayFlashcard(currentIndex);
    }

    @FXML
    private void editFlashcard() 
    {
        if (currentFlashcard != null) 
        {
            // Create a custom dialog for editing the flashcard
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Flashcard");

            // Set the header text
            dialog.setHeaderText("Edit the flashcard's details:");

            // Create the labels and fields for the dialog
            Label questionLabel = new Label("Edit Question:");
            TextField questionField = new TextField(currentFlashcard.getQuestion());

            Label answerLabel = new Label("Edit Answer:");
            TextField answerField = new TextField(currentFlashcard.getAnswer());

            Label categoryLabel = new Label("Edit Category:");
            TextField categoryField = new TextField(currentFlashcard.getCategory());

            // Create a layout for the dialog
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(questionLabel, 0, 0);
            grid.add(questionField, 1, 0);
            grid.add(answerLabel, 0, 1);
            grid.add(answerField, 1, 1);
            grid.add(categoryLabel, 0, 2);
            grid.add(categoryField, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Add "Done" and "Cancel" buttons to the dialog
            ButtonType doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(doneButton, cancelButton);
            
            // Handle the user's input
            dialog.showAndWait().ifPresent(response -> {
                if (response == doneButton) 
                {
                    // Validate if any field is empty
                    if (questionField.getText().isEmpty() || answerField.getText().isEmpty() || categoryField.getText().isEmpty()) 
                    {
                        // Show an error alert if any field is empty
                        showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields before saving.");
                        return;  // Exit the method, do not proceed with updating the flashcard
                    }

                    // Update the flashcard details
                    currentFlashcard.setQuestion(questionField.getText());
                    currentFlashcard.setAnswer(answerField.getText());
                    currentFlashcard.setCategory(categoryField.getText());
                    
                    // Update flashcard in the database
                    flashcardManager.updateFlashcard(currentFlashcard, currentUser);

                    // Refresh the display to show updated flashcard
                    displayFlashcard(currentIndex);
                    showAlert(Alert.AlertType.INFORMATION, "Updated", "Flashcard updated successfully.");
                }
            });
        } 
        else 
        {
            showAlert(Alert.AlertType.ERROR, "Error", "No flashcard selected to edit.");
        }
    }

    @FXML
    private void deleteFlashcard() 
    {
        if (currentFlashcard != null) 
        {
            // Show confirmation dialog before deleting
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Flashcard");
            alert.setHeaderText("Delete this flashcard?");
            alert.setContentText("Are you sure you want to delete: " + currentFlashcard.getQuestion() + "?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) 
                {
                    // Delete from the database
                    flashcardManager.deleteFlashcardForUser(currentFlashcard, currentUser);
                    
                    // Remove the flashcard from the user's list and filtered list
                    currentUser.getFlashcards().remove(currentFlashcard);
                    filteredFlashcards.remove(currentFlashcard);
                    currentFlashcard = null; // Reset the current flashcard
                    currentIndex = Math.max(0, currentIndex - 1); // Move to the previous flashcard if possible
                    displayFlashcard(currentIndex); // Refresh display
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Flashcard deleted successfully.");
                }
            });
        } 
        else 
        {
            showAlert(Alert.AlertType.ERROR, "Error", "No flashcard selected to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

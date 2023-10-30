package quoridor.view;

import javafx.scene.control.Alert;

/**
 * Class to display error messages
 */
public class ErrorMessage {
    /**
     * Method to display an error message
     * @param txt
     */
    public static void showAlert(String txt) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null); // No header text
        alert.setContentText(txt); // Custom error message
        // Display the alert dialog
        alert.showAndWait();
    }
}

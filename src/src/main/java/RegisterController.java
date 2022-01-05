import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "much2less";
    static final String PASS = "1234qwer";

    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField emailRegister;
    @FXML
    private TextField passwordRegister;
    @FXML
    private Label errorBoxRegister;

    private String username;
    private String email;
    private String password;

    private StringBuilder error = new StringBuilder();

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void registerUser() throws IOException {
        username = usernameRegister.getText();
        email = emailRegister.getText();
        password = passwordRegister.getText();

        /*
        if(checkRegisterData(username, email, password)) {
            errorBoxRegister.setText(error.toString());
        }
         */

        //TODO implement code that should register the user in the new database
        //TODO implement an exception if the username or email is already existing in the database

    }

    //TODO checkRegisterData: Should check if Username is not too long, E-Mail has correct format and Password is strong enough
    //The Method should also build a String with all errors that occured when registering
    /*
        Example:
        Username too short
        Not a real E-Mail
        Password has no numbers
     */
/*
    private boolean checkRegisterData(String username, String email, String password) {
    }
*/

}

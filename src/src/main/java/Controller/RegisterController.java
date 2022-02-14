package Controller;

import Object.DBData;
import Object.HashedPassword;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;

/**
 * This class controls the registration of users.
 * It also checks if the Input data is legal for upload
 * If the registration is canceled you will be switched back to the login screen
 */

public class RegisterController {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = DBData.getDBUser();
    static final String PASS = DBData.getDBPassword();
    static final String QUERY = "INSERT INTO login (username,email,password,admin) VALUES (?, ?, ?, 0)";

    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField emailRegister;
    @FXML
    private TextField passwordRegister;

    private final StringBuilder error = new StringBuilder();

    /**
     * This method is for switching the screen to login after clicking register button
     */

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method gets the input of the TextField and uploads it to the database
     * It also uses isValidData() to check if the input is legal
     * @param actionEvent to change the screen by clicking a button
     */

    public void registerUser(javafx.event.ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        String username = usernameRegister.getText();
        String email = emailRegister.getText();
        String password = passwordRegister.getText();

        System.out.println(isValidData(username, email, password));

        if(isValidData(username, email, password)) {
            if (!checkUser(email)) {
                HashedPassword hashedPassword = new HashedPassword(password);
                String hash = hashedPassword.getHashString();


                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement stmt = conn.prepareStatement(QUERY)
                ) {
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, hash);

                    stmt.executeUpdate();

                    System.out.println("Success!");
                    switchToLogin(actionEvent);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * This method checks if the user is already registered
     * @param email email address of the user who wants to register
     * @return boolean true if user is already registered
     */

    public boolean checkUser(String email) {
        boolean usernameExists = false;

        try
        {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            PreparedStatement stmt = conn.prepareStatement("select * from login where email = ? ");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            String usernameCounter;
            if(rs.next())
            {
                usernameCounter = rs.getString("email");
                if(usernameCounter.equals(email))
                {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("ERROR");
                    errorAlert.setContentText("E-Mail already exists!");
                    errorAlert.showAndWait();
                    usernameExists = true;

                    usernameRegister.clear();
                    emailRegister.clear();
                    passwordRegister.clear();
                }

            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception: "+ e);
        }

        return usernameExists;
    }

    /**
     * This method is for checking if the input from the user is legal after our own standards
     * It checks if the username is at least 5 characters long, not longer than 29 characters and doesn't include special characters
     * It checks if the email is in the correct format
     * It checks if the password contains at least one special character and no white spaces
     * Finally if an input is illegal, an alert appears which prints all broken rules
     * @param username chose username of user
     * @param email chose email of user
     * @param password chose password from user
     * @return returns false if username, email or password is illegal
     */

    private boolean isValidData(String username, String email, String password) {
        boolean validData = true;

        String regex1 = "^[A-Za-z0-9]{5,29}$";
        String regex2 = "^(.+)@(.+)$";
        String regex3 = "^(?=.*[0-9])" //at least one digit/lower and upper case alphabet must occur
                + "(?=.*[a-z])(?=.*[A-Z])" // special character at least once, white spaces not allowed
                + "(?=.*[@#$%^&+=.])"
                + "(?=\\S+$).{6,20}$";

        StringBuilder errorMsg = new StringBuilder();

        if(!username.matches(regex1)) {
            errorMsg.append("Invalid username\n");

            usernameRegister.clear();
            emailRegister.clear();
            passwordRegister.clear();
            validData = false;
        }
        if(!email.matches(regex2)){
            errorMsg.append("Invalid email\n");

            usernameRegister.clear();
            emailRegister.clear();
            passwordRegister.clear();
            validData = false;

        }
/*
        if(!password.matches(regex3)) {
            errorMsg.append("Password has no correct form (At least one upper and lowercase alphabet, one special character and between 6-20 characters");

            usernameRegister.clear();
            emailRegister.clear();
            passwordRegister.clear();
            validData = false;

        }
*/

        if(!validData) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR");
            errorAlert.setContentText(errorMsg.toString());
            errorAlert.showAndWait();

        }

        return validData;
    }
}

package Controller;

import Object.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "root";
    static final String PASS = "Passwort123";
    static final String QUERY = "INSERT INTO login (username,email,password,admin) VALUES (?, ?, ?, 0)";

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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void registerUser(javafx.event.ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException, SQLException {
        username = usernameRegister.getText();
        email = emailRegister.getText();
        password = passwordRegister.getText();

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
                    errorAlert.setContentText("Process failed, please register again");
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
            System.out.println("SQL Exception: "+ e.toString());
        }

        return usernameExists;
    }



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
        if(!password.matches(regex3)) {
            errorMsg.append("Password has no correct form (At least one upper and lowercase alphabet, one special character and between 6-20 characters");

            usernameRegister.clear();
            emailRegister.clear();
            passwordRegister.clear();
            validData = false;

        }

        if(!validData) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR");
            errorAlert.setContentText(errorMsg.toString());
            errorAlert.showAndWait();

        }

        return validData;
    }
}

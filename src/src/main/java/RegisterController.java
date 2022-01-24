import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "much2less";
    static final String PASS = "1234qwer";
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void registerUser(javafx.event.ActionEvent actionEvent) throws IOException,  NoSuchAlgorithmException {
        username = usernameRegister.getText();
        email = emailRegister.getText();
        password = passwordRegister.getText();

        /*
        if(checkRegisterData(username, email, password)) {
            errorBoxRegister.setText("");
            errorBoxRegister.setText(error.toString());
        }
         */

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
        boolean return_val = false;

        if (username.length() < 4) {
            return_val = true;
            error.append("Username is too short!\n");
        }
        if (username.length() >= 100) {
            return_val = true;
            error.append("Username is too long!\n");
        }

        Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(username);
        boolean res = m.find();
        if (res) {
            return_val = true;
            error.append("Username has special characters!\n");
        }



        return return_val;
    }

*/
}

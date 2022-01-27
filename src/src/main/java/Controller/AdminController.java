package Controller;

import Object.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "much2less";
    static final String PASS = "1234qwer";
    static final String SELECT_FROM_LOGIN = "SELECT * FROM login";
    static final String DELETE_USER = "DELETE FROM login WHERE id = ?";

    private final ArrayList<User> userArrayList = new ArrayList<>();

    private int selectedUserIndex;

    //List declarations
    @FXML
    private ListView<String> userList;

    //Button declarations
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelAdmin;
    @FXML
    private Button submitAdmin;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("admin.fxml"));

        selectFromLogin();
    }

    public void selectFromLogin() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(SELECT_FROM_LOGIN)
        ) {
            ResultSet rs = stmt.executeQuery();

            try {
                userList.getItems().clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Saving Users from the database in an ArrayList
            while (rs.next()) {
                userArrayList.add(new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5)));
            }

            //Builds a string with information from every user
            for (int i = 0; i < userArrayList.size(); i++) {
                userList.getItems().add(
                        userArrayList.get(i).getId() + " "
                        + userArrayList.get(i).getUsername() + " "
                        + userArrayList.get(i).getEmail() + " "
                        + userArrayList.get(i).getAdmin() + " "
                        + userArrayList.get(i).getPassword());
            }

            userList.setOnMouseClicked(event -> selectedUserIndex = userList.getSelectionModel().getSelectedIndex());


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteUser(int index) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)
        ) {
            stmt.setInt(1, userArrayList.get(index).getId());
            stmt.executeQuery();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }

    public void confirmationEvent(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this User?");
        alert.show();
        //Handles the confirmation of the Confirmation Prompt
        alert.setOnCloseRequest(event -> {
                try {
                    deleteUser(selectedUserIndex);
                    selectFromLogin();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("delete");
            }
        );
    }

}

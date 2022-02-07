package Controller;

import Object.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class AdminController implements Initializable {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "much2less";
    static final String PASS = "1234qwer";
    static final String SELECT_FROM_LOGIN = "SELECT * FROM login";
    static final String DELETE_USER = "DELETE FROM login WHERE id = ?";
    static final String EDIT_USER = "UPDATE login SET username = ?, email = ?, admin = ? WHERE id = ?";

    private final ArrayList<User> userArrayList = new ArrayList<>();

    static private User selectedUser;
    private int selectedUserIndex;

    //List declarations
    @FXML
    private ListView<String> userListView;

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
                userListView.getItems().add(
                        userArrayList.get(i).getId() + " "
                        + userArrayList.get(i).getUsername() + " "
                        + userArrayList.get(i).getEmail() + " "
                        + userArrayList.get(i).getAdmin() + " "
                        + userArrayList.get(i).getPassword());
            }

            userListView.setOnMouseClicked(event -> {
                selectedUser = userArrayList.get(userListView.getSelectionModel().getSelectedIndex());
                selectedUserIndex = userListView.getSelectionModel().getSelectedIndex();
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteUser(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)
        ) {
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();

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
                    deleteUser(selectedUser);
                    userListView.getItems().remove(selectedUserIndex);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("delete");
            }
        );
    }

    private Stage stage;
    private Scene scene;

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Register");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void editUser() {
        if(userListView.getSelectionModel().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR");
            errorAlert.setContentText("You have to select a user!");
            errorAlert.showAndWait();
        }

        else {
            Dialog<AdminController> editDialog = new Dialog<>();
            //Get Username of edited User and set as a title
            editDialog.setTitle(selectedUser.getUsername());

            editDialog.setHeaderText("Edit the data of the User:" + selectedUser.getUsername());

            //Setting the Text fields to edit the user
            Label usernameLabel = new Label("Username: ");
            Label emailLabel = new Label("E-Mail: ");
            Label adminLabel = new Label("Admin Status: ");
            TextField usernameField = new TextField();
            TextField emailField = new TextField();
            TextField adminField = new TextField();

            //Adds a grid to add the text fields and labels to the content field of the dialog
            GridPane gridPane = new GridPane();
            gridPane.add(usernameLabel, 1, 1);
            gridPane.add(usernameField, 2, 1);
            gridPane.add(emailLabel, 1, 2);
            gridPane.add(emailField, 2, 2);
            gridPane.add(adminLabel, 1, 3);
            gridPane.add(adminField, 2, 3);
            editDialog.getDialogPane().setContent(gridPane);

            //Declares and adds the ButtonType for a callback, when the ok button is clicked
            ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            editDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
            editDialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    if (
                            //Checks if Fields are not empty
                            !(
                            usernameField.getText() == null ||
                            emailField.getText() == null ||
                            adminField.getText() == null
                            )
                    ) {
                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                             PreparedStatement stmt = conn.prepareStatement(EDIT_USER)
                        ) {
                            stmt.setString(1, usernameField.getText());
                            stmt.setString(2, emailField.getText());
                            stmt.setInt(3, Integer.parseInt(adminField.getText()));
                            stmt.setInt(4, selectedUser.getId());
                            stmt.executeUpdate();

                            //Updates the user in the current Arraylist with the text field content
                            userArrayList.get(selectedUserIndex).setUsername(usernameField.getText());
                            userArrayList.get(selectedUserIndex).setEmail(emailField.getText());
                            userArrayList.get(selectedUserIndex).setAdmin(Integer.parseInt(adminField.getText()));
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
                return null;
            });

            Optional<AdminController> result = editDialog.showAndWait();
            result.ifPresent(System.out::println);


        }

    }
}

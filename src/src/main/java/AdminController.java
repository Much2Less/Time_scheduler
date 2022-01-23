import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.fxml.FXMLLoader.load;

public class AdminController {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "much2less";
    static final String PASS = "1234qwer";
    static final String QUERY = "SELECT * FROM login";

    ArrayList<User> userArrayList;

    //List declarations
    @FXML
    private static ListView<String> usernameList;
    @FXML
    private static ListView<String> emailList;
    @FXML
    private static ListView<String> passwordList;
    @FXML
    private static ListView<Integer> adminList;
    @FXML
    private static ListView<Button> buttonList;
    @FXML
    private static ListView<Integer> idList;

    //Button declarations
    @FXML
    private Button cancelAdmin;
    @FXML
    private Button submitAdmin;

    public static void setup() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(QUERY)
        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                idList.getItems().add(rs.getInt(1));
                usernameList.getItems().add(rs.getString(2));
                emailList.getItems().add(rs.getString(3));
                passwordList.getItems().add(rs.getString(4));
                adminList.getItems().add(rs.getInt(5));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

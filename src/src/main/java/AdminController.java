import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    //List declarations
    @FXML
    private ListView<Integer> idList;
    @FXML
    private ListView<String> usernameList;
    @FXML
    private ListView<String> emailList;
    @FXML
    private ListView<String> passwordList;
    @FXML
    private ListView<Integer> adminList;
    @FXML
    private ListView<Button> buttonList;

    //Button declarations
    @FXML
    private Button cancelAdmin;
    @FXML
    private Button submitAdmin;

    public static void setup(FXMLLoader loader) {
        AdminController controller = loader.getController();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(QUERY)
        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                controller.idList.getItems().add(rs.getInt(1));
                controller.usernameList.getItems().add(rs.getString(2));
                controller.emailList.getItems().add(rs.getString(3));
                controller.passwordList.getItems().add(rs.getString(4));
                controller.adminList.getItems().add(rs.getInt(5));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

import Controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import Object.Appointments;
import Object.User;

public class EditDeleteScreen implements Initializable{

    private Stage stage;
    private Scene scene;

    @FXML
    private Label searchText;
    @FXML
    private ListView<String> myListview;
    @FXML
    private Button  SearchButton;
    @FXML
    private Button cancelButton;

    private final ArrayList<Appointments> appointmentsArrayList = new ArrayList<>();

    private int selectedUserIndex;
    private int userid;


    public User currentUser = LoginController.currentUser;


    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "much2less";
    static final String PASS = "1234qwer";
    static final String QUERY = "SELECT `name`,`date`,`participants`,`reminder` FROM appointment Where  userid = ?";
    static final String SELECT_FROM_Appointment = "SELECT name,date,participants,reminder FROM appointment";




    public void switchToOptions(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("optionMenu.fxml")));
        this.stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        this.stage.setTitle("Welcome to Time Scheduler");
        this.scene = new Scene(root);
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectFromAppointment();


    }

    public void selectFromAppointment(){


        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(QUERY)
        ) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();


            //Saving Users from the database in an ArrayList
            while (rs.next()) {
                appointmentsArrayList.add(new Appointments(
                        rs.getString(1),
                        Date.valueOf(rs.getString(2)),
                        rs.getString(3),
                        rs.getString(4)));
            }


            //Builds a string with information from every user
            for (Appointments appointments : appointmentsArrayList) {
                myListview.getItems().add(
                        appointments.getName() + " "
                                + appointments.getDate() + " "
                                + appointments.getParticipants() + " "
                                + appointments.getReminder());
            }

            myListview.setOnMouseClicked(event -> selectedUserIndex = myListview.getSelectionModel().getSelectedIndex());


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}



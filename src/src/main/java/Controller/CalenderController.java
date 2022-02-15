package Controller;

import Object.DBData;
import Object.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

/*
TODO
When selecting the time of appointment or the duration:
- It shouldn't be possible to input a number greater than 23 hours or 59 minutes
- When typing a number between 0-9 the input should append a zero before the number (Example: instead of 1 it should be converted to 01)

Participants:
- Participants need multiple input fields for multiple inputs. An add button is needed to add more input fields!
 */

/**
 * This class controls the create appointment screen
 */

public class CalenderController implements Initializable {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = DBData.getDBUser();
    static final String PASS = DBData.getDBPassword();
    static final String QUERY = "INSERT INTO appointment (name,date,start,startminutes,end,endminutes,location,participants,priority,reminder,userid) VALUES (?,?,?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public User currentUser = LoginController.currentUser;

    @FXML
    public Button cancelButton;

    @FXML
    private TextField eventName;
    @FXML
    private DatePicker date;
    @FXML
    private TextField hour;
    @FXML
    private TextField minutes;
    @FXML
    private TextField durationHour;
    @FXML
    private TextField durationMinutes;
    @FXML
    private TextField listParticipants;
    @FXML
    private TextField reminder;
    @FXML
    private TextField location;
    @FXML
    private ChoiceBox<String> priority;
    @FXML
    private ChoiceBox<String> reminderbox;


    private DatePicker dateAppointment;
    //private ChoiceBox reminderboxAppointment;
    private final String[] option = {"High","Medium","Low"};
    private final String[] option2 = {"1 Week","3 Days","1 Hour","10 Minutes","1 Minute"};



    /**
     * This method is creates an appointment object by using the input inside the TextField and uploads the data to the database
     */

    public void creatAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        String eventNameAppointment = eventName.getText();
        LocalDate dateAppointment = date.getValue();
        String hourAppointment = hour.getText();
        String minutesAppointment = minutes.getText();
        String durationHourAppointment = durationHour.getText();
        String durationMinutesAppointment = durationMinutes.getText();
        String locationAppointment = location.getText();
        String listParticipantsAppointment = listParticipants.getText();
        String priorityAppointment = priority.getValue();
        String reminderAppointment = reminderbox.getValue();


        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(QUERY)
        ) {
            stmt.setString(1, eventNameAppointment);
            stmt.setString(2, String.valueOf(dateAppointment));
            stmt.setInt(3, Integer.parseInt(hourAppointment));
            stmt.setInt(4, Integer.parseInt(minutesAppointment));
            stmt.setInt(5, Integer.parseInt(durationHourAppointment));
            stmt.setInt(6, Integer.parseInt(durationMinutesAppointment));
            stmt.setString(7, locationAppointment);
            stmt.setString(8, listParticipantsAppointment);
            stmt.setString(9, priorityAppointment);
            stmt.setString(10, reminderAppointment);
            stmt.setInt(11, currentUser.getId());



            stmt.executeUpdate();
            switchToOptionMenu(actionEvent);
            System.out.println("Success!");
        }

    }

    /**
     * This method sends you back to the Option Menu
     */

    @FXML
    private void switchToOptionMenu(ActionEvent actionEvent) throws IOException {
        Parent root = load(Objects.requireNonNull(getClass().getClassLoader().getResource("optionMenu.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Welcome");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * initializes the screen
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        priority.getItems().addAll(option);
        reminderbox.getItems().addAll(option2);
    }

}

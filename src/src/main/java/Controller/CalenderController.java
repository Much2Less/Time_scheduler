package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import Object.User;

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
 * This class is for creating appointments and to save them in database
 */

public class CalenderController implements Initializable {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "root";
    static final String PASS = "Passwort123";
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
    private TextField eventort;
    @FXML
    private ChoiceBox<String> priority;
    @FXML
    private ChoiceBox<String> reminderbox;


    private String eventNameAppointment;
    private DatePicker dateAppointment;
    private String hourAppointment;
    private String minutesAppointment;
    private String durationHourAppointment;
    private String durationMinutesAppointment;
    private String listParticipantsAppointment;
    private String locationAppointment;
    private String reminderAppointment;
    private String priorityAppointment;
    //private ChoiceBox reminderboxAppointment;
    private String[] option = {"High","Medium","Low"};
    private String[] option2 = {"1 Week","3 Days","1 Hour","10 Minutes"};


            private Stage stage;
            private Scene scene;


    public CalenderController() {
    }

    /**
     * This method is for creating an appointment and to put the data into database
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */

    public void creatAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        eventNameAppointment = eventName.getText();
        LocalDate dateAppointment = date.getValue();
        hourAppointment = hour.getText();
        minutesAppointment = minutes.getText();
        durationHourAppointment = durationHour.getText();
        durationMinutesAppointment = durationMinutes.getText();
        locationAppointment = eventort.getText();
        listParticipantsAppointment = listParticipants.getText();
        priorityAppointment =priority.getValue();
        reminderAppointment= reminderbox.getValue();


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
     * This method is to click on the cancel button
     * @param actionEvent
     * @throws IOException
     */

    public void cancelButton(javafx.event.ActionEvent actionEvent) throws IOException {
        switchToOptionMenu(actionEvent);
    }

    /**
     * This method is to switch from create appointment screen to calender screen
     * @param actionEvent
     * @throws IOException
     */

    private void switchToOptionMenu(ActionEvent actionEvent) throws IOException {
        Parent root = load(Objects.requireNonNull(getClass().getClassLoader().getResource("optionMenu.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Welcome");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        priority.getItems().addAll(option);
        reminderbox.getItems().addAll(option2);
    }

}

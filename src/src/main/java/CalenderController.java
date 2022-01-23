import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import static javafx.fxml.FXMLLoader.load;

public class CalenderController {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "root";
    static final String PASS = "Prabin2468";
    static final String QUERY = "INSERT INTO appointment (name,date,start,startminutes,end,endminutes,location,participants,reminder) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";

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
    private CheckBox High;
    @FXML
    private CheckBox Medium;
    @FXML
    private CheckBox Low;
    @FXML
    private TextField threehours;


    private String eventNameAppointment;
    private DatePicker dateAppointment;
    private String hourAppointment;
    private String minutesAppointment;
    private String durationHourAppointment;
    private String durationMinutesAppointment;
    private String listParticipantsAppointment;
    private String locationAppointment;
    private String reminderAppointment;
    private CheckBox HighAppointment;
    private CheckBox MediumAppointment;
    private CheckBox LowAppointment;
    private String threehoursAppointment;
/*
    public void getDate(ActionEvent event) {
        LocalDate date = dateAppointment.getValue();
    }

*/
            private Stage stage;
            private Scene scene;

            public void switchToRegister(javafx.event.ActionEvent actionEvent) throws IOException {
            Parent root = load(Objects.requireNonNull(getClass().getResource("optionmenu.fxml")));
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Welcome");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            }

    public void creatAppointment(ActionEvent actionEvent) throws IOException, SQLException, NoSuchAlgorithmException {
        eventNameAppointment = eventName.getText();
         LocalDate dateAppointment = date.getValue();
        hourAppointment = hour.getText();
        minutesAppointment = minutes.getText();
        durationHourAppointment = durationHour.getText();
        durationMinutesAppointment = durationMinutes.getText();
        locationAppointment = eventort.getText();
        listParticipantsAppointment = listParticipants.getText();
        reminderAppointment= reminder.getText();


        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(QUERY);
        ) {
            stmt.setString(1, eventNameAppointment);
            stmt.setString(2, String.valueOf(dateAppointment));
            stmt.setString(3, hourAppointment);
            stmt.setString(4, minutesAppointment);
            stmt.setString(5, durationHourAppointment);
            stmt.setString(6, durationMinutesAppointment);
            stmt.setString(7, locationAppointment);
            stmt.setString(8, listParticipantsAppointment);
            stmt.setString(9, reminderAppointment);



            stmt.executeUpdate();
            switchToRegister(actionEvent);
            System.out.println("Success!");
        }

    }
}

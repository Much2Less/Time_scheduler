import Controller.AdminController;
import Controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import Object.*;

import javax.swing.text.Document;

/**
 * This class is for editing and deleting appointments from database
 */

public class EditDeleteScreen implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML
    private Label searchText;
    @FXML
    private ListView<String> appointmentListView;
    @FXML
    private Button SearchButton;
    @FXML
    private Button cancelButton;

    private final ArrayList<Appointment> appointmentArrayList = new ArrayList<>();

    private int selectedAppointmentIndex;
    private Appointment selectedAppointment;
    private int userid;


    public User currentUser = LoginController.currentUser;

    /**
     * Queries for database actions
     */

    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = DBData.getDBUser();
    static final String PASS = DBData.getDBPassword();
    //static final String SELECT_APPOINTMENT = "SELECT `name`,`date`,`participants`,`reminder` FROM appointment Where  userid = ?";
    static final String SELECT_APPOINTMENT = "SELECT * FROM appointment Where  userid = ?";
    static final String DELETE_APPOINTMENT = "DELETE FROM appointment WHERE id = ?";

    /**
     * This method sends you to the options screen
     */

    public void switchToOptions(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("optionMenu.fxml")));
        this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        this.stage.setTitle("Welcome to Time Scheduler");
        this.scene = new Scene(root);
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectFromAppointment();


    }

    /**
     * Connects with the database to download every appointment and
     * constructing every appointment as a unique object to store them into an ArrayList.
     * It then constructs a string for every appointment and adds it to the ListView.
     * To be able to select a appointment from the ListView, the method also creates an EventHandler.
     */

    public void selectFromAppointment() {


        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(SELECT_APPOINTMENT)
        ) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();


            //Saving Users from the database in an ArrayList
            while (rs.next()) {

                appointmentArrayList.add(new Appointment(
                        rs.getInt(1),
                        rs.getString(2),
                        Date.valueOf(rs.getString(3)),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11)
                ));
            }


            //Builds a string with information from every user
            for (Appointment appointment : appointmentArrayList) {
                appointmentListView.getItems().add(
                        appointment.getName() + " "
                                + appointment.getDate() + " "
                                + appointment.getParticipants() + " "
                                + appointment.getReminder());
            }

            appointmentListView.setOnMouseClicked(event -> {
                selectedAppointment = appointmentArrayList.get(appointmentListView.getSelectionModel().getSelectedIndex());
                selectedAppointmentIndex = appointmentListView.getSelectionModel().getSelectedIndex();
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is for deleting an appointment from database
     * @param appointment appointment created before and is stored in database
     */

    public void deleteAppointment(Appointment appointment) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(DELETE_APPOINTMENT)
        ) {
            stmt.setInt(1, appointment.getId());
            stmt.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }

    /**
     * Asks if you really want to delete the selected appointment
     */

    public void confirmationEvent() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Appointment?");
        alert.show();
        //Handles the confirmation of the Confirmation Prompt
        alert.setOnCloseRequest(event -> {
            try {
                deleteAppointment(selectedAppointment);
                appointmentListView.getItems().remove(selectedAppointmentIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("delete");
                }
        );
    }

    //TODO
    public void editAppointment() {
        if (appointmentListView.getSelectionModel().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR");
            errorAlert.setContentText("You have to select an appointment!");
            errorAlert.showAndWait();
        }


    }
}

package Controller;

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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import Object.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class is for editing and deleting appointments from database
 */
public class EditDeleteScreen implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML
    private Label showusername;
    @FXML
    private ListView<String> appointmentListView;

    private final ArrayList<Appointment> appointmentArrayList = new ArrayList<>();

    private int selectedAppointmentIndex;
    private Appointment selectedAppointment;


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
    static final String EDIT_APPOINTMENT = "UPDATE appointment SET name = ?, date = ?,start = ?, startminutes= ?, end = ?,endminutes= ?,location= ?,participants =?,priority = ?,reminder=?  WHERE id = ?";
    static final String TURN_OFF_REMINDER = "UPDATE appointment SET reminder_sent = 1 WHERE id = ?";


    public void switchToOptions(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getClassLoader().getResource("optionMenu.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Welcome to Time Scheduler");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void displayName(){
        showusername.setText("All the Appointments from: "+currentUser.getUsername());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectFromAppointment();
        displayName();

        appointmentListView.setOnMouseClicked(event -> {
            selectedAppointmentIndex = appointmentListView.getSelectionModel().getSelectedIndex();
            selectedAppointment = appointmentArrayList.get(selectedAppointmentIndex);
        });
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


            //Saving Appointment from the database in an ArrayList
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
                        rs.getString(11),
                        rs.getInt("reminder_sent")
                ));
            }


            //Builds a string with information from that specific user Appointment
            for (Appointment appointment : appointmentArrayList) {
                appointmentListView.getItems().add(
                        appointment.getName()
                                + " Date: " + appointment.getDate()
                                + " Participants: " + appointment.getParticipants()
                                + " Starts at: " + appointment.getStartHours() +":" + appointment.getStartMinutes()+" Uhr"
                                + " Reminder " + appointment.getReminder());
            }




        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is for deleting an appointment from database
     *
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

    public void confirmationEvent() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Appointment?");
        alert.show();
        //Handles the confirmation of the Confirmation Prompt
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        alert.getDialogPane().getButtonTypes().set(0, buttonTypeOk);
        alert.setResultConverter(param -> {
            if (param == buttonTypeOk) {
                try {
                    deleteAppointment(selectedAppointment);
                    appointmentArrayList.remove(selectedAppointmentIndex);
                    appointmentListView.getItems().remove(selectedAppointmentIndex);
                    sendDeleteMail(selectedAppointment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("delete");
            }

                return null;
        });
    }

    private void sendEditMail(Appointment appointment) {
        String recipient = currentUser.getEmail();
        String sender = "anonymerrotertyp@gmail.com";
        String password = "Emfm1912,";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipients(Message.RecipientType.TO, recipient);
            message.setSubject("Reminder of your appointment");
            message.setText("Hi, " + currentUser.getUsername() + "\n" +
                    "You edited the appointment"+ appointment.getName() + "\n" +
                    "Here is the edited appointment! \n" +
                    "Name: " + appointment.getName() + "\n" +
                    "Date and Starting Time: " + appointment.getDate() + " " + appointment.getStartTimeFormatted() + "\n" +
                    "Duration: " + appointment.getEndTimeFormatted() + "\n" +
                    "Location: " + appointment.getLocation() + "\n" +
                    "Participants: " + appointment.getParticipants() + "\n" +
                    "Priority: " + appointment.getPriority() + "\n" +
                    "Reminder: " + appointment.getReminder());
            Transport.send(message);
            System.out.println("Mail successfully sent");


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendDeleteMail(Appointment appointment) {
        String recipient = currentUser.getEmail();
        String sender = "anonymerrotertyp@gmail.com";
        String password = "Emfm1912,";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipients(Message.RecipientType.TO, recipient);
            message.setSubject("Reminder of your appointment");
            message.setText("Hi, " + currentUser.getUsername() + "\n" +
                    "You deleted the appointment: "+ appointment.getName() + "\n");
            Transport.send(message);
            System.out.println("Mail successfully sent");


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void editAppointment() {
            if (appointmentListView.getSelectionModel().isEmpty()) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("ERROR");
                errorAlert.setContentText("You have to select a Appointment!");
                errorAlert.showAndWait();
            } else {
                Dialog<EditDeleteScreen> editDeleteScreenDialog = new Dialog<>();
                editDeleteScreenDialog.setTitle("Editing Appointment: " + selectedAppointment.getName());

                editDeleteScreenDialog.setHeaderText("Edit the Appointment: " + selectedAppointment.getName());

                Label AppointmentLabel = new Label("Appointment name: ");
                Label Date = new Label("Date: ");
                Label startLabel = new Label("StartHour: ");
                Label startminutesLabel = new Label("StartMinutes: ");
                Label endLabel = new Label("Duration Hour: ");
                Label endminutesLabel = new Label("Duration Minutes: ");
                Label locationLabel = new Label("Location: ");
                Label ParticipantsLabel = new Label("Participants: ");
                Label PriorityLabel = new Label("Priority:");
                Label ReminderLabel = new Label("Reminder:");

                TextField AppointmentField = new TextField();
                DatePicker DateField = new DatePicker();
                TextField startField = new TextField();
                TextField startminutesField = new TextField();
                TextField endField = new TextField();
                TextField endminutesField = new TextField();
                TextField locationField = new TextField();
                TextField ParticipantsField = new TextField();
                ChoiceBox<String> PriorityField = new ChoiceBox<String>();

                PriorityField.getItems().add("High");
                PriorityField.getItems().add("Medium");
                PriorityField.getItems().add("Low");

                ChoiceBox<String> ReminderField = new ChoiceBox<>();
                ReminderField.getItems().add("1 Week");
                ReminderField.getItems().add("3 Days");
                ReminderField.getItems().add("1 Hour");
                ReminderField.getItems().add("10 Minutes");


                GridPane gridPane = new GridPane();
                gridPane.add(AppointmentLabel, 1, 1);
                gridPane.add(AppointmentField, 2, 1);
                gridPane.add(Date, 1, 2);
                gridPane.add(DateField, 2, 2);
                gridPane.add(startLabel, 1, 3);
                gridPane.add(startField, 2, 3);
                gridPane.add(startminutesLabel, 1, 4);
                gridPane.add(startminutesField, 2, 4);
                gridPane.add(endLabel, 1, 5);
                gridPane.add(endField, 2, 5);
                gridPane.add(endminutesLabel, 1, 6);
                gridPane.add(endminutesField, 2, 6);
                gridPane.add(locationLabel, 1, 7);
                gridPane.add(locationField, 2, 7);
                gridPane.add(ParticipantsLabel, 1, 8);
                gridPane.add(ParticipantsField, 2, 8);
                gridPane.add(PriorityLabel, 1, 9);
                gridPane.add(PriorityField, 2, 9);
                gridPane.add(ReminderLabel, 1, 10);
                gridPane.add(ReminderField, 2, 10);

                editDeleteScreenDialog.getDialogPane().setContent(gridPane);

                ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                editDeleteScreenDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
                editDeleteScreenDialog.setResultConverter(param -> {
                    if (param == buttonTypeOk) {
                        if (
                            //Checks if Fields are not empty
                                !(
                                        AppointmentField.getText() == null ||
                                                DateField.getValue() == null ||
                                                startField.getText() == null ||
                                                startminutesField.getText() == null ||
                                                endField.getText() == null ||
                                                endminutesField.getText() == null ||
                                                locationField.getText() == null ||
                                                ParticipantsField.getText() == null ||
                                                PriorityField.getValue() == null ||
                                                ReminderField.getValue() == null

                                )
                        ) {
                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                 PreparedStatement stmt = conn.prepareStatement(EDIT_APPOINTMENT)
                            ) {
                                stmt.setString(1, AppointmentField.getText());
                                stmt.setString(2, String.valueOf(DateField.getValue()));
                                stmt.setString(3, startField.getText());
                                stmt.setString(4, startminutesField.getText());
                                stmt.setString(5, endField.getText());
                                stmt.setString(6, endminutesField.getText());
                                stmt.setString(7, locationField.getText());
                                stmt.setString(8, ParticipantsField.getText());
                                stmt.setString(9, String.valueOf(PriorityField.getValue()));
                                stmt.setString(10, String.valueOf(ReminderField.getValue()));
                                stmt.setInt(11, selectedAppointment.getId());
                                stmt.executeUpdate();

                                //Updates the user Appointments in the current Arraylist with the text field content
                                appointmentArrayList.get(selectedAppointmentIndex).setName(AppointmentField.getText());
                                appointmentArrayList.get(selectedAppointmentIndex).setDate(DateField.getValue());
                                appointmentArrayList.get(selectedAppointmentIndex).setStartHours(Integer.parseInt(startField.getText()));
                                appointmentArrayList.get(selectedAppointmentIndex).setStartMinutes(Integer.parseInt(startminutesField.getText()));
                                appointmentArrayList.get(selectedAppointmentIndex).setEndHours(Integer.parseInt(endField.getText()));
                                appointmentArrayList.get(selectedAppointmentIndex).setEndMinutes(Integer.parseInt(endminutesField.getText()));
                                appointmentArrayList.get(selectedAppointmentIndex).setLocation(locationField.getText());
                                appointmentArrayList.get(selectedAppointmentIndex).setParticipants(ParticipantsField.getText());
                                appointmentArrayList.get(selectedAppointmentIndex).setReminder(String.valueOf(PriorityField.getValue()));
                                appointmentArrayList.get(selectedAppointmentIndex).setReminder(String.valueOf(ReminderField.getValue()));

                                appointmentListView.getItems().set(selectedAppointmentIndex, AppointmentField.getText()
                                        + " Date: " + DateField.getValue()
                                        + " Participants: " + ParticipantsField.getText()
                                        + " Starts at: " + startField.getText() +":" + startminutesField.getText() +" Uhr" +
                                        " Reminder " + ReminderField.getValue());
                            } catch (SQLException throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    }
                    return null;


                });

                Optional<EditDeleteScreen> result = editDeleteScreenDialog.showAndWait();
                result.ifPresent(System.out::println);

                sendEditMail(appointmentArrayList.get(selectedAppointmentIndex));
            }


        }
    }





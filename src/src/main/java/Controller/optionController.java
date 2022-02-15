package Controller;

import Object.Appointment;
import Object.DBData;
import Object.User;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static Controller.LoginController.currentUser;
import static Controller.optionController.*;
import static javafx.fxml.FXMLLoader.load;

/**
 * This class is for controlling the option menu.
 * In the option menu you can get to the create or edit/delete appointment screen.
 * You can also export all appointments into a pdf.
 */


public class optionController implements Initializable {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = DBData.getDBUser();
    static final String PASS = DBData.getDBPassword();
    static final String SELECT_APPOINTMENT = "SELECT * FROM appointment Where userid = ?";

    private Stage stage;
    private Scene scene;


    public User currentUser = LoginController.currentUser;
    public ArrayList<Appointment> appointmentArrayList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(SELECT_APPOINTMENT)) {
            stmt.setInt(1, currentUser.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointmentArrayList.add(new Appointment(
                        //id
                        rs.getInt(1),
                        //name
                        rs.getString(2),
                        //date
                        Date.valueOf(rs.getString(3)),
                        //startHours
                        rs.getInt(4),
                        //startMinutes
                        rs.getInt(5),
                        //endHours
                        rs.getInt(6),
                        //endMinutes
                        rs.getInt(7),
                        //location
                        rs.getString(8),
                        //participants
                        rs.getString(9),
                        //priority
                        rs.getString(10),
                        //reminder
                        rs.getString(11),
                        //reminder_sent
                        rs.getInt(13)
                ));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            setTimer();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sends you back to the login screen
     */

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method sends you to the creating screen for a new appointment
     */

    public void switchToCalender(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = load(Objects.requireNonNull(getClass().getClassLoader().getResource("calender.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Welcome to Time Scheduler");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method sends you to the edit/delete screen
     */

    public void switchToEditDeleteScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("editDeleteScreen.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Edit or Delete your Meetings ");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is exporting every appointment from the currently
     * logged on user into a pdf file.
     */


    public void exportPdf() throws FileNotFoundException, DocumentException {

        Document document = new Document();

        //Insert own path
        //PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\User\\Desktop\\Test.pdf"));

        document.open();
        document.add(new Paragraph("Appointments"));
        document.add(new Chunk(" "));

        for (Appointment appointment: appointmentArrayList) {
            Paragraph paragraph = new Paragraph(
                    "  userID: "+currentUser.getId()+ "\n"
                    +"  title: "+appointment.getName()+ "\n"
                    + "  date: "+appointment.getDate()+ "\n"
                    +"  Start time: "+appointment.getStartHours()+ ":"+appointment.getStartMinutes()+ "\n"
                    +"  end time: "+appointment.getEndHours()+ ":"+appointment.getEndMinutes()+ "\n"
                    +"  Location: "+appointment.getLocation()+ "\n"
                    +"  participants: "+appointment.getParticipants()+"\n"
                    + "  priority: "+appointment.getPriority()+ "\n"
                    +"  reminder: "+appointment.getReminder());
            document.add(paragraph);
            document.add(new Paragraph(" "));
            System.out.println("Opening Document");
        }

        document.close();
        System.out.println("Finished");


    }

    public void setTimer() throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date;
        Timer timer = new Timer();

        for (Appointment appointment : appointmentArrayList) {
            switch (appointment.getReminder()) {
                case "1 Week":
                    date = dateFormat.parse(appointment.getDate().toLocalDate().minusWeeks(1) + " " + appointment.getStartTimeFormatted()+":00");

                    timer.schedule(new Sender(appointment), date);
                    break;

                case "3 Days":
                    date = dateFormat.parse(appointment.getDate().toLocalDate().minusDays(3) + " " + appointment.getStartTimeFormatted()+":00");

                    timer.schedule(new Sender(appointment), date);
                    break;

                case "1 Hour":
                    date = dateFormat.parse(appointment.getDate().toLocalDate() + " " + appointment.getStartTimeFormatted(1, 0)+":00");

                    timer.schedule(new Sender(appointment), date);
                    break;

                case "10 Minutes":
                    date = dateFormat.parse(appointment.getDate().toLocalDate() + " " + appointment.getStartTimeFormatted(0, 10)+":00");

                    timer.schedule(new Sender(appointment), date);
                    break;

                case "1 Minute":
                    date = dateFormat.parse(appointment.getDate().toLocalDate() + " " + appointment.getStartTimeFormatted(0, 1)+":00");

                    timer.schedule(new Sender(appointment), date);
                    break;

                default:
                    System.out.println("Unknown Case");
            }
        }
    }
}

class Sender extends TimerTask {

    private static final String TURN_OFF_REMINDER = "UPDATE appointment SET reminder_sent = 1 WHERE id = ?";
    private final Appointment appointment;

    public Sender(Appointment appointment) {
        this.appointment = appointment;
    }

    public void run() {

        //If appointment has not already set a reminder, then send a reminder
        if (!appointment.isReminder_sent()) {

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
                            return new PasswordAuthentication(sender,password);
                        }
                    });
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(sender));
                message.addRecipients(Message.RecipientType.TO, recipient);
                message.setSubject("Reminder of your appointment");
                //message.setText("This is just a test!");
                message.setText("Hi, " + currentUser.getUsername() + "\n" +
                        "This is a friendly reminder that your appointment is due soon \n" +
                        "Here you have all the Information about the appointment:\n" +
                        "Name: " + appointment.getName() + "\n" +
                        "Date and Starting Time: " + appointment.getDate() + " " + appointment.getStartTimeFormatted() + "\n" +
                        "Duration: " + appointment.getEndTimeFormatted() + "\n" +
                        "Location: " + appointment.getLocation() + "\n" +
                        "Participants: " + appointment.getParticipants() + "\n" +
                        "Priority: " + appointment.getPriority() + "\n" +
                        "Reminder: " + appointment.getReminder());
                Transport.send(message);
                System.out.println("Mail successfully sent");

                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                         PreparedStatement stmt = conn.prepareStatement(TURN_OFF_REMINDER)) {
                        stmt.setInt(1, appointment.getId());
                        stmt.executeUpdate();
                }

            } catch (MessagingException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

package Object;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimerTask;

import static Controller.LoginController.currentUser;
import static Controller.optionController.*;

/**
 * This class sends a reminder email at a specific date and time
 */
public class Sender extends TimerTask {

    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = DBData.getDBUser();
    static final String PASS = DBData.getDBPassword();
    private static final String TURN_OFF_REMINDER = "UPDATE appointment SET reminder_sent = 1 WHERE id = ?";

    private final Appointment appointment;

    public Sender(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Constructs a reminder email and uses a gmail account to send it.
     * It also changes a value in the database to prevent the same reminder getting spammed
     */
    public void run() {

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
        //If appointment has not already set a reminder, then send a reminder
        if (!appointment.isReminder_sent()) {
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

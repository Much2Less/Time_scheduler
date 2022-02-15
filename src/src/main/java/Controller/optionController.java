package Controller;

import Object.Appointment;
import Object.DBData;
import Object.User;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.fxml.FXMLLoader.load;

/**
 * This class is for controlling the option menu.
 * In the option menu you can get to the create or edit/delete appointment screen.
 * You can also export all appointments into a pdf.
 */


public class optionController {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = DBData.getDBUser();
    static final String PASS = DBData.getDBPassword();
    static final String SELECT_APPOINTMENT = "SELECT * FROM appointment Where userid = ?";

    private Stage stage;
    private Scene scene;


    public User currentUser = LoginController.currentUser;
    private ArrayList<Appointment> appointmentArrayList = new ArrayList<>();

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
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\User\\Desktop\\Test.pdf"));
        document.open();
        document.add(new Paragraph("Appointments"));
        document.add(new Chunk(" "));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(SELECT_APPOINTMENT)) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //" "+rs.getInt("id")+ " "+rs.getString("name")+ " "+Date.valueOf(rs.getString("date")+ " "+rs.getInt("start")+ " "+rs.getInt("startminutes")+ " "+rs.getInt("end")+ " "+rs.getInt("endminutes")+ " "+rs.getString("location")+ " "+rs.getString("participants")+ " "+rs.getString("priority")+ " "+rs.getString("reminder")
                int userid = rs.getInt("userid");
                String name = rs.getString("name");
                Date date = Date.valueOf(rs.getString("date"));
                int start = rs.getInt("start");
                int startminutes = rs.getInt("startminutes");
                int end = rs.getInt("end");
                int endminutes = rs.getInt("endminutes");
                String location = rs.getString("location");
                String participants = rs.getString("participants");
                String priority = rs.getString("priority");
                String reminder = rs.getString("reminder");
                Paragraph paragraph = new Paragraph(
                        "  userID: "+userid+ "\n"
                        +"  title: "+name+ "\n"
                        + "  date: "+date+ "\n"
                        +"  starttime: "+start+ ":"+startminutes+ "\n"
                        +"  endtime: "+end+ ":"+endminutes+ "\n"
                        +"  Location: "+location+ "\n"
                        +"  participants: "+participants+"\n"
                        + "  priority: "+priority+ "\n"
                        +"  reminder: "+reminder);
                document.add(paragraph);
                document.add(new Paragraph(" "));
                System.out.println("Opening Document");
                Appointment a = new Appointment(
                        userid,
                        name,
                        date,
                        start,
                        startminutes,
                        end,
                        endminutes,
                        location,
                        participants,
                        priority,
                        reminder

                );
                appointmentArrayList.add(a);
            }

            document.close();
            System.out.println("Finished");


        } catch (SQLException | DocumentException e) {
            e.printStackTrace();
                }
    }
}

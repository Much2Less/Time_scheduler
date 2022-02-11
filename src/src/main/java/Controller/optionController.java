package Controller;
import Object.*;
import com.itextpdf.text.*;
import com.sun.javafx.font.FontFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.event.DocumentListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class is for handling after the login part, the user is able
 * to create appointments, edit/delete them or to export the appointments in a pdf file
 */


public class optionController {
    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "root";
    static final String PASS = "Passwort123";
    static final String SELECT_APPOINTMENT = "SELECT * FROM appointment Where userid = ?";

    private Stage stage;
    private Scene scene;
    private int userid;


    public User currentUser = LoginController.currentUser;
    private ArrayList<Appointment> appointmentArrayList = new ArrayList<>();

    /**
     * This method is for the start screen
     * @param actionEvent
     * @throws IOException
     */

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is for switching to calender menu is username and password were filled in correctly
     * @param actionEvent
     * @throws IOException
     */

    public void switchToCalender(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("calender.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Welcome to Time Scheduler");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is for switching to edit or delete screen to manipulate appointments
     * @param actionEvent
     * @throws IOException
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
     * This method is for exporting the appointments from logged user by a pdf file
     * @throws FileNotFoundException
     * @throws DocumentException
     */


    public void exportPdf() throws FileNotFoundException, DocumentException {

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Anil Aksu\\Desktop\\Test.pdf"));
        document.open();
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
                Paragraph paragraph = new Paragraph(" "+userid+ " "+name+ " "+date+ " "+start+ " "+startminutes+ " "+end+ " "+endminutes+ " "+location+ " "+participants+ " "+priority+ " "+reminder);
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


        } catch (SQLException e) {
            e.printStackTrace();
                } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

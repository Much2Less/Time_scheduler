package Controller;

import Object.Appointment;
import Object.DBData;
import Object.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("calender.fxml")));
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
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Anil Aksu\\Desktop\\Test.pdf"));
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2 });
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        document.open();
        System.out.println("Opening Document");
        document.add(new Chunk("Appointments"));
        document.add(new Paragraph(" "));

    /*    table.addCell("ID");
        table.addCell("Name");
        table.addCell("Date");
        table.addCell("Start");
        table.addCell("Startminutes");
        table.addCell("End");
        table.addCell("Endminutes");
        table.addCell("Location");
        table.addCell("Participants");
        table.addCell("Priority");
        table.addCell("Reminder");

        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(BaseColor.GRAY);
            System.out.println(j);
        }
        document.add(table);
    */
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(SELECT_APPOINTMENT)) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

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
                Paragraph paragraph = new Paragraph("ID: "+userid+ " Name: "+name+ " Date: "+date+ " At: "+start+ ":"+startminutes+ " o´clock End: "+end+ ":"+endminutes+ " o´clock Location: "+location+ " Participants: "+participants+ " Priority: "+priority+ " Reminder: "+reminder);
                document.add(paragraph);
                document.add(new Paragraph(" "));
                System.out.println("Writing Appointment into Document");
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
            System.out.println("Document closed");


        } catch (SQLException | DocumentException e) {
            e.printStackTrace();
                }
    }
}

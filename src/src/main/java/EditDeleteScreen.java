import Controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import Object.Appointments;
import Object.User;

public class EditDeleteScreen implements Initializable{

    private Stage stage;
    private Scene scene;

    @FXML
    private Label searchText;
    @FXML
    private ListView<String> myListview;
    @FXML
    private Button  SearchButton;
    @FXML
    private Button buttonCancel ;

    private final ArrayList<Appointments> userAppoList = new ArrayList<>();

    private int selectedUserIndex;
    private int userid;


    public User currentUser = LoginController.currentUser;


    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "much2less";
    static final String PASS = "1234qwer";
    static final String QUERY = "SELECT name,date,participants,reminder FROM time_scheduler.appointment Where  userid = ?";
    static final String SELECT_FROM_Appointment = "SELECT name,date,participants,reminder FROM appointment";




    public void switchToCalender(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("optionMenu.fxml")));
        this.stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        this.stage.setTitle("Welcome to Time Scheduler");
        this.scene = new Scene(root);
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectfromappoint();


    }

    public void selectfromappoint(){


        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(QUERY)
        ) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();


            //Saving Users from the database in an ArrayList
            while (rs.next()) {
                userAppoList.add(new Appointments(
                        rs.getString("name"),
                        Date.valueOf(rs.getString("date")),
                        rs.getString("participants"),
                        rs.getString("reminder")));
            }


            //Builds a string with information from every user
            for (int i = 0; i < userAppoList.size(); i++) {
                myListview.getItems().add(
                            userAppoList.get(i).getName() + " "
                                + userAppoList.get(i).getDate() + " "
                                + userAppoList.get(i).getParticipants() + " "
                                + userAppoList.get(i).getReminder());
            }

            myListview.setOnMouseClicked(event -> selectedUserIndex = myListview.getSelectionModel().getSelectedIndex());


        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
        /*
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(QUERY)

        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                String name = rs.getString("Name");
                Date date = Date.valueOf(rs.getString("date"));
                String participants = rs.getString("participants");

                String listOut ="Event Name "+ name +" on " + date+" " + " participants "+participants ;

                myListview.getItems().add(listOut);
/*
                myListview.getSelectionModel().getSelectedItems().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    }
                });
          myListview.setOnMouseClicked(event -> {

                    String selectedItem = myListview.getSelectionModel().getSelectedItem().toString();

                });
        */

 /*
                }
            }



        catch (SQLException e) {
            e.printStackTrace();
        }


    }

  */
}



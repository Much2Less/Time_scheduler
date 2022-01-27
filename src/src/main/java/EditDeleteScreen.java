import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

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




    static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
    static final String USER = "root";
    static final String PASS = "Prabin2468";
    static final String QUERY = "SELECT name,date,participants FROM time_scheduler.appointment ";




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
*/                  myListview.setOnMouseClicked(event -> {

                    String selectedItem = myListview.getSelectionModel().getSelectedItem().toString();

                });


                }
            }



        catch (SQLException e) {
            e.printStackTrace();
        }


    }
}



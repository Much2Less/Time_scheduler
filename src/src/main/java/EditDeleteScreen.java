import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EditDeleteScreen {

    private Stage stage;
    private Scene scene;

    public EditDeleteScreen() {
    }

    public void switchToCalender(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("optionMenu.fxml")));
        this.stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        this.stage.setTitle("Welcome to Time Scheduler");
        this.scene = new Scene(root);
        this.stage.setScene(this.scene);
        this.stage.show();
    }
}



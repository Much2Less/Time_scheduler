import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CalenderController {
    private Stage stage;
    private Scene scene;


    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("optionmenu.fxml"));
         stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
         scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}

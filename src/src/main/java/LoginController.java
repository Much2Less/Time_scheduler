import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class
LoginController extends Application {

	private Stage stage;
	private Scene scene;
	private Parent root;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchToRegister(javafx.event.ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
		stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

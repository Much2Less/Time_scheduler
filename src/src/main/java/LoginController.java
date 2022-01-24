import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;

import static javafx.fxml.FXMLLoader.load;

public class
LoginController extends Application {
	static final String DB_URL = "jdbc:mysql://localhost/time_scheduler";
	static final String USER = "much2less";
	static final String PASS = "1234qwer";
	static final String QUERY = "SELECT username, password, admin FROM login WHERE username = ? AND password = ?";

	@FXML
	private TextField usernameLogin;
	@FXML
	private TextField passwordLogin;

	private Stage stage;
	private Scene scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			Parent root = load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
			stage.setTitle("SignUp");
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchToRegister(javafx.event.ActionEvent actionEvent) throws IOException {
		Parent root = load(Objects.requireNonNull(getClass().getResource("register.fxml")));
		stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		stage.setTitle("Register");
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void switchToCalender(javafx.event.ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
		String username = usernameLogin.getText();
		String password = passwordLogin.getText();

		HashedPassword hashedPassword = new HashedPassword(password);
		String hash = hashedPassword.getHashString();

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			 PreparedStatement stmt = conn.prepareStatement(QUERY)
		) {
			stmt.setString(1, username);
			stmt.setString(2, hash);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				if (rs.getInt(3) > 0) switchToAdmin(actionEvent);
				else {
					Parent root = load(Objects.requireNonNull(getClass().getResource("optionMenu.fxml")));
					stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
					stage.setTitle("Welcome to Time Scheduler");
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				}
			}
			else {
				System.out.println("Not found!");
			}
			//TODO Display an Error Message if Username was not found or password is wrong

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void switchToAdmin(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
		Parent root = loader.load();
		AdminController controller = loader.getController();
		stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		AdminController.setup(loader);
	}
}

package application;

import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import java.io.IOException; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {
	private BorderPane root;
	public Stage primaryStage;
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Hospital App");
		
		InitialWidget();
		showLogin();
	}
	
	public void InitialWidget() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("Root.fxml"));
			root = (BorderPane) loader.load();
			
			Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showLogin() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Login.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            FXMLLoginController logincontroller = loader.getController();
            logincontroller.setMainApp(this);
            
            root.setCenter(personOverview);
//            while((con=logincontroller.getdatabae())==null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	public static void main(String[] args) {
		launch(args);
	}
}

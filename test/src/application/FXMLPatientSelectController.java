package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.mysql.jdbc.PreparedStatement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FXMLPatientSelectController {

    @FXML
    private Label LableOne;

    @FXML
    private Button GuahaoByHand;

    @FXML
    private Button GuahaoOnline;
    
    @FXML
    private Label id;
    
    @FXML
    private Button quit;
    
    private Main mainApp;
    
    private AnchorPane root;
    

    @FXML
    void OneClicked(MouseEvent event) {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("Patient.fxml"));
		try {
			root = (AnchorPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FXMLPatientController patientcontroller = loader.getController();
		patientcontroller.setMainApp(this.mainApp);
		
		Scene scene = new Scene(root);
        mainApp.primaryStage.setScene(scene);
        mainApp.primaryStage.show();
    }

    @FXML
    void TwoClicked(MouseEvent event) {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("Pay.fxml"));
		try {
			root = (AnchorPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FXMLPayController paycontroller = loader.getController();
		paycontroller.setMainApp(this.mainApp);
		
		Scene scene = new Scene(root);
        mainApp.primaryStage.setScene(scene);
        mainApp.primaryStage.show();
    }
    
    @FXML
    void initialize() {
    	id.setText("欢迎"+FXMLLoginController.user+"患者");
    }
    
    @FXML
    void logout(MouseEvent e) {
    	ConnectSql con = new ConnectSql();
		Connection myCon = con.connect();
		if(myCon == null){
			   JOptionPane.showMessageDialog(null, "连接数据库失败！");
			   return;
		}      
		  
		  /*fetch data*/
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = "select NOW();";
		try {
			pStatement=(PreparedStatement) myCon.prepareStatement(sql);
		} catch (SQLException e2) {
				// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
		} catch (SQLException e2) {
				// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String date = null;
		try {
			while(rs.next()) {
				try {
					date = rs.getString("NOW()");
					System.out.println(date);
				} catch (SQLException e1) {
						// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (SQLException e2) {
				// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String preStr = "UPDATE BRXX SET DLRQ = \""+ date +"\"WHERE BRBH =\""+FXMLLoginController.name+"\";";
		try {
			pStatement=(PreparedStatement) myCon.prepareStatement(preStr);
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		System.out.println( preStr );	
		try {
			pStatement.executeUpdate();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}	
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("Login.fxml"));
		try {
			root = (AnchorPane) loader.load();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FXMLLoginController logcontroller = loader.getController();
		logcontroller.setMainApp(this.mainApp);
		
		Scene scene = new Scene(root);
        mainApp.primaryStage.setScene(scene);
        mainApp.primaryStage.show();
    }
    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
    }
}

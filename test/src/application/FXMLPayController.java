package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.mysql.jdbc.PreparedStatement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FXMLPayController {

	private Main mainApp;
	
	private AnchorPane root;
    @FXML
    private TextField paymoney;

    @FXML
    private Button subbutton;
    
    @FXML
    private Label tip;
    
    @FXML
    void initialize() {
    	paymoney.textProperty().addListener((obs,oldText,newText)->{
    		if(!isInteger(newText)) {
    			tip.setText("请输入整数金额");
    		}
    		else {
    			tip.setText("");
    		}
    	});
    }
    
    private boolean isInteger(String str) {  
        if (null == str || "".equals(str)) {  
            return false;  
        }  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
    }  

    @FXML
    void submit(MouseEvent event) {
    	String[] money = paymoney.getText().split("元");
    	 ConnectSql con = new ConnectSql();
		 Connection myCon = con.connect();
		 if(myCon == null)
		 {
			   JOptionPane.showMessageDialog(null, "连接数据库失败！");
			   return;
		 }
		 PreparedStatement pStatement = null;
		 ResultSet rs = null;
		 String addYCJE = "update BRXX set YCJE = YCJE + ? Where BRXX.BRBH = ?;";
			
		try {
			pStatement=(PreparedStatement) myCon.prepareStatement(addYCJE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			pStatement.setString(1, money[0]);
			pStatement.setString(2, FXMLLoginController.name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int isSuc = 0;
		try {
			isSuc = pStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(isSuc > 0) {
			String sucStr = "充值成功,金额为：" + paymoney.getText();
			JOptionPane.showMessageDialog(null, sucStr, "充值成功", 1);
		}
		else {
			String sucStr = "充值失败";
			JOptionPane.showMessageDialog(null, sucStr, "充值失败", 1);
		}
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("PatientSelect.fxml"));
			try {
				root = (AnchorPane) loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			FXMLPatientSelectController patientSelectcontroller = loader.getController();
			patientSelectcontroller.setMainApp(this.mainApp);
            
			Scene scene = new Scene(root);
            mainApp.primaryStage.setScene(scene);
            mainApp.primaryStage.show();
	}
		
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
    }

}
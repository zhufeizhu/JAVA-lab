/**
 * Sample Skeleton for 'Login.fxml' Controller Class
 */

package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class FXMLLoginController {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/JAVA?useSSL=false";
 
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";
    
    static String name;
    
    static String user;
    
	private Main mainApp;
    @FXML // fx:id="doctor"
    private RadioButton doctor; // Value injected by FXMLLoader

    @FXML // fx:id="password"
    private PasswordField password; // Value injected by FXMLLoader

    @FXML // fx:id="log"
    private Button log; // Value injected by FXMLLoader

    @FXML // fx:id="patient"
    private RadioButton patient; // Value injected by FXMLLoader

    @FXML // fx:id="username"
    private TextField username; // Value injected by FXMLLoader

    @FXML
    private AnchorPane root;
    
    @FXML
    void initialize() {
    	Connection conn = null;
    	Statement state = null;
    	LinkedList resultlist =  new LinkedList<String>();
    	try{
	           ConnectSql SQL = new ConnectSql();
	           conn = SQL.connect();
	           if(conn != null) {
	        	// 打开链接
		            state = conn.createStatement();
		            String sql = "select * from KSYS;";
		            ResultSet res = state.executeQuery(sql);
		            while(res.next()) {
		            	String str = res.getString("YSBH").trim();
		            	resultlist.add(str);
		            }
		            sql = "select * from BRXX;";
		            res = state.executeQuery(sql);
		            while(res.next()) {
		            	String str = res.getString("BRBH").trim();
		            	resultlist.add(str);
		            }
		            AutoCompletionBinding textAutoBingding = TextFields.bindAutoCompletion(username, resultlist);
		            textAutoBingding.setVisibleRowCount(5);
		            textAutoBingding.setMaxWidth(200);
	           }
	        }catch(Exception e){
    			e.printStackTrace();
    		}
    }
    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
    }
    
    
    @FXML
    void submit(MouseEvent event) {
    	String name = username.getText();
    	String psw  = password.getText();
    	FXMLLoginController.name = name;
    	Connection conn = null;
    	Statement state = null;
    	try{
	           ConnectSql SQL = new ConnectSql();
	           conn = SQL.connect();
	           if(conn != null) {
	        	// 打开链接
		            state = conn.createStatement();
		            
		            String sql;
		            if(doctor.isSelected()) {
		            	sql = "select * from KSYS where YSBH = \"" + name + "\" and DLKL = \"" + psw + "\";";
		            }
		            else {
		            	sql = "select * from BRXX where BRBH = \"" + name + "\" and DLKL = \"" + psw + "\";";
		            }
		            
		            name = "";
		            ResultSet result = state.executeQuery(sql);
		            
		            if(result.next()) {
		            		try {
		            			if(doctor.isSelected()){
		            				FXMLLoginController.user = result.getString("YSMC");
		            				String str = "欢迎医生"+FXMLLoginController.user+"登陆系统";
		            				JOptionPane.showMessageDialog(null, str, "欢迎登陆", 1);
		            				FXMLLoader loader = new FXMLLoader();
				        			loader.setLocation(Main.class.getResource("Doctor.fxml"));
				        			root = (AnchorPane) loader.load();

				        			FXMLDoctorController doctorcontroller = loader.getController();
				        			doctorcontroller.setMainApp(this.mainApp);
				        			
				        			Scene scene = new Scene(root);
				                    mainApp.primaryStage.setScene(scene);
				                    mainApp.primaryStage.show();
		            			}else {
		            				FXMLLoginController.user = result.getString("BRMC");
		            				String str = "欢迎患者"+FXMLLoginController.user+"登陆系统";
		            				JOptionPane.showMessageDialog(null, str, "欢迎登陆", 1);
			            			FXMLLoader loader = new FXMLLoader();
				        			loader.setLocation(Main.class.getResource("PatientSelect.fxml"));
				        			root = (AnchorPane) loader.load();
				        			
				        			FXMLPatientSelectController patientSelectcontroller = loader.getController();
				        			patientSelectcontroller.setMainApp(this.mainApp);
				                    
				        			Scene scene = new Scene(root);
				                    mainApp.primaryStage.setScene(scene);
				                    mainApp.primaryStage.show();	
		            			}		        			
		        			} 
		            		catch(Exception e){
		            			e.printStackTrace();
		            		}
		            }
		            else {
		            	Alert information = new Alert(Alert.AlertType.ERROR,"Wrong UserName or PassWord");
		        		information.setTitle("错误提示"); 
		        		information.setHeaderText("您输入了错误的用户名或密码，请确认后重新输入");
		        		information.showAndWait(); 
		            }
	           }
    	}catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    	finally {
    		if(conn != null) {
    			try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}	   	
    }
}

package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import com.mysql.jdbc.PreparedStatement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FXMLDoctorController {
	
	private String name;
	
	private AnchorPane root;
	
    @FXML
    private TableView<Patient> PatientTable;
    @FXML
    private TableColumn<Patient, String> GHID;
    @FXML
    private TableColumn<Patient, String> GHTIME;
    @FXML
    private TableColumn<Patient, String> PATIENTNAME;
    @FXML
    private TableColumn<Patient, String> PATIENTTYPE;

    
    @FXML
    private TableView<Doctor> DoctorTable;
    @FXML
    private TableColumn<Doctor, String> DOCID;
    @FXML
    private TableColumn<Doctor, String> KSNAME;
    @FXML
    private TableColumn<Doctor, String> DOCNAME;
    @FXML
    private TableColumn<Doctor,String> DOCTORTYPE;
    @FXML
    private TableColumn<Doctor, String> NUM;    
    @FXML
    private TableColumn<Doctor, String> INCOME;
    
    @FXML
    private DatePicker starttime;
    
    @FXML
    private DatePicker endtime;



    @FXML
    private Button submit1;
    
    @FXML
    private Button submit2;

    @FXML
    private Button submit3;
    
    @FXML
    private Label id;
    
    
    private Main mainApp;
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
    }


    private final ObservableList<Patient> dataPatient = FXCollections.observableArrayList();
    private final ObservableList<Doctor> dataDoctor = FXCollections.observableArrayList();
//    @FXML
//    void serch1(MouseEvent event) {
//    		System.out.print("1");
//    }

//    @FXML
//    void SerchIncome(MouseEvent event) {
//
//    }
    
    @FXML
    void OnSerchClicked(MouseEvent event){
		ConnectSql SQL = new ConnectSql();
		Connection conn = SQL.connect();
		if(conn == null)
		   {
			   JOptionPane.showMessageDialog(null, "连接数据库失败！");
			   return;
		   }
		    Statement state = null;		    
		    try {
				state = conn.createStatement();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			ResultSet rs = null;
			name = FXMLLoginController.name;
			//String sql = "select * from register join patient on register.pid = patient.pid) join register_category on register.catid = register_category.catid where register.docid =\"" + FXMLLoginController.name + "\";";
			String sql = "SELECT * FROM GHXX,BRXX,KSYS WHERE GHXX.BRBH = BRXX.BRBH AND GHXX.YSBH = KSYS.YSBH AND GHXX.YSBH = \'" + name + "\';";
			System.out.print(sql+"\n");		
			try {
				rs = state.executeQuery(sql);
				String GHID = null;
				String PATIENTNAME = null;
				String GHTIME = null;
				String PATIENTTYPE = null;
				while(rs.next())
				{
					GHID = rs.getString("GHBH");
					PATIENTNAME = rs.getString("BRMC");
					GHTIME = rs.getString("RQSJ");
					PATIENTTYPE = rs.getString("SFZJ");
					System.out.println(PATIENTTYPE);
					if(Integer.parseInt(PATIENTTYPE) == 1) PATIENTTYPE = "专家号";
					else PATIENTTYPE = "普通号";
					dataPatient.add(new Patient(GHID,PATIENTNAME,GHTIME, PATIENTTYPE));
				}	
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		   
		 /*close the connection*/
		   try 
			{
			   conn.close();
			} catch (SQLException e) {
			   e.printStackTrace();
			}
		   PatientTable.setItems(dataPatient);
    }
    
    @FXML
    void TwoClicked(MouseEvent event) {
		/*set up connection */
    	ConnectSql SQL = new ConnectSql();
		Connection conn = SQL.connect();
		if(conn == null){
			   JOptionPane.showMessageDialog(null, "连接数据库失败！");
			   return;
		}
		/*fetch data*/
		Statement state = null;
	    
	    try {
			state = conn.createStatement();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ResultSet rs = null;
		try {
			String pattern = "yyyy-MM-dd";
			DateTimeFormatter format = null;
			format = DateTimeFormatter.ofPattern(pattern);
			String st = format.format(starttime.getValue());
			String et = format.format(endtime.getValue());
			System.out.println(st);
			String sql = "SELECT KSXX.KSMC,KSYS.YSBH,KSYS.YSMC,HZXX.SFZJ,COUNT(GHXX.GHRC),SUM(GHXX.GHFY) FROM KSYS,KSXX,GHXX,HZXX WHERE KSYS.KSBH = KSXX.KSBH AND GHXX.YSBH = KSYS.YSBH AND GHXX.RQSJ >= \""+st+"\" AND GHXX.RQSJ<= \""+et 
					+"\"GROUP BY KSXX.KSMC,KSYS.YSBH,KSYS.YSMC,HZXX.SFZJ;";
			System.out.println(sql);
			rs=state.executeQuery(sql);
			while(rs.next()){
//				timeLabel.setText(timeLabelStr);
					
				String KSMC = rs.getString("KSXX.KSMC").trim();
				String YSBH = rs.getString("KSYS.YSBH").trim();
				String YSMC = rs.getString("KSYS.YSMC").trim();
				String HZLB = null;
				if(rs.getInt("HZXX.SFZJ") == 1){
					HZLB = "专家号";
				}else {
					HZLB = "普通号";
				}				
				int patientCount = rs.getInt("COUNT(GHXX.GHRC)");
				BigDecimal regiFeeDec = rs.getBigDecimal("SUM(GHXX.GHFY)");				
				String GHRC = String.valueOf(patientCount);
				String SRHJ = String.valueOf(regiFeeDec);
				System.out.println(KSMC + YSBH + YSMC + HZLB + GHRC + SRHJ);
				dataDoctor.add(new Doctor(KSMC, YSBH, YSMC, HZLB, GHRC, SRHJ));
			}				
		} catch (SQLException e1) {
				e1.printStackTrace();
		}
		 /*close the connection*/
		   try 
			{
			   conn.close();
			} catch (SQLException e) {
			   e.printStackTrace();
			}
		   if(DoctorTable == null) System.out.println("xxx");
		   DoctorTable.setItems(dataDoctor);
    }

    @FXML
    void OnThreeClicked(MouseEvent event) {
    	ConnectSql con = new ConnectSql();
		   Connection myCon = con.connect();
		   if(myCon == null)
		   {
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
			
			
				String preStr = "UPDATE KSYS SET DLRQ = \""+ date +"\"WHERE YSBH =\""+FXMLLoginController.name+"\";";
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FXMLLoginController logincontroller = loader.getController();
        
		Scene scene = new Scene(root);
        mainApp.primaryStage.setScene(scene);
        mainApp.primaryStage.show();
    }
    

    @FXML
    public void initialize(){
		starttime.setValue(LocalDate.now());
		endtime.setValue(LocalDate.now());
		id.setText("欢迎"+FXMLLoginController.user+"医生");
		
    	GHID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGHId()));
    	PATIENTNAME.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
    	GHTIME.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
    	PATIENTTYPE.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().gettype()));
    	
    	KSNAME.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKeshiName()));
    	DOCID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDocId()));
    	DOCNAME.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDocName()));
    	DOCTORTYPE.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().gettype()));
    	NUM.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getnum()));
    	INCOME.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getincome()));

    }
}


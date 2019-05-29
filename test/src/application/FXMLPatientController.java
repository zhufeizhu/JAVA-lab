package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JOptionPane;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.mysql.jdbc.PreparedStatement;

import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FXMLPatientController {
	
	
		private double allMoney;
		private double submoney;
		private boolean isshow = false;
		private String YSBH;
		private String KSMC;
		private String HZBH;
		private AnchorPane root;
		private Main mainApp;

	    @FXML
	    private TextField DoctorName;

	    @FXML
	    private TextField KeshiName;

	    @FXML
	    private TextField GuahaoNumber;


	    @FXML
	    private ChoiceBox<String> HaozhongType;


	    @FXML
	    private TextField ZhaolingMoney;

	    @FXML
	    private TextField HaozhongName;
	    @FXML
	    private TextField YingjiaoMoney;

	    @FXML
	    private TextField JiaokuanMoney;

	    @FXML
	    private Button ok;	    

	    @FXML
	    private Button clear;
	   
	    @FXML
	    private Label id;

	    @FXML
	    private Button logout;
	    boolean isBalanceSufficient;
	    
		private Set<String> autoCompletions;
		
		SuggestionProvider<String> provider;
		
	    @FXML public void initialize() {
	    	id.setText("欢迎"+FXMLLoginController.user+"患者");
			isBalanceSufficient = true;
			autoCompletions = new HashSet<>(Arrays.asList("A","B","C"));
			provider = SuggestionProvider.create(autoCompletions);
			TextFields.bindAutoCompletion(DoctorName, provider);
			System.out.println("keshiname triggered");
			HaozhongType.getItems().addAll("专家号", "普通号");
			HaozhongType.getSelectionModel().selectFirst();
			/*set up connection */
			   ConnectSql con = new ConnectSql();
			   Connection conn = con.connect();
			   if(conn == null)
			   {
				   JOptionPane.showMessageDialog(null, "连接数据库失败！");
				   return;
			   }
			/*fetch data*/
			    Statement state = null;
				try {
					state = conn.createStatement();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				ResultSet rs = null;
				LinkedList<String> searchResult = new LinkedList<>();
				System.out.println("preparing keshi info\n");
				
				String sql = "SELECT * from KSXX;";				
				try {
					rs  = state.executeQuery(sql);
					while(rs.next())
					{
						String str1 = rs.getString("KSBH").trim();
						String str2 = rs.getString("KSMC").trim();
						String str3 = rs.getString("PYZS").trim();
						String togetherStr = str1 + " " + str2 + " " + str3;
						searchResult.add(togetherStr);
					}
					System.out.println(searchResult);
					AutoCompletionBinding textAutoBingding  = TextFields.bindAutoCompletion(KeshiName, searchResult);
					textAutoBingding.setVisibleRowCount(5);
					textAutoBingding.setMaxWidth(200);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				String patientID = FXMLLoginController.name;
				
				String myPreState = "SELECT * from BRXX WHERE BRBH = '%1$s';";  	
				String myfinalState = String.format(myPreState, patientID);
				System.out.println(myfinalState);
				sql = myfinalState;
				
				try {
					rs = state.executeQuery(sql);
					double  ycMoney = 0.0;
					while(rs.next())
					{
						BigDecimal dec = rs.getBigDecimal("YCJE");
						ycMoney = dec.doubleValue();
						allMoney = ycMoney;
					}
					String str = Double.toString(ycMoney) + "元";
					str ="账户余额:" + ycMoney +"元";
					JiaokuanMoney.setText(str);
					JiaokuanMoney.setEditable(false);
					ZhaolingMoney.setEditable(false);
//						
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
			
			KeshiName.textProperty().addListener((obs, oldText, newText) -> {
				String[] list = newText.split(" ");
				if(list.length == 3) {
					KeshiName.setText(list[1]);
					KSMC = list[0];
				}
			});
			
			DoctorName.textProperty().addListener((obs, oldText, newText) -> {
				String[] list = newText.split(" ");
				if(list.length == 3) {
					DoctorName.setText(list[1]);
					YSBH = list[0];
				}
			});
			
			HaozhongName.focusedProperty().addListener((obs, oldbool, newbool)->{
				if(newbool == true )
					on_select_haozhongnama();
			});
			
			DoctorName.focusedProperty().addListener((obs, oldbool, newbool)->{
				if(newbool == true)
					on_select_doctorname();
			});
		};
	    
	    private void on_select_doctorname() {
			// TODO Auto-generated method stub
	    	int isPro;
	    	if(HaozhongType.getValue().equals("专家号"))
	    	{
	    		isPro = 1;
	    	}else {
	    		isPro = 0;
			}
	    	LinkedList<String> list = new LinkedList<String>();
	    	ConnectSql con = new ConnectSql();
			Connection conn = con.connect();
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
			System.out.println("preparing doctor info\n");
			String sql = null;
			String fetchString = null;
			String temp = "SELECT * FROM KSYS,KSXX WHERE KSXX.KSMC = '%1$s' AND SFZJ = %2$d AND KSYS.KSBH=KSXX.KSBH";
			fetchString = String.format(temp, KeshiName.getText(),isPro);
			sql = fetchString;
			System.out.print(sql);
			
			try {
				rs = state.executeQuery(sql);
				while(rs.next())
				{
					String str1 = rs.getString("YSBH").trim();
					String str2 = rs.getString("YSMC").trim();
					String str3 = rs.getString("PYZS").trim();
					String togetherStr = str1 + " " + str2 + " " + str3;
					list.add(togetherStr);
				}
				AutoCompletionBinding textAutoBingding  = TextFields.bindAutoCompletion(DoctorName, list);
				textAutoBingding.setMaxWidth(200);
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
			
		}

		private void on_select_haozhongnama() {
	    	int isPro;
	    	if(HaozhongType.getValue().equals("专家号"))
	    	{
	    		isPro = 1;
	    	}else {
	    		isPro = 0;
			}
	    	ConnectSql con = new ConnectSql();
			Connection conn = con.connect();
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
			String sql = null;
			String fetchString = null;
			String temp = "SELECT * FROM HZXX,KSXX WHERE KSXX.KSBH = HZXX.KSBH AND KSXX.KSMC = '%1$s' AND HZXX.SFZJ = %2$d;";
			fetchString = String.format(temp, KeshiName.getText(),isPro);
			sql = fetchString;
			System.out.print(sql);
			String str1 = null;
			String str2 = null;		
			try {
				rs = state.executeQuery(sql);
				while(rs.next()) {
					str1 = rs.getString("HZMC").trim();
					str2 = rs.getString("GHFY").trim();
					HZBH = rs.getString("HZBH").trim();
				}
				HaozhongName.setText(str1);
				HaozhongName.setEditable(false);
				YingjiaoMoney.setText(str2);
				YingjiaoMoney.setEditable(false);
				submoney = allMoney - Double.parseDouble(str2);
				if(submoney >=0 ) {
					double left = allMoney - Double.parseDouble(str2);
					String strleft = Double.toString(left);
					strleft = "预存金额剩余:" + strleft + "元";
					ZhaolingMoney.setText(strleft);
				}
				else {
					ok.setDisable(false);
					if(!isshow) {
						isshow = !isshow;
						Alert information = new Alert(Alert.AlertType.INFORMATION,"Not Enough Money!");
		        		information.setTitle("交款提醒"); //设置标题，不设置默认标题为本地语言的information
		        		information.setHeaderText("您的预存金额已不够缴费，请尽快缴费!"); //设置头标题，默认标题为本地语言的information
		        		information.showAndWait(); //显示弹窗，同时后续代码等挂起
					}
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
		   
		   
			
		}	    

	    @FXML
	    void okclicked(MouseEvent event) {
	    	System.out.print("1");
	    	/*检查是否存在空项*/
			 if(KeshiName.getText().equals(""))
			 {
				 return;
			 }
			 if(DoctorName.getText().equals(""))
			 {
				 return;
			 }
			 if(HaozhongName.getText().equals(""))
			 {
				 return;
			 }
			 if(ZhaolingMoney.getText().equals(""))
			 {
				 return;
			 }
		 
			 String GHBH = null;  //需要遍历，看目前有多少个号
			 String HZMC = HaozhongName.getText();
			 String BRBH = FXMLLoginController.name;
			 int GHRC = 0;            //需要读出，看目前人次是多少,然后加1
			 boolean THBZ = true;
			 BigDecimal GHFY = new BigDecimal(YingjiaoMoney.getText());
			 Date dNow = new Date();
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String RQSJ = sdf.format(dNow);
			 
			 /*set up connection */
			   ConnectSql con = new ConnectSql();
			   Connection myCon = con.connect();
			   if(myCon == null)
			   {
				   JOptionPane.showMessageDialog(null, "连接数据库失败！");
				   return;
			   }
			   
			  /*fetch data*/
			    PreparedStatement pStatement = null;
			    PreparedStatement pStatement2 = null;
				ResultSet rs = null;
				System.out.println("preparing to read\n");
				
				try {
					String preStr = "SELECT MAX(GHXX.GHRC), HZXX.GHRS from GHXX, HZXX, KSXX"
							+ " WHERE  HZXX.HZBH = GHXX.HZBH AND HZXX.HZMC = '%1$s' GROUP BY HZXX.HZMC,HZXX.GHRS;";
					preStr = String.format(preStr, HZMC);
					pStatement=(PreparedStatement) myCon.prepareStatement(preStr);
					System.out.println(preStr);	
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				try {
					rs = pStatement.executeQuery();
					int maxGHRC;
					while(rs.next())
					{
						GHRC = rs.getInt("MAX(GHXX.GHRC)");
						maxGHRC = rs.getInt("HZXX.GHRS");												
						if ((maxGHRC-1) <= GHRC)
						{
							Alert information = new Alert(Alert.AlertType.INFORMATION,"Full People!");
			        		information.setTitle("挂号人数已满");
			        		information.setHeaderText("您选择的号种挂号人数已达上限，请重新选择!"); 
			        		information.showAndWait(); 
			        		return;
						}
						else
							GHRC++;												
					}
						System.out.println("1111");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				try {
					pStatement=(PreparedStatement) myCon.prepareStatement("SELECT COUNT(*) from GHXX");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				try {
					rs = pStatement.executeQuery();
					String numStr = null;
					while(rs.next())
					{
						numStr = rs.getString("COUNT(*)").trim();
					}
					int num = (int)Double.parseDouble(numStr) + 1;
					GHBH = String.format("%06d", num);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}								
				
			 /*插入内容*/
				try {
					String insertGHXX = "INSERT INTO GHXX VALUES(?,?,?,?,?,?,?,?);";
					String subYCJE = "update BRXX set YCJE = ?;";
					
					pStatement=(PreparedStatement) myCon.prepareStatement(insertGHXX);
					pStatement.setString(1, GHBH);
					pStatement.setString(2, HZBH);
					pStatement.setString(3, YSBH);
					pStatement.setString(4, BRBH);
					pStatement.setInt(5, GHRC);
					pStatement.setBoolean(6, THBZ);
					pStatement.setBigDecimal(7, GHFY);
					pStatement.setString(8, RQSJ);
					
					pStatement2=(PreparedStatement) myCon.prepareStatement(subYCJE);
					pStatement2.setDouble(1,submoney);
					System.out.println(pStatement);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				try {
					int isSuc = pStatement.executeUpdate();
					int isSuc2 = pStatement2.executeUpdate();
					if(isSuc > 0 && isSuc2>0)
					{
						String sucStr = "挂号成功,号码为：" + GHBH;
						JOptionPane.showMessageDialog(null, sucStr, "挂号成功", 1);
						clear();
						
					}else
					{
						JOptionPane.showMessageDialog(null, "挂号失败，请检查输入是否正确", "挂号失败", 1);
					}
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			 /*close the connection*/
			   try 
				{
				   myCon.close();
				} catch (SQLException e) {
				   e.printStackTrace();
				}   
		 }
	    
	    void clear() {
	    	System.out.println("Clear out all the text");
			KeshiName.clear();
			DoctorName.clear();
			HaozhongName.clear();
			JiaokuanMoney.clear();
			YingjiaoMoney.clear();
			ZhaolingMoney.clear();
			GuahaoNumber.clear();
			JiaokuanMoney.setEditable(true);
			ZhaolingMoney.setEditable(true);
	    }
	    

	    @FXML
	    void clearclicked(MouseEvent e) {
	    	clear();
	    }

	    @FXML 
	    void logout(MouseEvent e) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Main.class.getResource("PatientSelect.fxml"));
					try {
						root = (AnchorPane) loader.load();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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


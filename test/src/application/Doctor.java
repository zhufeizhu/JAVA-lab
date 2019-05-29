package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Doctor {
		private final SimpleStringProperty KeshiName;
		private final SimpleStringProperty DocId;
		private final SimpleStringProperty DocName;
		private final SimpleStringProperty type;
		private final SimpleStringProperty num;
		private final SimpleStringProperty income;
	 
		Doctor(String KeshiName, String DocId, String DocName,String type,String num,String income){
			this.KeshiName = new SimpleStringProperty(KeshiName);
			this.DocId = new SimpleStringProperty(DocId);
			this.DocName = new SimpleStringProperty(DocName);
			this.type = new SimpleStringProperty(type);
			this.num = new SimpleStringProperty(num);
			this.income = new SimpleStringProperty(income);
		}
	 
		public String getKeshiName() {
			return KeshiName.get();
		}
	 
		public void setKeshiName(String KsName) {
			KeshiName.set(KsName);
		}
	 
		public String getDocId() {
			return DocId.get();
		}
	 
		public void setDocId(String KsName) {
			DocId.set(KsName);
		}
		
		public String getDocName() {
			return DocName.get();
		}
	 
		public void setDocName(String KsName) {
			DocName.set(KsName);
		}
		
		public String gettype() {
			return type.get();
		}
	 
		public void settype(String ty) {
			type.set(ty);
		}
		
		public String getnum() {
			return num.get();
		}
		
	 
		public void setnum(String n) {
			num.set(n);
		}
		
		public String getincome() {
			return income.get();
		}
		
	 
		public void setincome(String n) {
			income.set(n);
		}
		
		
	}

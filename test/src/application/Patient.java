package application;

import javafx.beans.property.SimpleStringProperty;

public class Patient{
	private final SimpleStringProperty GHId;
	private final SimpleStringProperty Name;
	private final SimpleStringProperty Time;
	private final SimpleStringProperty type;
 
	Patient(String GHId, String Name, String Time,String type){
		this.GHId = new SimpleStringProperty(GHId);
		this.Name = new SimpleStringProperty(Name);
		this.Time = new SimpleStringProperty(Time);
		this.type = new SimpleStringProperty(type);
	}
 
	public String getGHId() {
		return GHId.get();
	}
 
	public void setGHId(String str) {
		GHId.set(str);
	}
	
	public String getName() {
		return Name.get();
	}
 
	public void setName(String str) {
		Name.set(str);
	}
	
	public String getTime() {
		return Time.get();
	}
 
	public void setTime(String str) {
		Time.set(str);
	}
	
	public String gettype() {
		return type.get();
	}
 
	public void settype(String str) {
		type.set(str);
	}
}

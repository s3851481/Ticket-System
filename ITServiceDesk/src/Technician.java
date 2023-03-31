
public class Technician extends Users{
 private int techLvl;



public Technician(String email, String fullName, int phoneNum, String password, int techLvl) {
	super(email, fullName, phoneNum, password);
	this.techLvl = techLvl;
}



public int getTechLvl() {
	return techLvl;
}



public void setTechLvl(int techLvl) {
	this.techLvl = techLvl;
}
 
 
}

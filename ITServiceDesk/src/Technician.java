import java.io.Serializable;
import java.util.Hashtable;

public class Technician extends Users implements Serializable {
	private int techLvl;
	private int ticketsOpen;

	public Technician(String email, String fullName, String phoneNum, String password, userType type, Hashtable<String, String> securityQuestions, int techLvl, int ticketsOpen) {
		super(email, fullName, phoneNum, password, type,securityQuestions);
		setTechLvl(techLvl);
		setTicketsOpen(ticketsOpen);
	}

	public int getTechLvl() {
		return techLvl;
	}

	public void setTechLvl(int techLvl) {
		this.techLvl = techLvl;
	}

	public int getTicketsOpen() {
		return ticketsOpen;
	}

	public void setTicketsOpen(int ticketsOpen) {
		this.ticketsOpen = ticketsOpen;
	}

}

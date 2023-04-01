
public class Technician extends Users {
	private int techLvl;
	private int ticketsOpen;

	public Technician(String email, String fullName, int phoneNum, String password, int techLvl, int ticketsOpen) {
		super(email, fullName, phoneNum, password);
		this.techLvl = techLvl;
		this.ticketsOpen = ticketsOpen;
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

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BackEnd {

	List<Users> users = new ArrayList<>();
	List<Ticket> tickets = new ArrayList<>();
	List<Ticket> tempList = new ArrayList<>();
	List<Ticket> archiveTickets = new ArrayList<>();
	List<Ticket> selectionTickets = new ArrayList<>();
	List<Users> tempUse = new ArrayList<>();
	List<Users> tempUseTwo = new ArrayList<>();
	String validate;
	Users currentUser = null;
	Users tempTech = null;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	// technician and admin hard coded into system to set there user types runs only
	// if no users file
	private void initializeTechnicians() {
		Users tech1 = new Users("harry.styles@cinco.com", "Harry Styles", "0356781000", "Password1",
				userType.Level1Tech, null);
		Users tech2 = new Users("niall.horan@cinco.com", "Niall Horan", "0356781001", "Password1", userType.Level1Tech,
				null);
		Users tech3 = new Users("liam.payne@cinco.com", "Liam Payne", "0356781002", "Password1", userType.Level1Tech,
				null);
		Users tech4 = new Users("louis.tomlinson@cinco.com", "Louis Tomlinson", "0356781003", "Password1",
				userType.Level2Tech, null);
		Users tech5 = new Users("zayn.malik@cinco.com", "Zayn Tomlinson", "0356781004", "Password1",
				userType.Level2Tech, null);
		Users admin = new Users("admin@cinco.com", "Administrator", "0400000000", "Admin", userType.admin, null);
		this.users.add(tech1);
		this.users.add(tech2);
		this.users.add(tech3);
		this.users.add(tech4);
		this.users.add(tech5);
		this.users.add(admin);
	}

	// load from the save file of users and loads into the arraylist
	@SuppressWarnings({ "unchecked", "resource" })
	public void load(String savefile) {
		File userFile = new File(savefile);
		List<Users> users = new ArrayList<>();
		if (userFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(savefile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				users = (ArrayList<Users>) ois.readObject();
				this.users = users;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			this.initializeTechnicians();
			try {
				FileOutputStream fos = new FileOutputStream(savefile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(this.users);
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

// new creation or change of users will overwrite the file users
	public void persistUsers(String savefile) {
		try {
			FileOutputStream fos = new FileOutputStream(savefile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this.users);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// loads the tickets from the file into the arraylists
	@SuppressWarnings({ "unchecked", "resource" })
	public void loadTickets(String savedTickets) {
		File ticketsFile = new File(savedTickets);
		List<Ticket> currentTickets = new ArrayList<>();
		long millis = System.currentTimeMillis();
		if (ticketsFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(savedTickets);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				currentTickets = (ArrayList<Ticket>) ois.readObject();
				this.tickets = currentTickets;

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("error");
			}
		}

	}

	// load archive file into arraylist
	public void loadArchive(String archive) {
		File archiveFile = new File(archive);
		List<Ticket> currentArchive = new ArrayList<>();

		if (archiveFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(archive);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				currentArchive = (ArrayList<Ticket>) ois.readObject();
				this.archiveTickets = currentArchive;

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("error");
			}

		}
	}

// on load of program moves the closed tickets to the archive after 24 hours and removes from the ticket arraylist
	public void resetTickets(String savedTickets, String archive) {
		long millis = System.currentTimeMillis();
		this.tempList.clear();
		// moves file to archive file if 24 elapsed
		for (Ticket x : tickets) {
			if (x.getClosed() != null) {
				// + 24 * 60 * 60 * 1000 add this after x.getTimecounter for 24 hour otherwise
				// they go
				// straight to archive after closed
				if (millis >= x.getTimeCounter() && x.getTimeCounter() != 0) {
					this.archiveTickets.add(x);
					// add only the newly changed over tickets to a list for removal
					this.tempList.add(x);
				}
			}
			// removes the tickets that are in the archive from the main tickets
		}
		if (!tempList.isEmpty()) {
			// removes all matches from tempList from the tickets than overrides the files
			tickets.removeAll(tempList);
			persistTickets(savedTickets);
			// override the archive
			persistTickets2(archive);
		}

	}

	// validate password from the input
	public boolean validatePass(String pass) {
		boolean found = false;
		if (currentUser.getPassword().matches(pass)) {
			found = true;
		}
		if (found) {
			return true;
		} else {
			return false;
		}

	}

	// validation for unique user email
	public boolean userExists(String email) {
		boolean found = false;
		for (Users x : users) {
			if (x.getEmail().matches(email)) {
				found = true;
			}
		}
		if (found) {
			return true;
		} else {
			return false;
		}

	}

	// log in user validation setting the current user to input
	public boolean validateUser(String email) {
		currentUser = null;
		boolean found = false;
		for (Users x : users) {
			if (x.getEmail().matches(email)) {
				currentUser = x;
				found = true;
			}
		}

		if (found) {
			return true;
		} else {
			return false;
		}
	}

	// ticket creation with user input auto assign tech and fill in not needed
	// information at this stage
	public Ticket createTicket(String description, int severity) {
		String creator = currentUser.getEmail();
		String tech = null;
		LocalDate created = LocalDate.now();
		LocalDate closed = null;
		long count = 0;
		int status = 1;
		try {
			String assignedTech = assignTicket(severity);
			tech = assignedTech;
		} catch (Exception e) {
			System.out.println("error assigning tech");
		}
		Ticket newTicket = new Ticket(description, severity, creator, tech, status, created, closed, count);
		this.tickets.add(newTicket);
		return newTicket;
	}

	// write tickets to file overwrites on use
	public void persistTickets(String savedTickets) {
		try {
			FileOutputStream fos = new FileOutputStream(savedTickets);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this.tickets);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	// write achive tickets to file overwrites on use
	public void persistTickets2(String archive) {
		try {
			FileOutputStream fos = new FileOutputStream(archive);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this.archiveTickets);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	// maps how many tickets each tech has to allow to assign tickets based on least
	private HashMap<String, Integer> getAssignedTicketCount() {
		HashMap<String, Integer> ticketCounts = new HashMap<String, Integer>();

		if (this.tickets == null) {
			return ticketCounts;
		} else {
			for (Ticket x : tickets) {
				if (ticketCounts.containsKey(x.getAssignedTech())) {
					int count = ticketCounts.get(x.getAssignedTech());
					ticketCounts.put(x.getAssignedTech(), count + 1);
				} else {
					// First time we have found a ticket for that user
					ticketCounts.put(x.getAssignedTech(), 1);
				}
			}
		}
		return ticketCounts;
	}

	// math work to assign ticket on correct tech, least ticket count and if more
	// than 1 with the same ticket will randomise between these users
	private String assignTicket(int x) {
		tempUse.clear();
		tempUseTwo.clear();
		String tech = null;
		int tickets = 0;
		int low = 100;
		HashMap<String, Integer> ticketCounts = this.getAssignedTicketCount();

		if (x == 1 || x == 2) {
			// Get the list of technicians who can handle severity 1 or 2 tickets
			for (Users y : users) {
				if (y.getUserType() == userType.Level1Tech) {
					tempUse.add(y);
				}
			}

			for (Users z : tempUse) {
				// Check if there is an entry in ticketCounts for the technician, otherwise
				// assign 0 tickets
				if (ticketCounts.containsKey(z.getEmail())) {
					tickets = ticketCounts.get(z.getEmail());
				} else {
					tickets = 0;
				}
				if (tickets <= low) {
					low = tickets;
				}
			}
			for (Users a : tempUse) {
				// Check if there is an entry in ticketCounts for the technician, otherwise
				// assign 0 tickets
				if (ticketCounts.containsKey(a.getEmail())) {
					tickets = ticketCounts.get(a.getEmail());
				} else {
					tickets = 0;
				}
				if (tickets == low) {
					tempUseTwo.add(a);
				}
			}

			// All techs have the same count so randomly assign a technician
			if (tempUseTwo.size() > 1) {
				int size = tempUseTwo.size();
				int random = (int) Math.floor(Math.random() * (size - 1 + 1) + 1);
				tempTech = tempUseTwo.get(random - 1);
				tech = tempTech.getEmail();
			}

			// If there is only one technician with the lowest assign tickets, assign that
			// technician
			else if (tempUseTwo.size() == 1) {
				tempTech = tempUseTwo.get(0);
				tech = tempTech.getEmail();
			}
		} else if (x == 3) {
			// Get the list of technicians who can handle severity 3 tickets
			for (Users y : users) {
				if (y.getUserType() == userType.Level2Tech) {
					tempUse.add(y);
				}
			}
			for (Users z : tempUse) {
				// Check if there is an entry in ticketCounts for the technician, otherwise
				// assign 0 tickets
				if (ticketCounts.containsKey(z.getEmail())) {
					tickets = ticketCounts.get(z.getEmail());
				} else {
					tickets = 0;
				}
				if (tickets <= low) {
					low = tickets;
				}
			}
			for (Users a : tempUse) {
				// Check if there is an entry in ticketCounts for the technician, otherwise
				// assign 0 tickets
				if (ticketCounts.containsKey(a.getEmail())) {
					tickets = ticketCounts.get(a.getEmail());
				} else {
					tickets = 0;
				}
				if (tickets == low) {
					tempUseTwo.add(a);
				}
			}

			// All techs have the same count so randomly assign a technician
			if (tempUseTwo.size() > 1) {
				int size = tempUseTwo.size();
				int random = (int) Math.floor(Math.random() * (size - 1 + 1) + 1);
				tempTech = tempUseTwo.get(random - 1);
				tech = tempTech.getEmail();
			}
			// If there is only one technician with the lowest assign tickets, assign that
			// technician
			else if (tempUseTwo.size() == 1) {
				tempTech = tempUseTwo.get(0);
				tech = tempTech.getEmail();
			}
		}
		return tech;
	}

	// display for tickets matches tickets to the user or tech and prints in a list
	// with an index number
	public void printTickets() {
		tempList.clear();
		int i = 1;
		for (Ticket x : tickets) {
			if (x.getCreator().matches(currentUser.getEmail())) {
				tempList.add(x);
			} else if (x.getAssignedTech().matches(currentUser.getEmail())) {
				tempList.add(x);
			}

		}
		System.out.println("Your tickets are as follows :");
		System.out.println("------------------------------------------------------");
		if (tempList.isEmpty()) {
			System.out.println("No tickets");
		} else {
			// counter mainly used for tech that allows selection of ticket to change
			for (Ticket x : tempList) {
				System.out.println(i + " : " + x);
				i++;
				System.out.println("------------------------------------------------------");
			}
		}
		System.out.println("");

	}

	// validation of menu selection to change ticket status or severity
	public boolean confirmSelection(int x) {
		int j = x - 1;
		boolean found = false;
		if (j < tempList.size() || j == tempList.size()) {
			found = true;
		}
		if (found) {
			return true;
		} else {

			return false;
		}

	}

	// changes status
	// from open to close will add a timer that allows for the archive to work
	// from close to open will remove the counter so that the ticket doesnt get
	// archived
	public void changeTicketStatus(int ticketSelection, int statusSelection, int closedTime) {
		for (Ticket x : tickets) {
			if (tempList.get(ticketSelection - 1).equals(x)) {
				x.setStatus(statusSelection);
				if (closedTime == 2) {
					x.setClosed(LocalDate.now());
					x.setTimeCounter(System.currentTimeMillis());
				} else if (closedTime == 1 && x.getClosed() != null) {
					x.setClosed(null);
					x.setTimeCounter(0);
				}
			}
		}
	}

	// changes ticket severity keeps assigned to same tech
	public void changeTicketSeverityBasic(int ticketSelection, int severitySelection) {
		for (Ticket x : tickets) {
			if (tempList.get(ticketSelection - 1).equals(x)) {
				x.setSeverity(severitySelection);
			}
		}

	}

	// changes severity and reassigns if the ticket goes outside the technicians
	// level
	public void changeTicketSeverityAll(int ticketSelection, int severitySelection) {
		for (Ticket x : tickets) {
			if (tempList.get(ticketSelection - 1).equals(x)) {
				x.setSeverity(severitySelection);
				x.setAssignedTech(assignTicket(severitySelection));
			}
		}
	}

	// admin ticket viewer prints out from selection of days all tickets
	public void printReportDays(LocalDate dayStart, LocalDate dayEnd) {
		int ticketCount = 0;
		int openCount = 0;
		int closedCount = 0;
		// add to selection list from the dates provided
		selectionTickets.clear();
		int i = 1;
		// adds all the tickets within this range from the tickets
		for (Ticket x : tickets) {
			if (x.getCreated().isAfter(dayStart) || x.getCreated().isEqual(dayStart)) {
				if (x.getClosed() != null) {
					if (x.getCreated().isBefore(dayEnd) || x.getCreated().isEqual(dayEnd)) {
						this.selectionTickets.add(x);
						ticketCount++;
						closedCount++;
					}

				} else if (x.getClosed() == null && x.getCreated().isBefore(dayEnd) || x.getCreated().isEqual(dayEnd)) {
					this.selectionTickets.add(x);
					ticketCount++;
					openCount++;
				}

			}
		}
		// adds all the tickets within the range from the archive
		for (Ticket x : archiveTickets) {
			if ((x.getCreated().isAfter(dayStart) || x.getCreated().isEqual(dayStart))
					&& (x.getCreated().isBefore(dayEnd) || x.getCreated().isEqual(dayEnd))) {

				this.selectionTickets.add(x);
				ticketCount++;
				closedCount++;

			}
		}
		System.out.println("Your tickets are as follows :");
		System.out.println("------------------------------------------------------");
		if (selectionTickets.isEmpty()) {
			System.out.println("No tickets");
		} else {
			System.out.println("For the selected period there are :\nTickets submited : " + ticketCount
					+ "\nTickets Open : " + openCount + "\nTickets Closed : " + closedCount);
			System.out.println("------------------------------------------------------");
			for (Ticket x : selectionTickets) {
				if (x.getClosed() != null) {
					long period = ChronoUnit.DAYS.between(x.getCreated(), x.getClosed());
					System.out.println(i + " : " + x + "\n    Date created : " + formatter.format(x.getCreated())
							+ "\n    Date Closed : " + formatter.format(x.getClosed()) + "\n    Days taken : "
							+ period);
					i++;
					System.out.println("------------------------------------------------------");
				} else {
					long period = ChronoUnit.DAYS.between(x.getCreated(), LocalDate.now());
					System.out.println(i + " : " + x + "\n    Date created : " + formatter.format(x.getCreated())
							+ "\n    Date Closed : N/A\n    Days Open : " + period);
					i++;
					System.out.println("------------------------------------------------------");
				}

			}
		}
		System.out.println("");
	}
}
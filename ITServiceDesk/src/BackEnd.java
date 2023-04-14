import java.io.BufferedReader;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class BackEnd {

	List<Users> users = new ArrayList<>();
	List<Ticket> tickets = new ArrayList<>();
	List<Ticket> tempList = new ArrayList<>();
	List<Users> tempUse = new ArrayList<>();
	List<Users> tempUseTwo = new ArrayList<>();
	String validate;
	Users currentUser = null;

	Users tempTech = null;

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
		this.users.add(tech1);
		this.users.add(tech2);
		this.users.add(tech3);
		this.users.add(tech4);
		this.users.add(tech5);
	}

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

	public void loadTickets(String savedTickets) {
		File ticketsFile = new File(savedTickets);
		List<Ticket> currentTickets = new ArrayList<>();
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

	Users loadUser(String[] values, Hashtable<String, String> securityQuestions) {
		return new Users(values[0], values[1], values[2], values[3], userType.Staff, securityQuestions);
	}

	Ticket loadTicket(String[] values) {
		String description = values[0];
		int severity = Integer.parseInt(values[1]);
		String creator = values[2];
		String assignedTech = values[3];
		int status = Integer.parseInt(values[4]);
		return new Ticket(description, severity, creator, assignedTech, status);
	}

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

	public Ticket createTicket(String description, int severity) {
		String creator = currentUser.getEmail();
		String tech = null;
		int status = 1;
		try {
			String assignedTech = assignTicket(severity);
			tech = assignedTech;
		} catch (Exception e) {
			System.out.println("error assigning tech");
		}
		Ticket newTicket = new Ticket(description, severity, creator, tech, status);
		this.tickets.add(newTicket);
		return newTicket;
	}

	public void persistTickets(String savedTickets) {
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(savedTickets);
			oos = new ObjectOutputStream(fos);
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

	public void createStringValidate(Users tech) {
		HashMap<String, Integer> ticketCounts = getAssignedTicketCount();
		validate = tech.getEmail() + "," + tech.getFullName() + "," + tech.getPhoneNum() + "," + tech.getPassword()
				+ "," + tech.getUserType().toString() + ",";
		System.out.println("this is what validate is :" + validate);
	}

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

	private ArrayList<Users> getTechnicians() {
		ArrayList<Users> techs = new ArrayList<Users>();
		for (Users x : this.users) {
			if (x.getUserType() == userType.Level1Tech || x.getUserType() == userType.Level2Tech) {
				techs.add(x);
			}
		}
		return techs;
	}

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
		System.out.println("Your tickets are as following:");
		System.out.println("------------------------------------------------------");
		if (tempList.isEmpty()) {
			System.out.println("No tickets");
		} else {
			for (Ticket x : tempList) {
				System.out.println(i + " : " + x);
				i++;
			}
		}

	}



}

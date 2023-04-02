import java.io.BufferedReader;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BackEnd {

	List<Users> users = new ArrayList<>();
	List<Technician> techs = new ArrayList<>();
	List<Ticket> tickets = new ArrayList<>();
	List<Technician> tempUse = new ArrayList<>();
	List<Technician> tempUseTwo = new ArrayList<>();
	String validate;
	Users currentUser = null;
	Technician currentTech = null;
	Technician tempTech = null;

	public List<Users> load(String savefile) {

		// load the details out of the txt file
		try (BufferedReader br = new BufferedReader(new FileReader(savefile))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				if (values.length == 4) {
					Users user = loadUser(values);
					users.add(user);
				} else {
					Technician tech = loadTech(values);
					techs.add(tech);
				}

			}
		} catch (IOException e) {
			System.out.println("File not found");

		}
		return users;
	}

	public List<Ticket> loadTickets(String savedTickets) {
		try (BufferedReader br = new BufferedReader(new FileReader(savedTickets))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Ticket ticket = loadTicket(values);
				tickets.add(ticket);

			}

		} catch (IOException e) {
			System.out.println("File not found");

		}

		return tickets;
		// TODO Auto-generated method stub

	}

	Technician loadTech(String[] values) {
		String email = values[0];
		String name = values[1];
		int phone = Integer.parseInt(values[2]);
		String password = values[3];
		int techLvl = Integer.parseInt(values[4]);
		int tickets = Integer.parseInt(values[5]);
		return new Technician(email, name, phone, password, techLvl, tickets);
	}

	Users createNewUser(String[] values) {
		String email = values[0];
		String name = values[1];
		int phone = Integer.parseInt(values[2]);
		String password = values[3];
		try (FileWriter f = new FileWriter("users.txt", true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {
			p.println(email + "," + name + "," + phone + "," + password);

		} catch (IOException i) {
			i.printStackTrace();
		}

		return new Users(email, name, phone, password);

	}

	Users loadUser(String[] values) {
		String email = values[0];
		String name = values[1];
		int phone = Integer.parseInt(values[2]);
		String password = values[3];
		return new Users(email, name, phone, password);

	}

	Ticket loadTicket(String[] values) {
		String description = values[0];
		int severity = Integer.parseInt(values[1]);
		String creator = values[2];
		String assignedTech = values[3];
		int status = Integer.parseInt(values[4]);
		return new Ticket(description, severity, creator, assignedTech, status);
	}

	public boolean searchUser(String email) {
		currentUser = null;
		currentTech = null;
		boolean found = false;
		for (Users x : users) {
			if (x.getEmail().matches(email)) {
				currentUser = x;
				found = true;
			}
		}
		for (Technician x : techs) {
			if (x.getEmail().matches(email)) {
				currentTech = x;
				found = true;
			}
		}
		if (found) {
			return true;
		} else {
			return false;
		}
	}

	public String getPass(String email) {
		String pass = null;
		for (Users x : users) {
			if (x.getEmail().matches(email)) {
				pass = x.getPassword();
			}
		}
		return pass;
	}

	public String getPassTwo(String email) {
		String pass = null;
		for (Technician x : techs) {
			if (x.getEmail().matches(email)) {
				pass = x.getPassword();
			}
		}
		return pass;
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

	public boolean validateTech(String email) {
		currentTech = null;
		boolean found = false;
		for (Technician x : techs) {
			if (x.getEmail().matches(email)) {
				currentTech = x;
				found = true;
			}
		}
		if (found) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validatePassTech(String pass) {
		boolean found = false;
		if (currentTech.getPassword().matches(pass)) {
			found = true;
		}
		if (found) {
			return true;
		} else {
			return false;
		}
	}

	public int checkStatus() {
		int x = 0;
		if (currentUser != null) {
			x = 1;
		} else if (currentTech != null) {
			x = 2;
		}
		return x;

	}

	public Ticket createTicket(String discription, int severity) {
		String creator = currentUser.getEmail();
		int status = 1;
		String assignedTech = assignTicket(severity);
		try (FileWriter f = new FileWriter("tickets.txt", true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {
			p.println(discription + "," + severity + "," + creator + "," + assignedTech + "," + status);

		} catch (IOException i) {
			i.printStackTrace();
		}

		return new Ticket(discription, severity, creator, assignedTech, status);

	}

	private void createStringValidate(Technician tech) {
		validate = tech.getEmail() + "," + tech.getFullName() + "," + tech.getPhoneNum() + "," + tech.getPassword()
				+ "," + tech.getTechLvl() + "," + tech.getTicketsOpen();
		System.out.println("this is what validate is :" + validate);
	}

	private String assignTicket(int x) {
		tempUse.clear();
		tempUseTwo.clear();
		String tech = null;
		int tickets = 0;
		int low = 100;
		if (x == 1 || x == 2) {
			for (Technician y : techs) {
				if (y.getTechLvl() == 1) {
					tempUse.add(y);
				}
			}
			for (Technician z : tempUse) {
				tickets = z.getTicketsOpen();
				if (tickets <= low) {
					low = tickets;
				}
			}
			for (Technician a : tempUse) {
				if (a.getTicketsOpen() == low) {
					tempUseTwo.add(a);
				}
			}
			if (tempUseTwo.size() > 1) {
				int size = tempUseTwo.size();
				int random = (int) Math.floor(Math.random() * (size - 1 + 1) + 1);
				tempTech = tempUseTwo.get(random - 1);
				tech = tempTech.getEmail();
				createStringValidate(tempTech);
				updateListTech(tempTech);

			} else if (tempUseTwo.size() == 1) {
				tempTech = tempUseTwo.get(0);
				tech = tempTech.getEmail();
				createStringValidate(tempTech);
				updateListTech(tempTech);

			}
		} else if (x == 3) {
			for (Technician y : techs) {
				if (y.getTechLvl() == 2) {
					tempUse.add(y);
				}
			}
			for (Technician z : tempUse) {
				tickets = z.getTicketsOpen();
				if (tickets <= low) {
					low = tickets;
				}
			}
			for (Technician a : tempUse) {
				if (a.getTicketsOpen() == low) {
					tempUseTwo.add(a);
				}
			}
			if (tempUseTwo.size() > 1) {
				int size = tempUseTwo.size();
				int random = (int) Math.floor(Math.random() * (size - 1 + 1) + 1);
				tempTech = tempUseTwo.get(random - 1);
				tech = tempTech.getEmail();
				createStringValidate(tempTech);
				updateListTech(tempTech);
			} else if (tempUseTwo.size() == 1) {
				tempTech = tempUseTwo.get(0);
				tech = tempTech.getEmail();
				createStringValidate(tempTech);
				updateListTech(tempTech);

			}

		}

		return tech;

	}

	private void updateListTech(Technician tempTech2) {
		int element = 0;
		int tickets = tempTech2.getTicketsOpen();
		for (Technician x : techs) {
			if (x.getEmail() == tempTech2.getEmail()) {
				tickets = tickets + 1;
				x.setTicketsOpen(tickets);
				tempTech = x;
				techs.set(element, x);
				updateTxtTechs();
				xfer();
			}
			element++;
		}

	}

	private void updateTxtTechs() {

		try (FileWriter f = new FileWriter("users2.txt");
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);
				BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contentEquals(validate)) {
					System.out.println("we have a match");
					line = tempTech.getEmail() + "," + tempTech.getFullName() + "," + tempTech.getPhoneNum() + ","
							+ tempTech.getPassword() + "," + tempTech.getTechLvl() + "," + tempTech.getTicketsOpen();
				}
				p.println(line);
			}
			// br.close();
			// f.close();
			// p.close();
			// b.close();
			// File base = new File("users.txt");
			// File current = new File("users2.txt");
			// boolean success = current.renameTo(base);

			// if (!success) {
			// System.out.println("file was not transfered");
			// }
		} catch (IOException e) {
			System.out.println("File not found");

		}
	}

	private void xfer() {
		File base = new File("users.txt");
		File current = new File("users2.txt");
		boolean success = current.renameTo(base);

		if (!success) {
			System.out.println("file was not transfered");
		}

//		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("users.temp")));
//
//				BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				if (line.matches(validate)) {
//					line.replaceAll(validate,
//							tempTech.getEmail() + "," + tempTech.getFullName() + "," + tempTech.getPhoneNum() + ","
//									+ tempTech.getPassword() + "," + tempTech.getTechLvl() + ","
//									+ tempTech.getTicketsOpen());
//				}
//				writer.println(line);
//			}
//			File realName = new File("users.txt");
//			realName.delete(); // remove the old file
//			new File("users.temp").renameTo(realName); // Rename temp file
//
//		}
//
//	}
	}
}

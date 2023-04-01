import java.io.BufferedReader;
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
	Users currentUser = null;
	Technician currentTech = null;

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
}

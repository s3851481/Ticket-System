import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BackEnd {

	private int userCount = 0;
	List<Users> users = new ArrayList<>();

	public List<Users> load(String savefile) {
		// load the details out of the txt file
		try (BufferedReader br = new BufferedReader(new FileReader(savefile))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Users user = loadUser(values);
				users.add(user);

			}
		} catch (IOException e) {
			System.out.println("File not found");

		}
		return users;
	}

	Users createNewUser(String[] values) {
		String email = values[0];
		String name = values[1];
		int phone = Integer.parseInt(values[2]);
		String password = values[3];// TODO Auto-generated method stub
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
		String password = values[3];// TODO Auto-generated method stub
		return new Users(email, name, phone, password);

	}
}

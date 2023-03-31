import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BackEnd {
	
	private int userCount = 0;
	List<Users> users = new ArrayList<>();

	public List<Users> load(String savefile) {
		// load the details out of the txt file
		try (BufferedReader br = new BufferedReader(new FileReader(savefile))) {
			String line;
			// skip the header easily
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Users user = createUser(values);
				users.add(user);

			}
		} catch (IOException e) {
			System.out.println("File not found");

		}
		return users;
	}

	private Users createUser(String[] values) {
		// TODO Auto-generated method stub
		return null;
	}

}

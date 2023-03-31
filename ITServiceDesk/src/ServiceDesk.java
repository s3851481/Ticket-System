import java.util.Scanner;

public class ServiceDesk {

	private String savefile = "users.txt";
	BackEnd be;
	private Scanner scan;
	
	//main loader for the console
	public ServiceDesk(BackEnd backEnd) {
		be = backEnd;
		be.load(savefile);
		System.out.println("Welcome to IT service desk");
		loginMenu();
	}

	//main menu for selection
	private void loginMenu() {
		this.scan = new Scanner(System.in);
		int menuSelection = 0;
		String menu = "**Main Menu**\n";
		menu += "1: Login\n";
		menu += "2: Set up Account\n";
		menu += "3: Exit\n";
		System.out.println(menu);
		System.out.print("Enter Choice\n");
		try {
			menuSelection = Integer.parseInt(scan.nextLine());
			if (menuSelection == 1) {
				login();
			} else if (menuSelection == 2) {
				setUp();
			} else if (menuSelection == 3) {
				System.out.print("Have a nice day :)");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Enter valid choice");
			loginMenu();
		}// TODO Auto-generated method stub
		
	}

	// creation of an account staff members only technicians are already coded into system
	private void setUp() {
		this.scan = new Scanner(System.in);
		String email;
		String name;
		int phoneNumber;
		String password;
		System.out.print("Please enter Email adress\n");
		email = scan.nextLine();
		//validation needed and match from file
		System.out.print("Please enter Full name\n");
		name = scan.nextLine();
		//possle need more validation
		while (name.isBlank()) {
			System.out.println("Must enter name, Please enter first name:");
			name = scan.nextLine();
		}
		System.out.print("Please enter Phone Number\n");
		phoneNumber = Integer.parseInt(scan.nextLine());
		//validation needed for phone number entry
		System.out.print("Please enter Password\n");
		password = scan.nextLine();
		//validation needed for password requirements min 20 char with mix of characters
		String values[] = {email , name , Integer.toString(phoneNumber) , password};
		be.createNewUser(values);
		loginMenu();
		// TODO Auto-generated method stub
		
	}

	private void login() {
		// TODO Auto-generated method stub
		
	}

}

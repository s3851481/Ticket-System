import java.util.Scanner;

public class ServiceDesk {

	private String savefile = "users.txt";
	BackEnd be;
	private Scanner scan;

	// main loader for the console
	public ServiceDesk(BackEnd backEnd) {
		be = backEnd;
		be.load(savefile);
		System.out.println("Welcome to IT service desk");
		loginMenu();
	}

	// main menu for selection
	private void loginMenu() {
		this.scan = new Scanner(System.in);
		int menuSelection = 0;
		String menu = "**Main Menu**\n";
		menu += "1: Login\n";
		menu += "2: Forgotten Password\n";
		menu += "3: Set up Account\n";
		menu += "4: Exit\n";
		System.out.println(menu);
		System.out.print("Enter Choice\n");
		try {
			menuSelection = Integer.parseInt(scan.nextLine());
			if (menuSelection == 1) {
				login();
			} else if (menuSelection == 2) {
				passwordRecovery();
			} else if (menuSelection == 3) {
				setUp();
			} else if (menuSelection == 4) {
				System.out.print("Have a nice day :)");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Enter valid choice");
			loginMenu();
		} // TODO Auto-generated method stub

	}

	private void passwordRecovery() {
		this.scan = new Scanner(System.in);
		String user;
		System.out.println("Please enter email address");
		user = scan.nextLine();
		try {
			if (be.searchUser(user)) {
				System.out.println("Your password is: " + be.getPass(user));
				loginMenu();
			}

		} catch (Exception e) {
			System.out.println("invalid user email");
			loginMenu();
		}

	}

	// creation of an account staff members only technicians are already coded into
	// system
	private void setUp() {
		this.scan = new Scanner(System.in);
		String email;
		String name;
		int phoneNumber;
		String password;
		System.out.println("Please enter Email adress\n");
		email = scan.nextLine();
		// validation needed and match from file
		System.out.println("Please enter Full name\n");
		name = scan.nextLine();
		// possle need more validation
		while (name.isBlank()) {
			System.out.println("Must enter name, Please enter first name:");
			name = scan.nextLine();
		}
		System.out.print("Please enter Phone Number\n");
		phoneNumber = Integer.parseInt(scan.nextLine());
		// validation needed for phone number entry
		System.out.print("Please enter Password\n");
		password = scan.nextLine();
		// validation needed for password requirements min 20 char with mix of
		// characters
		String values[] = { email, name, Integer.toString(phoneNumber), password };
		be.createNewUser(values);
		loginMenu();
		// TODO Auto-generated method stub

	}

	private void login() {
		this.scan = new Scanner(System.in);
		String user;
		String pass;
		System.out.println("Please enter email address");
		user = scan.nextLine();
		try {
			if (be.searchUser(user)) {
				System.out.println("Please enter password");
				pass = scan.nextLine();
				try {
					if (be.validatePass(user, pass)) {
						staffMenu();
						// need to add in tech/staff redirect
					}
				} catch (Exception e) {
					System.out.println("incorrect password!");
					loginMenu();
				}

			}
		} catch (Exception e) {
			System.out.println("invalid user email");
			loginMenu();
		}

	}

	private void staffMenu() {
		this.scan = new Scanner(System.in);
		int menuSelection = 0;
		String menu = "**Welcome Staff Member what would you like to do today**\n";
		menu += "1: Submit ticket\n";
		menu += "2: View tickets\n";
		menu += "3: Exit\n";
		System.out.println(menu);
		System.out.print("Enter Choice\n");
		try {
			menuSelection = Integer.parseInt(scan.nextLine());
			if (menuSelection == 1) {
				ticketCreation();
			} else if (menuSelection == 2) {
				error();
			} else if (menuSelection == 3) {
				System.out.print("Have a nice day :)");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Enter valid choice");
			staffMenu();
		}

		// submit ticket
		// view status of tickets for themselves
		// description
		// severity h m l
		// low medium level 1
		// high level 2
	}

	// ticket notes assigned based on the ticket severity and goes to least number
	// otherwise random
	// closed tickets archived after 24 hour period noone can re-open but can be
	// viewed

	private void error() {
		System.out.println("service not currently available");
		loginMenu();
	}

	private void ticketCreation() {
		System.out.println("service not currently available");
		loginMenu();

	}

	private void techMenu() {
		this.scan = new Scanner(System.in);
		int menuSelection = 0;
		String menu = "**Welcome technician here are you're available tickets please make a selection**\n";
		// auto generate from the tickets class
		// menu += "\n";
		menu += "3: Exit\n";
		System.out.println(menu);
		System.out.print("Enter Choice\n");
		// view tickets even closed within 24 hours
		// change severity
		// change status to closed & resolved
		// change status to closed not resolved
		// view archieved tickets
	}

}

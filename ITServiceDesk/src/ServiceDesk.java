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

	private void setUp() {
		// TODO Auto-generated method stub
		
	}

	private void login() {
		// TODO Auto-generated method stub
		
	}

}

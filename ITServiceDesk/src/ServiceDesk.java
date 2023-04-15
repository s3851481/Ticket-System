import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceDesk {

	private final String savefile = "users";
	private final String savedTickets = "tickets";
	BackEnd be;
	private Scanner scan;

	// main loader for the console
	public ServiceDesk(BackEnd backEnd) {
		be = backEnd;
		be.load(savefile);
		be.loadTickets(savedTickets);
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
		}
	}

	private void passwordRecovery() {
		/*
		 * Use security questions instead of email as we will not have a facility to
		 * change the user's password through an external email. We have access to the
		 * currentUser, so we will cycle through the security questions to see if the
		 * user answers one of the security questions correctly. If yes, reset password.
		 * If no, fail.
		 */
		Scanner scan = new Scanner(System.in);
		boolean resetPassword = false;
		String user;
		System.out.println("Please enter username:");
		user = scan.nextLine();
		if (be.validateUser(user)) {
			if (be.currentUser != null) {
				try {
					Enumeration<String> e = be.currentUser.getSecurityQuestions().keys();

					while (e.hasMoreElements() && !resetPassword) {
						String question = e.nextElement();
						System.out.println(question);
						System.out.println("Enter your response to " + question);
						String response = scan.nextLine();

						if (be.currentUser.getSecurityQuestions().get(question).equals(response)) {
							resetPassword = true;
						}
					}

					if (resetPassword) {
						System.out.println("Please enter a new password.");
						String newPassword = scan.nextLine();
						be.currentUser.setPassword(newPassword);
						be.persistUsers(savefile);
						loginMenu();
					} else {
						System.out.println("Unable to reset password.");
						loginMenu();
					}

				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				} finally {
					scan.close();
				}
			}
		} else {
			System.out.println("Invalid username");
			loginMenu();
		}
	}

	// creation of an account staff members only technicians are already coded into
	// system
	private void setUp() {
		this.scan = new Scanner(System.in);
		String email;
		String name;
		String phoneNumber;
		String password;
		System.out.println("Please enter Email adress");
		email = scan.nextLine();
		while (email.isBlank()) {
			System.out.println("Must enter email");
			email = scan.nextLine();
		}
		Pattern pattern = Pattern.compile("\\S+?@\\S+?\\.com");
		Matcher matcher = pattern.matcher(email);
		// check if user exists
		if (be.userExists(email)) {
			System.out.println("Email already in use");
			setUp();
		} 
		//else if (!matcher.matches()) {
		//System.out.println("invalid email address must contain @ and be .com");
		//setUp();
		//}
		// validation needed and match from file
		System.out.println("Please enter Full name");
		name = scan.nextLine();
		// possible need more validation
		while (name.isBlank()) {
			System.out.println("Must enter name, Please enter name:");
			name = scan.nextLine();
		}
		System.out.println("Please enter Australian Phone Number");
		phoneNumber = scan.nextLine();
		Pattern phone = Pattern.compile(
				"^(?:\\+?(61))? ?(?:\\((?=.*\\)))?(0?[2-57-8])\\)? ?(\\d\\d(?:[- ](?=\\d{3})|(?!\\d\\d[- ]?\\d[- ]))\\d\\d[- ]?\\d[- ]?\\d{3})$");
		Matcher ausPhone = phone.matcher(phoneNumber);
		while (!ausPhone.matches()) {
			System.out.println("Must be australia phone number");
			phoneNumber = scan.nextLine();
			ausPhone = phone.matcher(phoneNumber);
		}
		// validation needed for phone number entry
		System.out.println(
				"Please enter Password minimum 20 characters maximum 30 with atleast 1 upper and lowercase alphanumeric");
		password = scan.nextLine();
		// 20 minimum length 30 maximum at least 1 lower and 1 upper comment out these
		// lines for testing ease
		Pattern pass = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{20,30}$");
		Matcher longPass = pass.matcher(password);
		//while (!longPass.matches()) {
		//	System.out.println(
		//			"Must be minimum 20 characters maximum 30 with atleast 1 upper and lowercase alphanumeric");
		//	password = scan.nextLine();
		//	longPass = pass.matcher(password);
		//}
		// validation needed for password requirements min 20 char with mix of
		// characters
		// String values[] = {email, name, Integer.toString(phoneNumber), password};
		Hashtable<String, String> securityQuestions = this.recordSecurityQuestions();
		// be.createNewUser(values, securityQuestions);
		try {
			be.users.add(new Users(email, name, phoneNumber, password, userType.Staff, securityQuestions));
			be.persistUsers(savefile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loginMenu();
	}

	private Hashtable<String, String> recordSecurityQuestions() {
		this.scan = new Scanner(System.in);
		String question;
		String answer;
		Hashtable<String, String> securityQuestions = new Hashtable<>();

		int i;
		for (i = 1; i <= 3; i++) {
			System.out.println("Please enter security question hint " + i);
			question = scan.nextLine();
			System.out.println("Please enter the answer to question " + i);
			answer = scan.nextLine();
			securityQuestions.put(question, answer);
		}

		return securityQuestions;

	}

	private void login() {
		this.scan = new Scanner(System.in);
		String user;
		String pass;
		System.out.println("Please enter email address");
		user = scan.nextLine();
		try {
			if (be.validateUser(user)) {
				System.out.println("Please enter password");
				pass = scan.nextLine();
				try {
					if (be.validatePass(pass)) {
						if (be.currentUser.getSecurityQuestions() == null) {
							System.out.println("We noticed you do not have any security questions in case you"
									+ " forget your password? Would you like to record some now? (Y/N)");
							String response = scan.nextLine();
							if (response == "Y") {
								recordSecurityQuestions();
							}
						}
						if (be.currentUser.getUserType() == userType.Staff) {
							staffMenu();
						} else if (be.currentUser.getUserType() == userType.Level1Tech
								|| be.currentUser.getUserType() == userType.Level2Tech) {
							techMenu();
						}
					} else {
						System.out.println("incorrect password!");
						loginMenu();
					}

				} catch (Exception e) {
					System.out.println("error has occured");
					loginMenu();
				}
			}
		} catch (Exception e) {
			System.out.println("error has occured");
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
				viewTickets();
				staffMenu();
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

	
	private void viewTickets() {
		be.printTickets();

	}

	private void ticketCreation() {
		String description;
		int severity;
		this.scan = new Scanner(System.in);
		System.out.println("Please describe IT ticket");
		description = scan.nextLine();
		System.out.println("Please enter severity:\n1.low\n2.medium\n3.high");
		try {
			severity = Integer.parseInt(scan.nextLine());
			while (severity < 1 || severity > 3) {
				System.out.println("Please enter valid input");
				severity = Integer.parseInt(scan.nextLine());
			}
			be.createTicket(description, severity);
			be.persistTickets(savedTickets);
		} catch (Exception e) {
			System.out.println("Was not valid input");
			ticketCreation();
		}
		staffMenu();

	}

	private void techMenu() {
		this.scan = new Scanner(System.in);
		int menuSelection = 0;
		String menu = "**Welcome technician here are you're available tickets please make a selection**\n\n";
		menu += "1: view tickets\n";
		menu += "2: change severity\n";
		menu += "3: change status\n";
		menu += "4: Exit\n";
		System.out.println(menu);
		System.out.print("Enter Choice\n");
		try {
			menuSelection = Integer.parseInt(scan.nextLine());
			if (menuSelection == 1) {
				viewTickets();
				techMenu();
			} else if (menuSelection == 2) {
				viewTickets();
				severityChange();
			} else if (menuSelection == 3) {
				viewTickets();
				statusChange();
			} else if (menuSelection == 4) {
				System.out.print("Have a nice day :)");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Enter valid choice");
			techMenu();
		}

		// view tickets even closed within 24 hours
		// change severity
		// change status to closed & resolved
		// change status to closed not resolved
		// view archived tickets
	}

	private void statusChange() {
		this.scan = new Scanner(System.in);
		int ticketSelection = 0;
		System.out.println("Please choose which ticket or any other key to return");
		System.out.print("Enter Choice\n");
		try {
			ticketSelection = Integer.parseInt(scan.nextLine());
			if (be.confirmSelection(ticketSelection)) {
				//System.out.println("works");
				changeStatus(ticketSelection);
				techMenu();
			}else {
				System.out.println("Return to menu");
				techMenu();
			}
		} catch (Exception e) {
			System.out.println("Return to menu");
			techMenu();
		}

	}
	public void changeStatus(int ticketSelection) {
		int i = ticketSelection - 1;
		this.scan = new Scanner(System.in);
		int statusSelection = 0;
		int ticketCurrent = be.tempList.get(i).getStatus();
		String statusString = null;
		if (ticketCurrent == 1) {
			statusString = "Open";
		}else if (ticketCurrent == 2) {
			statusString = "closed & resolved";
		}else if (ticketCurrent == 3) {
			statusString = "closed & unresolved";
		}
		System.out.println("The current status of this ticket is : " + statusString +"\n");
		System.out.println("Please enter the new status of ticket or any other key to return to menu\n");
		System.out.println("1 : Open");
		System.out.println("2 : closed & resolved");
		System.out.println("3 : closed & unresolved");
		try {
			statusSelection = Integer.parseInt(scan.nextLine());
			if (statusSelection == 1) {
				be.changeTicketStatus(ticketSelection, statusSelection);
				be.persistTickets(savedTickets);
				System.out.println("");
				techMenu();
			} else if (statusSelection == 2) {
				be.changeTicketStatus(ticketSelection, statusSelection);
				be.persistTickets(savedTickets);
				System.out.println("");
				techMenu();
			} else if (statusSelection == 3) {
				be.changeTicketStatus(ticketSelection, statusSelection);
				be.persistTickets(savedTickets);
				System.out.println("");
				techMenu();
			} else  {
				System.out.println("Return to menu\n");
				techMenu();
			}
		} catch (Exception e) {
			System.out.println("Return to menu\n");
			techMenu();
		}
		
	}

	private void severityChange() {
		this.scan = new Scanner(System.in);
		int ticketSelection = 0;
		System.out.println("Please choose which ticket or any other key to return");
		//System.out.print("Enter Choice\n");
		try {
			ticketSelection = Integer.parseInt(scan.nextLine());
			if (be.confirmSelection(ticketSelection)) {
				//System.out.println("works");
				changeSeverity(ticketSelection);
				techMenu();
			}else {
				System.out.println("Return to menu");
				techMenu();
			}
		} catch (Exception e) {
			System.out.println("Return to menu");
			techMenu();
		}

	}

	private void changeSeverity(int ticketSelection) {
		int i = ticketSelection - 1;
		this.scan = new Scanner(System.in);
		int severitySelection = 0;
		int ticketCurrent = be.tempList.get(i).getSeverity();
		String severityString = null;
		if (ticketCurrent == 1) {
			severityString = "Low";
		}else if (ticketCurrent == 2) {
			severityString = "Medium";
		}else if (ticketCurrent == 3) {
			severityString = "High";
		}
		System.out.println("The current severity of this ticket is : " + severityString +"\n");
		System.out.println("Please enter the new severity of ticket or any other key to return to menu\n");
		System.out.println("1 : Low");
		System.out.println("2 : Medium");
		System.out.println("3 : High");
		try {
			severitySelection = Integer.parseInt(scan.nextLine());
			if (ticketCurrent == severitySelection) {
				System.out.println("No changes to make");
				techMenu();
			}
			//change if severity stays same assigns to same tech
			else if ((ticketCurrent == 1 || ticketCurrent ==2) && (severitySelection == 1 || severitySelection ==2)) {
				be.changeTicketSeverityBasic(ticketSelection, severitySelection);
				be.persistTickets(savedTickets);
				techMenu();
			}
			// if changing tech teir up
			else if ((ticketCurrent == 1 || ticketCurrent ==2) && (severitySelection == 3)) {
				be.changeTicketSeverityAll(ticketSelection, severitySelection);
				be.persistTickets(savedTickets);
				techMenu();
			} 
			// if changing tech teir down
			else if ((ticketCurrent == 3) && (severitySelection ==1 || severitySelection == 2)) {
				be.changeTicketStatus(ticketSelection, severitySelection);
				be.persistTickets(savedTickets);
				techMenu();
			} 
			
			else  {
				System.out.print("Return to menu");
				techMenu();
			}
		} catch (Exception e) {
			System.out.println("Return to menu");
			techMenu();
		}
		
	}

}

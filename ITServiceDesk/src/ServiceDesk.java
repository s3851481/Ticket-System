import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

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
            Use security questions instead of email as we will not have a facility to change the user's password through
            an external email.
            We have access to the currentUser, so we will cycle through the security questions to see if the user
            answers one of the security questions correctly. If yes, reset password. If no, fail.
        */
        Scanner scan = new Scanner(System.in);
        boolean resetPassword = false;
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
        // validation needed and match from file
        System.out.println("Please enter Full name");
        name = scan.nextLine();
        // possible need more validation
        while (name.isBlank()) {
            System.out.println("Must enter name, Please enter first name:");
            name = scan.nextLine();
        }
        System.out.println("Please enter Phone Number");
        //phoneNumber = Integer.parseInt(scan.nextLine()); // phoneNumber is no longer an int
        phoneNumber = scan.nextLine();
        // validation needed for phone number entry
        System.out.println("Please enter Password");
        password = scan.nextLine();
        // validation needed for password requirements min 20 char with mix of
        // characters
        //String values[] = {email, name, Integer.toString(phoneNumber), password};
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
                            System.out.println("We noticed you do not have any security questions in case you" +
                                    "you forget your password? Would you like to record some now? (Y/N)");
                            String response = scan.nextLine();
                            if (response == "Y") {
                                recordSecurityQuestions();
                            }
                        }
                        if (be.currentUser.getUserType() == userType.Staff) {
                            staffMenu();
                        } else if (be.currentUser.getUserType() == userType.Level1Tech || be.currentUser.getUserType() == userType.Level2Tech) {
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
        // view archived tickets
    }

}

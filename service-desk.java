/* Ticket-System
    Features include: 
1. Account setup Feature
2. Login Feature
3. Forgotten/reset password Feature
4. Submit ticket Feature
5. Ticket Status Feature
6. Assign Ticket Feature
7. Technician Login Feature (?not sure if need)
8. Change Ticket Severity Feature
9. Change Ticket Status Feature
10. Archive Ticket Feature
 */

 import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.Scanner;
 
 public class AccountSetup {
 
     private String username;
     private String password;
     private String email;
 
     public AccountSetup(String username, String password, String email) {
         this.username = username;
         this.password = password;
         this.email = email;
     }
 
     public static void main(String[] args) {
      
      ArrayList<AccountSetup> accountList = readAccountsFromFile();
 
         try (Scanner scanner = new Scanner(System.in)) {
             System.out.println("Enter a number to select a feature:");
             System.out.println("1. Account Setup Feature");
             System.out.println("2. Login Feature");
             System.out.println("3. Forgotten/Reset Password Feature");
             System.out.println("4. Submit Ticket Feature");
             System.out.println("5. Ticket Status Feature");
             System.out.println("6. Assign Ticket Feature");
             System.out.println("7. Technician Login Feature");
             System.out.println("8. Change Ticket Severity Feature");
             System.out.println("9. Change Ticket Status Feature");
             System.out.println("10. Archive Ticket Feature");
             int featureNumber = scanner.nextInt();
 
             switch (featureNumber) {
                 case 1:
                     // Code for Account Setup Feature
                     System.out.println("Selected Account Setup Feature");
                     scanner.nextLine();
                     System.out.print("Enter username: ");
                     String username = scanner.nextLine();
                     System.out.print("Enter password: ");
                     String password = scanner.nextLine();
                     System.out.print("Enter email: ");
                     String email = scanner.nextLine();
 
                     AccountSetup account1 = new AccountSetup(username, password, email);
                     accountList.add(account1);
                     saveAccountToFile(account1);
                     break;
 
                 case 2:
                     // Code for Login Feature
                     System.out.println("Selected Login Feature");
                     System.out.println("Please enter your username:");
                     String username2 = scanner.next();
                     System.out.println("Please enter your password:");
                     String password2 = scanner.next();
 
                     // Check if the entered username and password match a user account in the list
                     boolean found = false;
                     for (AccountSetup account : accountList) {
                         if (account.getUsername().equals(username2) && account.getPassword().equals(password2)) {
                             found = true;
                             System.out.println("Login successful.");
                             break;
                         }
                     }
                     if (!found) {
                         System.out.println("Invalid username or password.");
                     }
                     break;
                     case 3:
                     // Code for Forgotten/Reset Password Feature
                     System.out.println("Selected Forgotten/Reset Password Feature");
                     System.out.println("Please enter your email address:");
                     String email2 = scanner.next();
                 
                     // Find the user account with the matching email address
                     boolean foundEmail = false;
                     for (AccountSetup account : accountList) {
                         if (account.getEmail().equals(email2)) {
                             foundEmail = true;
                             System.out.println("An email with instructions for resetting your password has been sent to " + email2);
                             // Code to send password reset email would go here
                             break;
                         }
                     }
                     if (!foundEmail) {
                         System.out.println("No user account found with email address " + email2);
                     }
                     break;
                 
                 case 4:
                     // Code for Submit Ticket Feature
                     System.out.println("Selected Submit Ticket Feature");
                     break;
                 case 5:
                     // Code for Ticket Status Feature
                     System.out.println("Selected Ticket Status Feature");
                     break;
                 case 6:
                     // Code for Assign Ticket Feature
                     System.out.println("Selected Assign Ticket Feature");
                     break;
                 case 7:
                     // Code for Technician Login Feature
                     System.out.println("Selected Technician Login Feature");
                     break;
                 case 8:
                     // Code for Change Ticket Severity Feature
                     System.out.println("Selected Change Ticket Severity Feature");
                     break;
                 case 9:
                     // Code for Change Ticket Status Feature
                     System.out.println("Selected Change Ticket Status Feature");
                     break;
                 case 10:
                     // Code for Archive Ticket Feature
                     System.out.println("Selected Archive Ticket Feature");
                     break;
                 default:
                     System.out.println("Invalid feature number.");
             }
         }
     }




   private static ArrayList<AccountSetup> readAccountsFromFile() {
      ArrayList<AccountSetup> accountList = new ArrayList<>();
      try {
          Scanner scanner = new Scanner(new File("accounts.txt"));
          while (scanner.hasNextLine()) {
              String line = scanner.nextLine();
              String[] parts = line.split(",");
              String username = parts[0];
              String password = parts[1];
              String email = parts[2];
              AccountSetup account = new AccountSetup(username, password, email);
              accountList.add(account);
          }
          scanner.close();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      return accountList;
  }


   
        
     public static void saveAccountToFile(AccountSetup account) {
      try {
          FileWriter writer = new FileWriter("accounts.txt", true);
          writer.write(account.getUsername() + "," + account.getPassword() + "," + account.getEmail() + "\n");
          writer.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }


  public void setEmail(String email) {
   this.email = email;
}
  private String getEmail() {
   return email;
}

private String getPassword() {
   return password;
}

private String getUsername() {
   return username;
}
}

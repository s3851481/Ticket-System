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


import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AccountSetup {


   private String password;
   private String email;
   private String username; 
   public AccountSetup(String username, String password, String email) {
      this.password = password;
      this.email = email;
      this.username = username;
   }

   public static void main(String[] args) {
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
    
             AccountSetup account = new AccountSetup(username, password, email);
             saveAccountToFile(account);
          

          
       
   
    
             break;
         case 2:
             // Code for Login Feature
             System.out.println("Selected Login Feature");
             break;
         case 3:
             // Code for Forgotten/Reset Password Feature
             System.out.println("Selected Forgotten/Reset Password Feature");
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





   
        
public static void saveAccountToFile(AccountSetup account) {
   try {
      FileWriter writer = new FileWriter("accounts.txt", true);
      writer.write(AccountSetup.getUsername() + "," + AccountSetup.getPassword() + "," + AccountSetup.getEmail() + "\n");
      writer.close();
   } catch (IOException e) {
      e.printStackTrace();
   }
}
private static String getEmail() {
    return null;
}

private static String getPassword() {
    return null;
}

private static String getUsername() {
    return null;
}
 
}

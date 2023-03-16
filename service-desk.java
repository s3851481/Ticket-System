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
         System.out.print("Enter username: ");
         String username = scanner.nextLine();
         System.out.print("Enter password: ");
         String password = scanner.nextLine();
         System.out.print("Enter email: ");
         String email = scanner.nextLine();

         AccountSetup account = new AccountSetup(username, password, email);
         saveAccountToFile(account);
      

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

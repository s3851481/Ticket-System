import java.io.BufferedReader;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class BackEnd {

    List<Users> users = new ArrayList<>();
    List<Technician> techs = new ArrayList<>();
    List<Ticket> tickets = new ArrayList<>();
    List<Technician> tempUse = new ArrayList<>();
    List<Technician> tempUseTwo = new ArrayList<>();
    String validate;
    Users currentUser = null;
    Technician currentTech = null;
    Technician tempTech = null;

    private void initializeTechnicians() {
        Users tech1 = new Users("harry.styles@cinco.com", "Harry Styles", "0356781000", "Password1", userType.Level1Tech, null);
        Users tech2 = new Users("niall,horan@cinco.com", "Niall Horan", "0356781001", "Password1", userType.Level1Tech, null);
        Users tech3 = new Users("Liam.Payne@cinco.com", "Liam Payne", "0356781002", "Password1", userType.Level1Tech, null);
        Users tech4 = new Users("Louis.Tomlinson@cinco.com", "Louis Tomlinson", "0356781003", "Password1", userType.Level2Tech, null);
        Users tech5 = new Users("Zayn.Malik@cinco.com", "Zayn Tomlinson", "0356781004", "Password1", userType.Level2Tech, null);
        this.users.add(tech1);
        this.users.add(tech2);
        this.users.add(tech3);
        this.users.add(tech4);
        this.users.add(tech5);
    }

    public void load(String savefile) {
        File userFile = new File(savefile);
        FileOutputStream fos;
        ObjectOutputStream oos;
        FileInputStream fis;
        ObjectInputStream ois;
        List<Users> users = null;
        if (userFile.exists()) {
            try {
                fis = new FileInputStream(savefile);
                ois = new ObjectInputStream(fis);
                users = (ArrayList<Users>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            this.initializeTechnicians();
            try {
                fos = new FileOutputStream(savefile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(this.users);
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.users = users;
    }

    public void persistUsers(String savefile) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(savefile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.users);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Ticket> loadTickets(String savedTickets) {
        File ticketsFile = new File(savedTickets);
        FileInputStream fis;
        ObjectInputStream ois;
        List<Ticket> tickets = null;
        if (ticketsFile.exists()) {
            try {
                fis = new FileInputStream(savedTickets);
                ois = new ObjectInputStream(fis);
                tickets = (ArrayList<Ticket>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.tickets = tickets;
        return tickets;
    }

    Technician loadTech(String[] values, userType type, Hashtable<String, String> securityQuestions) {
        int techLvl = Integer.parseInt(values[4]);
        int tickets = Integer.parseInt(values[5]);
        return new Technician(values[0], values[1], values[2], values[3], type, securityQuestions, techLvl, tickets);
    }

    Users loadUser(String[] values, Hashtable<String, String> securityQuestions) {
        return new Users(values[0], values[1], values[2], values[3], userType.Staff, securityQuestions);
    }

    Ticket loadTicket(String[] values) {
        String description = values[0];
        int severity = Integer.parseInt(values[1]);
        String creator = values[2];
        String assignedTech = values[3];
        int status = Integer.parseInt(values[4]);
        return new Ticket(description, severity, creator, assignedTech, status);
    }

    public boolean searchUser(String email) {
        currentUser = null;
        currentTech = null;
        boolean found = false;
        for (Users x : users) {
            if (x.getEmail().matches(email)) {
                currentUser = x;
                found = true;
            }
        }
        for (Technician x : techs) {
            if (x.getEmail().matches(email)) {
                currentTech = x;
                found = true;
            }
        }
        return found;
    }

    public String getPass(String email) {
        String pass = null;
        for (Users x : users) {
            if (x.getEmail().matches(email)) {
                pass = x.getPassword();
            }
        }
        return pass;
    }

    public String getPassTwo(String email) {
        String pass = null;
        for (Technician x : techs) {
            if (x.getEmail().matches(email)) {
                pass = x.getPassword();
            }
        }
        return pass;
    }

    public boolean validatePass(String pass) {
        boolean found = false;
        if (currentUser.getPassword().matches(pass)) {
            found = true;
        }
        if (found) {
            return true;
        } else {
            return false;
        }

    }

    public boolean validateUser(String email) {
        currentUser = null;
        boolean found = false;
        for (Users x : users) {
            if (x.getEmail().matches(email)) {
                currentUser = x;
                found = true;
            }
        }

        if (found) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateTech(String email) {
        currentTech = null;
        boolean found = false;
        for (Technician x : techs) {
            if (x.getEmail().matches(email)) {
                currentTech = x;
                found = true;
            }
        }
        if (found) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validatePassTech(String pass) {
        boolean found = false;
        if (currentTech.getPassword().matches(pass)) {
            found = true;
        }
        if (found) {
            return true;
        } else {
            return false;
        }
    }

    public int checkStatus() {
        int x = 0;
        if (currentUser != null) {
            x = 1;
        } else if (currentTech != null) {
            x = 2;
        }
        return x;

    }

    public Ticket createTicket(String discription, int severity) {
        String creator = currentUser.getEmail();
        int status = 1;
        String assignedTech = assignTicket(severity);
        tickets.add(new Ticket(discription, severity, creator, assignedTech, status));
        return new Ticket(discription, severity, creator, assignedTech, status);
    }

    public void persistTickets(String savedTickets) {
        File ticketsFile = new File(savedTickets);
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(savedTickets);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.tickets);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void createStringValidate(Technician tech) {
        validate = tech.getEmail() + "," + tech.getFullName() + "," + tech.getPhoneNum() + "," + tech.getPassword()
                + "," + tech.getTechLvl() + "," + tech.getTicketsOpen();
        System.out.println("this is what validate is :" + validate);
    }

    private String assignTicket(int x) {
        tempUse.clear();
        tempUseTwo.clear();
        String tech = null;
        int tickets = 0;
        int low = 100;
        if (x == 1 || x == 2) {
            for (Technician y : techs) {
                if (y.getTechLvl() == 1) {
                    tempUse.add(y);
                }
            }
            for (Technician z : tempUse) {
                tickets = z.getTicketsOpen();
                if (tickets <= low) {
                    low = tickets;
                }
            }
            for (Technician a : tempUse) {
                if (a.getTicketsOpen() == low) {
                    tempUseTwo.add(a);
                }
            }
            if (tempUseTwo.size() > 1) {
                int size = tempUseTwo.size();
                int random = (int) Math.floor(Math.random() * (size - 1 + 1) + 1);
                tempTech = tempUseTwo.get(random - 1);
                tech = tempTech.getEmail();
                createStringValidate(tempTech);
                updateListTech(tempTech);

            } else if (tempUseTwo.size() == 1) {
                tempTech = tempUseTwo.get(0);
                tech = tempTech.getEmail();
                createStringValidate(tempTech);
                updateListTech(tempTech);

            }
        } else if (x == 3) {
            for (Technician y : techs) {
                if (y.getTechLvl() == 2) {
                    tempUse.add(y);
                }
            }
            for (Technician z : tempUse) {
                tickets = z.getTicketsOpen();
                if (tickets <= low) {
                    low = tickets;
                }
            }
            for (Technician a : tempUse) {
                if (a.getTicketsOpen() == low) {
                    tempUseTwo.add(a);
                }
            }
            if (tempUseTwo.size() > 1) {
                int size = tempUseTwo.size();
                int random = (int) Math.floor(Math.random() * (size - 1 + 1) + 1);
                tempTech = tempUseTwo.get(random - 1);
                tech = tempTech.getEmail();
                createStringValidate(tempTech);
                updateListTech(tempTech);
            } else if (tempUseTwo.size() == 1) {
                tempTech = tempUseTwo.get(0);
                tech = tempTech.getEmail();
                createStringValidate(tempTech);
                updateListTech(tempTech);

            }

        }

        return tech;

    }

    private void updateListTech(Technician tempTech2) {
        int element = 0;
        int tickets = tempTech2.getTicketsOpen();
        for (Technician x : techs) {
            if (x.getEmail() == tempTech2.getEmail()) {
                tickets = tickets + 1;
                x.setTicketsOpen(tickets);
                tempTech = x;
                techs.set(element, x);
                updateTxtTechs();
                xfer();
            }
            element++;
        }

    }

    private void updateTxtTechs() {

        try (FileWriter f = new FileWriter("users2.txt");
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);
             BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contentEquals(validate)) {
                    System.out.println("we have a match");
                    line = tempTech.getEmail() + "," + tempTech.getFullName() + "," + tempTech.getPhoneNum() + ","
                            + tempTech.getPassword() + "," + tempTech.getTechLvl() + "," + tempTech.getTicketsOpen();
                }
                p.println(line);
            }
            // br.close();
            // f.close();
            // p.close();
            // b.close();
            // File base = new File("users.txt");
            // File current = new File("users2.txt");
            // boolean success = current.renameTo(base);

            // if (!success) {
            // System.out.println("file was not transfered");
            // }
        } catch (IOException e) {
            System.out.println("File not found");

        }
    }

    private void xfer() {
        File base = new File("users.txt");
        File current = new File("users2.txt");
        boolean success = current.renameTo(base);

        if (!success) {
            System.out.println("file was not transfered");
        }

//		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("users.temp")));
//
//				BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				if (line.matches(validate)) {
//					line.replaceAll(validate,
//							tempTech.getEmail() + "," + tempTech.getFullName() + "," + tempTech.getPhoneNum() + ","
//									+ tempTech.getPassword() + "," + tempTech.getTechLvl() + ","
//									+ tempTech.getTicketsOpen());
//				}
//				writer.println(line);
//			}
//			File realName = new File("users.txt");
//			realName.delete(); // remove the old file
//			new File("users.temp").renameTo(realName); // Rename temp file
//
//		}
//
//	}
    }
}
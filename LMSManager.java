import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Alyssa Caraballo
 * CEN 3024C - Software Development 1
 * June 19, 2026
 * LMSManager.java
 *
 * This class runs the whole application and will display the menu loop in the terminal.
 * From this we are able to many things such as loading new patrons from files, add/remove patrons,
 * check for errors, and exit the system
 */
public class LMSManager {
    // This list temporarily stores active patrons in RAM memory while running.
    private static final Map<String, Patron> patronMap = new LinkedHashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * method: main
     * parameters: args (String[])
     * return: void
     * purpose: This is a start point for the program that first asks you to type a text file
     * This will then grab the data from the file and start the menu loop
     */
    public static void main(String[] args) {
        System.out.println("\n       WELCOME TO LIBRARY MANAGEMENT SYSTEM       ");

        System.out.print("Please provide the dataset text file to display: ");
        String initialPath = scanner.nextLine().trim();
        loadPatronsFromFile(initialPath);

        // Keeps menu running until librarian wants to exit
        while (true) {
            displayMenu();
            System.out.print("Select option (1-5): ");
            String selection = scanner.nextLine().trim();

            switch (selection) {
                case "1":
                    System.out.print("Enter file path: ");
                    String customPath = scanner.nextLine().trim();
                    loadPatronsFromFile(customPath);
                    break;
                case "2":
                    manuallyAddPatron();
                    break;
                case "3":
                    removePatronById();
                    break;
                case "4":
                    printAllPatrons();
                    break;
                case "5":
                    System.out.println("\nSYSTEM CLOSED! Goodbye :)");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n Invalid, Enter only digits 1-5.");
            }
        }
    }

    /**
     * method: displayMenu
     * parameters: none
     * return: void
     * purpose: Prints and displays menu options for the user
     */
    private static void displayMenu() {
        System.out.println("\n            LMS OPERATIONAL COMMAND MENU          ");
        System.out.println("                                                        ");
        System.out.println("1. Load Patrons from Text File");
        System.out.println("2. Register a New Patron");
        System.out.println("3. Remove/Cancel a Patron Profile using ID");
        System.out.println("4. Display Patron Roster List");
        System.out.println("5. System Termination & Exit");
        System.out.println("                                                           ");
    }

    /**
     * method: loadPatronsFromFile
     * parameters: filePath (String)
     * return: void
     * purpose: This reads a text file line by line, splits data using a dash,
     * checks for errors, and will save active patrons to a list in our memory
     */
    public static void loadPatronsFromFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            System.out.println(" Error, path is empty");
            return;
        }

        int loadCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] segments = line.split("-");
                if (segments.length != 4) {
                    System.out.println(" line format error" + line);
                    continue;
                }

                String id = segments[0].trim();
                String name = segments[1].trim();
                String address = segments[2].trim();
                String fineStr = segments[3].trim();

                // Makes sure when run the data abides by rules
                if (!id.matches("\\d{7}")) {
                    System.out.println(" Warning: Skip '" + id + "'. Must have 7 numbers.");
                    continue;
                }

                double fineVal;
                try {
                    fineVal = Double.parseDouble(fineStr);
                } catch (NumberFormatException nfe) {
                    System.out.println("Warning: Skip '" + id + "'. Invalid fine amount.");
                    continue;
                }

                if (fineVal < 0.0 || fineVal > 250.0) {
                    System.out.println("Warning: Skip '" + id + "'. Fine must be between ($0-$250).");
                    continue;
                }

                // To save patron profile to map
                patronMap.put(id, new Patron(id, name, address, fineVal));
                loadCounter++;
            }
            System.out.println("\n>>> SUCCESS! Loaded [" + loadCounter + "] records from: " + filePath);
            printAllPatrons();

        } catch (IOException ioe) {
            System.out.println("\n Warning: File path could not be read.");
        }
    }

    /**
     * method: manuallyAddPatron
     * parameters: none
     * return: void
     * purpose: Obtains patron info from user, validates, and adds new patron record to our memory
     */
    public static void manuallyAddPatron() {
        System.out.println("\n MANUAL PROFILE REGISTRATION");
        System.out.print("Step 1 - Enter a 7-Digit ID: ");
        String id = scanner.nextLine().trim();

        if (!id.matches("\\d{7}")) {
            System.out.println("Error! must be 7 numbers, TRY AGAIN.");
            return;
        }

        if (patronMap.containsKey(id)) {
            System.out.println("STOP! An account profile matching this ID [" + id + "] already exists.");
            return;
        }

        System.out.print("Step 2 - Enter Patron Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Step 3 - Enter Patron Address: ");
        String address = scanner.nextLine().trim();

        System.out.print("Step 4 - Enter Overdue Fine ($0.00 - $250.00): ");
        String fineStr = scanner.nextLine().trim();

        double fineVal;
        try {
            fineVal = Double.parseDouble(fineStr);
        } catch (NumberFormatException nfe) {
            System.out.println(" Could not read fine amount.");
            return;
        }

        if (fineVal < 0.0 || fineVal > 250.0) {
            System.out.println("Fine must be between $0.00 and $250.00.");
            return;
        }

        patronMap.put(id, new Patron(id, name, address, fineVal));
        System.out.println("\n Profile has been save to list.");
        printAllPatrons();
    }

    /**
     * method: removePatronById
     * parameters: none
     * return: void
     * purpose: User can look up patron using their ID and delete their record permanently
     */
    public static void removePatronById() {
        System.out.println("\nRECORD REMOVAL");
        System.out.print("Enter 7-digit ID of the patron account to delete: ");
        String targetId = scanner.nextLine().trim();

        if (patronMap.containsKey(targetId)) {
            patronMap.remove(targetId);
            System.out.println("\n Patron record matching the ID [" + targetId + "] has been removed.");
            printAllPatrons();
        } else {
            System.out.println("\n Error, ID number not in system.");
        }
    }

    /**
     * method: printAllPatrons
     * parameters: none
     * return: void
     * purpose: Go through the active patrons stored in our memory
     * and displays the details in a list
     */
    public static void printAllPatrons() {
        System.out.println("\n                         CURRENT ACTIVE LMS PATRON ROSTER                             ");

        if (patronMap.isEmpty()) {
            System.out.println(" There are no active records in the system at the moment");
        } else {
            for (Patron p : patronMap.values()) {
                System.out.println("  " + p.toString());
            }
        }
        System.out.println("\n");
    }
}
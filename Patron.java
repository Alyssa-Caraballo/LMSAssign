/**
 * Alyssa Caraballo
 * CEN 3024C - Software Development 2
 * June 19, 2026
 * Patron.java
 *
 * This class acts as a data container that represents an individual
 * library patron object blueprint. Within this class it holds different data for the patron with a clean format
 */
public class Patron {
    private String id;
    private String name;
    private String address;
    private double overdueFine;

    /**
     * method: main
     * parameters: args (String[])
     * return: void
     * purpose: This is the entry point of the program.
     * This takes the text file path I input and loads the library data
     * and then starts permanent loop of the main menu
     */

    public Patron(String id, String name, String address, double overdueFine) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.overdueFine = overdueFine;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getOverdueFine() {
        return overdueFine;
    }

    /**
     * method: toString
     * parameters: none
     * return: String
     * purpose: This allows the patron objects text format to print neatly
     *          making it easy to read on the terminal
     */
    @Override
    public String toString() {
        return String.format("ID: %-10s | Name: %-20s | Address: %-35s | Fines: $%7.2f",
                id, name, address, overdueFine);
    }
}
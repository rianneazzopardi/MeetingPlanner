import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Main application class for the Meeting Management System.
 * 
 * This class serves as the entry point for the meeting management application.
 * It manages the main program flow, including initialization, user interface,
 * and data persistence. The class maintains global references to the main
 * data sets (PersonSet, RoomSet, MeetingSet) and provides functionality for
 * saving and loading application state.
 * 
 * The application provides a console-based user interface with menus for
 * managing people, rooms, meetings, and performing queries. All data is
 * automatically saved upon exit and restored when the application starts.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class Main{

    /** Global reference to the person management system */
    public static PersonSet personSet;
    /** Global reference to the room management system */
    public static RoomSet roomSet;
    /** Global reference to the meeting management system */
    public static MeetingSet meetingSet;

    /** File used for persisting application data between sessions */
    private static final File saveFile = new File("save.sav");

    /** Scanner instance used throughout the application for user input */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Main entry point for the Meeting Management System application.
     * 
     * This method initializes the application by:
     * 1. Configuring the scanner to use line delimiters
     * 2. Creating empty data sets for people, rooms, and meetings
     * 3. Loading any previously saved data from the save file
     * 4. Starting the main user interface
     * 
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args){

        scanner.useDelimiter("\n");

        personSet = new PersonSet();
        roomSet = new RoomSet();
        meetingSet = new MeetingSet();

        load();
        showMenu();
    }



    /**
     * Displays the main application menu and handles user navigation.
     * 
     * This method presents the main menu with options to manage people, rooms,
     * meetings, perform queries, save data, or exit. It continues to display
     * the menu until the user chooses to exit. When the user exits (option 6)
     * or saves (option 5), the application data is automatically saved.
     * 
     * Invalid input is handled gracefully with error messages.
     */
    private static void showMenu(){

        int choice;
        //Displaying the menu
        do {
            System.out.println("Meeting organizer");
            System.out.println("1. Manage people");
            System.out.println("2. Manage Rooms");
            System.out.println("3. Manage Meetings");
            System.out.println("4. Query");
            System.out.println("5. Save");
            System.out.println("6. Exit");
            System.out.println("Please enter your choice: ");
            choice = Utils.readInteger();
            switch (choice) {
                case 1: {
                    personSet.showMenu();
                }
                break;
                case 2: {
                    roomSet.showMenu();
                }
                break;
                case 3: {
                    meetingSet.showMenu();
                }
                break;
                case 4: {
                    queryMenu();
                }
                case 5:
                    //case 6 will save, then exit
                case 6:
                {
                    save();
                }
                break;
                //validation in case the user selects an option not within the menu's scope
                default:
                    System.out.println("Incorrect option, please try again");
            }
        }while(choice!=6);
    }

    /**
     * Displays a query interface for finding meetings by resource.
     * 
     * This method allows users to search for all meetings that involve a specific
     * resource (either a room or a person). The user selects the resource type,
     * provides the identifier (room number or person ID), and the system displays
     * all meetings that include that resource.
     * 
     * If the specified resource doesn't exist, an appropriate error message is shown.
     * The results are displayed using the meeting's toString() method.
     */
    private static void queryMenu(){
        int resourceType;
        System.out.println("Get all meetings with resource");
        System.out.println("Choose which type of resource:");
        System.out.println("1.Room");
        System.out.println("2.Person");
        //user input which type of resource (room or person) they want to display the meetings of
        resourceType = Utils.readInteger();

        Resource r = null;

        switch (resourceType) {
            case 1:
                System.out.println("Enter the number of the room to query");
                r = roomSet.getRoomByNumber(Utils.readInteger());
                //checks if the room exists or not
                if (r == null)
                    System.out.println("Room doesn't exist");
                break;

            case 2:
                System.out.println("Input id of person to query");
                r = personSet.getPersonByID(Main.getScanner().next());
                //checks if person exists or not
                if (r == null)
                    System.out.println("Person doesn't exist");
                break;
            }

            if (r != null){
                //copy r into a final variable to remove lambda error
                Resource finalR = r;
                meetingSet.getSet().stream().filter(m->m.getResources().contains(finalR)).forEach(System.out::println);
            }
    }

    /**
     * Saves all application data to a file using Java serialization.
     * 
     * This method serializes the PersonSet, RoomSet, and MeetingSet objects
     * to a file called "save.sav". This allows the application state to be
     * preserved between sessions. The method uses try-with-resources to ensure
     * proper resource cleanup.
     * 
     * If the save operation succeeds, a success message is displayed.
     * If any errors occur during saving, the stack trace is printed.
     */
    private static void save(){
        try (FileOutputStream fout = new FileOutputStream(saveFile)){
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(personSet);
            out.writeObject(roomSet);
            out.writeObject(meetingSet);
            out.flush();
            out.close();
            System.out.println("Success");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Loads application data from the save file using Java deserialization.
     * 
     * This method attempts to restore the PersonSet, RoomSet, and MeetingSet
     * objects from the "save.sav" file. If the file exists, it deserializes
     * the objects and replaces the empty sets created during initialization.
     * 
     * If the save file doesn't exist (first run) or if any errors occur during
     * loading, appropriate messages are displayed. The method uses try-with-resources
     * for proper resource cleanup.
     */
    private static void load(){
        if (saveFile.exists()){
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile))) {

                personSet = (PersonSet) in.readObject();
                roomSet = (RoomSet) in.readObject();
                meetingSet = (MeetingSet) in.readObject();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //this will happen if the save file is missing or if the program is being run for the 1st time.
            //in this case, the sets will be
            System.out.println("No save file found");
        }
    }

    /**
     * Returns the global Scanner instance used throughout the application.
     * 
     * This method provides access to the shared Scanner object that is used
     * for all user input operations across the application. The scanner is
     * configured with newline delimiters and reads from System.in.
     * 
     * @return the global Scanner instance
     */
    public static Scanner getScanner(){
        return scanner;
    }
}

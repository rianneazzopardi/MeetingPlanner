import java.io.Serializable;
import java.util.*;

/**
 * Abstract base class for managing collections of resources in the meeting management system.
 * 
 * This generic class provides common functionality for storing and managing different types
 * of resources (such as Person and Room objects). It includes a complete user interface
 * system for adding, removing, editing, and displaying resources of a specific type.
 * 
 * Concrete subclasses must implement the abstract methods to provide type-specific
 * behavior for resource management operations and menu customization.
 * 
 * @param <E> the type of Resource this set manages
 * @author Meeting Management System
 * @version 1.0
 */
public abstract class ResourceSet<E extends Resource> implements Serializable{

    /** The set that stores all resources of type E */
    protected Set<E> set = new HashSet<>();

    /**
     * Returns the title for the resource management menu.
     * This title will be displayed as the first line whenever the menu is shown.
     * 
     * @return the menu title for this type of resource
     */
    public abstract String getMenuTitle();

    /**
     * Displays an interactive menu allowing the user to manage resources in this set.
     * 
     * The menu provides options to add, remove, edit, and display all resources.
     * The user can navigate through the menu options until they choose to exit.
     * Invalid input is handled gracefully with error messages.
     */
    public void showMenu(){

        int choice;

        do{
            System.out.println(getMenuTitle());
            System.out.println("1. Add");
            System.out.println("2. Remove");
            System.out.println("3. Edit");
            System.out.println("4. Display all");
            System.out.println("5. Back to main menu");
            System.out.println("Please enter your choice: ");
            choice = Utils.readInteger();
            switch (choice) {
                case 1:
                    addResource();
                    break;
                case 2:
                    removeResource();
                    break;
                case 3:
                    editResource();
                    break;
                case 4:
                    displayAll();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid input, try again");

            }
        } while (choice != 5);
    }

    /**
     * Adds a new resource to this set.
     * 
     * This abstract method must be implemented by concrete subclasses to provide
     * type-specific logic for creating and adding resources, including user input
     * collection and validation.
     */
    public abstract void addResource();

    /**
     * Removes a resource from this set.
     * 
     * This abstract method must be implemented by concrete subclasses to provide
     * type-specific logic for identifying and removing resources, typically by
     * prompting the user for identifying information.
     */
    public abstract void removeResource();

    /**
     * Edits an existing resource in this set.
     * 
     * This abstract method must be implemented by concrete subclasses to provide
     * type-specific logic for modifying resource properties, including user interface
     * for selecting fields to edit and entering new values.
     */
    public abstract void editResource();

    /**
     * Displays all resources in this set.
     * 
     * This method iterates through all resources in the set and displays them
     * using their toDisplayString() method. The resources are displayed in the
     * order they are stored in the set.
     */
    public void displayAll(){
        //sort them by their key (id or room number) and output
        set.stream().forEach(e->System.out.println(e.toDisplayString()));
    }
}

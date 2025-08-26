import java.io.Serializable;
import java.util.*;

/**
 * Abstract UI-backed collection for resources of type {@code E}. Provides a console menu to add,
 * remove, edit, and display resources stored in an internal set.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *     <li>Manage a typed set of resources.</li>
 *     <li>Present a simple CLI for CRUD operations.</li>
 * </ul>
 *
 * @param <E> a {@link Resource} subtype
 */
public abstract class ResourceSet<E extends Resource> implements Serializable{

    protected Set<E> set = new HashSet<>();


    //returns the title of the menu, this will be shows as the 1st line whenever the menu is shown;
    public abstract String getMenuTitle();

    //shows the menu allowing the user to add,edit,remove and display the resources in this set
    /**
     * Shows a console menu for CRUD operations on this resource set.
     * Blocks until the user chooses to exit to the parent menu.
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

    /** Prompts the user for input and adds a resource, handling validation/duplicates. */
    public abstract void addResource();

    /** Prompts the user and removes a resource by its unique identifier. */
    public abstract void removeResource();

    /** Prompts the user and edits a resource's attributes with basic validation. */
    public abstract void editResource();

    public void displayAll(){
        //sort them by their key (id or room number) and output
        set.stream().forEach(e->System.out.println(e.toDisplayString()));
    }
}

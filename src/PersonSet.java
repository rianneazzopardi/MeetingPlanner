import java.util.Iterator;

/**
 * Manages a collection of Person objects in the meeting management system.
 * 
 * This class extends ResourceSet to provide specific functionality for managing
 * people in the system. It implements all the abstract methods from ResourceSet
 * to handle person-specific operations like adding, removing, and editing people
 * with appropriate user interface and validation.
 * 
 * The class ensures that each person has a unique ID and provides methods to
 * search for people by their ID.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class PersonSet extends ResourceSet<Person>{

    /**
     * Returns the menu title for person management.
     * 
     * @return the string "Manage People" which is displayed as the menu title
     */
    @Override
    public String getMenuTitle(){
        return "Manage People";
    }

    /**
     * Adds a new person to the system by collecting user input.
     * 
     * This method prompts the user to enter all required person details
     * (name, surname, ID, and position) and attempts to add the person to the set.
     * If a person with the same ID already exists, the addition will fail and
     * an error message will be displayed.
     */
    @Override
    public void addResource(){
        Person p = new Person();
        //Asks user to input the details
        System.out.println("Please enter the name: ");
        String name = Main.getScanner().next();
        p.setName(name);
        System.out.println("Please enter the surname: ");
        String surname = Main.getScanner().next();
        p.setSurname(surname);
        System.out.println("Please enter the id: ");
        String id = Main.getScanner().next();
        p.setId(id);
        System.out.println("Please enter the position: ");
        String pos = Main.getScanner().next();
        p.setPosition(pos);

        //tries to add a person and fails if the id number is already in use
        if (set.add(p))
            System.out.println("person added successfully");
        else
            System.out.println("Cannot add person because another person already exists with that id number");
    }

    /**
     * Removes a person from the system by searching for their ID.
     * 
     * This method prompts the user for the ID of the person to remove,
     * searches through the set for a person with that ID, and removes them
     * if found. If no person with the specified ID exists, an error message
     * is displayed.
     */
    @Override
    public void removeResource(){
        //Asks user for the id of the person to be removed
        System.out.println("Please enter the id of the person to be removed: ");
        String id = Main.getScanner().next();

        //loops through the set.
        Iterator<Person> iterator = set.iterator();
        while (iterator.hasNext()){
            Person currentPerson = iterator.next();
            if (currentPerson.getId().equals(id)){
                //remove the person, inform the user and return
                iterator.remove();
                System.out.println("Person removed");
                return;
            }
        }

        //person was not found, so we inform the user and return
        System.out.println("Person not found!");
    }

    /**
     * Edits an existing person's details through an interactive menu.
     * 
     * This method prompts the user for the ID of the person to edit, then
     * displays a menu allowing them to modify individual fields (name, surname,
     * ID, or position). The user can continue editing until they choose to save
     * and exit. When editing the ID, the method ensures that the new ID is not
     * already in use by another person.
     */
    @Override
    public void editResource(){
        //Asks user to enter the id in order to search for the person to be edited
        System.out.println("Please enter the id of the person to be edited: ");
        String id = Main.getScanner().next();

        Person p = getPersonByID(id);

        if (p == null) {
                System.out.println("Person not found!");
        } else{
            int ch;
            do{
                //Asks users what details they want to edit
                System.out.println("What details do you want to edit?");
                System.out.println("1. Name");
                System.out.println("2. Surname");
                System.out.println("3. ID");
                System.out.println("4. Position");
                System.out.println("5. Save and exit");
                ch = Utils.readInteger();
                switch (ch) {
                    case 1: {
                        System.out.println("Please enter the new name: ");
                        String n = Main.getScanner().next();
                        p.setName(n);
                    }
                    break;
                    case 2: {
                        System.out.println("Please enter the new surname: ");
                        String sn = Main.getScanner().next();
                        p.setSurname(sn);
                    }
                    break;
                    case 3: {
                        System.out.println("Please enter the new ID: ");
                        String idno = Main.getScanner().next();

                        //check that the id is not in use by another person (with e!=p to account for the chance that the user entered the same id number as before)
                        if(set.stream().anyMatch(e->e.getId().equals(idno) && e!=p)){
                            //inform the user and continue to menu
                            System.out.println("Cannot edit person because another person already exists with that id number");
                            break;
                        }

                        p.setId(idno);
                    }
                    break;
                    case 4: {
                        System.out.println("Please enter the new position: ");
                        String pos = Main.getScanner().next();
                        p.setPosition(pos);
                    }
                    break;
                    case 5:
                        break;
                    default:
                        System.out.println("invalid input, try again");
                }
            } while (ch !=5);
        }
    }

    /**
     * Retrieves a person from the set by their ID.
     * 
     * This method searches through all persons in the set and returns the
     * person object with the specified ID. If no person with the given ID
     * is found, null is returned.
     * 
     * @param s the ID of the person to find
     * @return the Person object with the specified ID, or null if not found
     */
    public Person getPersonByID(String s){
        //loops through the set.
        for (Person e : set){
            if (e.getId().equals(s)){
                //return result
                return e;
            }
        }
        //not found
        return null;
    }



}



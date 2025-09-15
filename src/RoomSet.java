import java.util.*;

/**
 * Manages a collection of Room objects in the meeting management system.
 * 
 * This class extends ResourceSet to provide specific functionality for managing
 * rooms in the system. It implements all the abstract methods from ResourceSet
 * to handle room-specific operations like adding, removing, and editing rooms
 * with appropriate user interface and validation.
 * 
 * The class ensures that each room has a unique room number and validates
 * room capacity constraints. It provides methods to search for rooms by their number.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class RoomSet extends ResourceSet<Room>{

    /**
     * Returns the menu title for room management.
     * 
     * @return the string "Manage Rooms" which is displayed as the menu title
     */
    @Override
    public String getMenuTitle(){
        return "Manage Rooms";
    }

    /**
     * Adds a new room to the system by collecting user input.
     * 
     * This method prompts the user to enter the room number and capacity,
     * validates that the capacity is positive, and attempts to add the room
     * to the set. If a room with the same number already exists, the addition
     * will fail and an error message will be displayed.
     */
    @Override
    public void addResource(){
        System.out.println("Enter room number: ");
        int RN = Utils.readInteger();

        int RC;
        boolean repeatRC;

        do{
            System.out.println("Enter room capacity: ");

            RC = Utils.readInteger();

            repeatRC = RC <= 0;

            if (repeatRC)
                System.out.println("Capacity cannot be 0 or less");
        } while (repeatRC);

        if (set.add(new Room(RN, RC)))
            System.out.println("room added successfully");
        else
            System.out.println("Cannot add room because another room already exists with that room number");
    }

    /**
     * Removes a room from the system by searching for its room number.
     * 
     * This method prompts the user for the room number of the room to remove,
     * searches through the set for a room with that number, and removes it
     * if found. If no room with the specified number exists, an error message
     * is displayed.
     */
    @Override
    public void removeResource(){
        //Asks user for the number of the room to be removed
        System.out.println("Please enter the id of the room to be removed: ");
        int number = Utils.readInteger();

        //loops through the set.
        Iterator<Room> iterator = set.iterator();
        while (iterator.hasNext()){
            Room currentRoom = iterator.next();
            if (currentRoom.getRoomNumber() == number){
                //remove the room, inform the user and return
                iterator.remove();
                System.out.println("Room removed");
                return;
            }
        }

        //room was not found, so we inform the user and return
        System.out.println("Room not found!");
    }

    /**
     * Edits an existing room's details through an interactive menu.
     * 
     * This method prompts the user for the room number of the room to edit,
     * then displays a menu allowing them to modify either the room number
     * or capacity. The user can continue editing until they choose to save
     * and exit. When editing the room number, the method ensures that the new
     * number is not already in use by another room. When editing capacity,
     * it validates that the value is positive.
     */
    @Override
    public void editResource(){
        boolean found = false;
        System.out.println("Enter room number you want to edit: ");
        int roomNumber = Utils.readInteger();

        Room r = getRoomByNumber(roomNumber);

        if (r == null)
            System.out.println("Room not found");
        else {
            int ch;
            do{
                //Asks users what details they want to edit
                System.out.println("What details do you want to edit?");
                System.out.println("1. Room number");
                System.out.println("2. Capacity");
                System.out.println("3. Save and exit");
                ch = Utils.readInteger();
                switch (ch) {
                    case 1: {
                        System.out.println("Please enter the new room number: ");
                        int n = Utils.readInteger();

                        //check that the number is not in use by another room (with e!=r to account for the chance that the user entered the same room number as before)
                        if(set.stream().anyMatch(e->e.getRoomNumber() == n && e!=r)){
                            //inform the user and continue to menu
                            System.out.println("Cannot edit room because another room already exists with that room number");
                            break;
                        }

                        r.setRoomNumber(n);
                    }
                    break;
                    case 2: {
                        System.out.println("Please enter the new capacity: ");
                        int c;
                        boolean repeat;

                        do{
                            System.out.println("Enter room capacity: ");

                            c = Utils.readInteger();

                            repeat = c <= 0;

                            if (repeat)
                                System.out.println("Capacity cannot be 0 or less");
                        } while (repeat);
                        r.setRoomCapacity(c);
                    }
                    break;
                    case 3:
                        break;
                    default:
                        System.out.println("invalid input, try again");
                }

            } while (ch !=3);
        }
    }

    /**
     * Retrieves a room from the set by its room number.
     * 
     * This method searches through all rooms in the set and returns the
     * room object with the specified room number. If no room with the given
     * number is found, null is returned.
     * 
     * @param num the room number of the room to find
     * @return the Room object with the specified room number, or null if not found
     */
    public Room getRoomByNumber(int num){
        //loops through the set.
        for (Room e : set){
            if (e.getRoomNumber() == num){
                //return result
                return e;
            }
        }
        //not found
        return null;
    }


}



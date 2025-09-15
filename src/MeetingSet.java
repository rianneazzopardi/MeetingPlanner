import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import java.util.function.IntSupplier;

/**
 * Manages a collection of Meeting objects in the meeting management system.
 * 
 * This class provides comprehensive functionality for managing meetings including
 * creating, editing, removing, and displaying meetings. It supports different types
 * of meetings (Webinar, Group Meet, Meetup) and handles complex operations such as
 * resource allocation, time scheduling, and conflict detection.
 * 
 * The class includes sophisticated date/time input parsing, resource management
 * user interfaces, and validation to prevent scheduling conflicts between meetings
 * that share the same resources at overlapping times.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class MeetingSet implements Serializable{
    /** List of full month names used for parsing user input (index 0 is empty, months start at index 1) */
    private static final List<String> monthsFull = Arrays.asList("", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december");
    /** List of abbreviated month names used for parsing user input (index 0 is empty, months start at index 1) */
    private static final List<String> monthsAbbreviated = Arrays.asList("", "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec");
    /** The set that stores all meetings in the system */
    private Set<Meeting> set = new HashSet<>();
    /** Counter for generating unique meeting IDs */
    private int nextID = 1;

    /**
     * Displays the main meeting management menu and handles user navigation.
     * 
     * This method presents a menu with options to add, remove, edit, and display
     * meetings. It continues to display the menu until the user chooses to exit.
     * Invalid input is handled gracefully with error messages.
     */
    public void showMenu(){
        int choice;
        do{
            System.out.println("Manage Meetings");
            System.out.println("1. Add");
            System.out.println("2. Remove");
            System.out.println("3. Edit");
            System.out.println("4. Display All");
            System.out.println("5. Exit");
            System.out.println("Please enter your choice: ");
            choice = Utils.readInteger();
            switch (choice) {
                case 1: {
                    addMeeting();
                }
                break;
                case 2: {
                    removeMeeting();
                }
                break;
                case 3: {
                    editMeeting();
                }
                break;
                case 4: {
                    displayAll();
                }
                break;
                case 5: {
                    break;
                }
                default: {
                    System.out.println("invalid option: please try again");
                }
            }
        } while (choice != 5);
    }

    /**
     * Adds a new meeting to the system through an interactive process.
     * 
     * This method guides the user through the process of creating a new meeting:
     * 1. Selects the type of meeting (webinar, group meet, or meetup)
     * 2. Collects start and end times with validation
     * 3. Allows the user to configure resources for the meeting
     * 4. Creates the appropriate meeting object based on type
     * 5. Validates resource constraints and handles conflicts
     * 
     * If the meeting would clash with existing meetings on shared resources,
     * it provides the user with options to edit the meeting to resolve conflicts.
     */
    private void addMeeting(){

        //select type of meeting
        int type;
        boolean repeatTypeMenu;

        do{

            repeatTypeMenu = false;
            System.out.println("select type of meeting");
            System.out.println("1. webinar");
            System.out.println("2. group meet");
            System.out.println("3. meetup");

            type = Utils.readInteger();

            if (type < 1 || type > 3){
                System.out.println("invalid option, try again");
                repeatTypeMenu = true;
            }

        } while (repeatTypeMenu);

        System.out.println("start date and time:");
        Calendar start = inputDateAndTime();

        boolean endTimeRepeat;
        Calendar end;

        do{
            System.out.println("end date and time:");
            end = inputDateAndTime();

            endTimeRepeat = !start.before(end);

            if (endTimeRepeat)
                System.out.println("end time need to be after start time");
        } while (endTimeRepeat);

        boolean resourceError;


        Meeting meeting = null;
        Set<Resource> resources = new HashSet<>();

        do{
            try {
                resourceError = false;
                System.out.println("choose resources:");
                showEditMeetingResourcesMenu(resources);

                //construct meeting depending on the type
                switch (type) {
                    case 1:
                        meeting = new WebinarMeeting(nextID++, resources, start, end);
                        break;
                    case 2:
                        meeting = new GroupMeetMeeting(nextID++, resources, start, end);
                        break;
                    case 3:
                        meeting = new MeetupMeeting(nextID++, resources, start, end);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Couldn't create this meeting due to the following error:");
                System.out.println(e.getMessage());
                System.out.println("try again");
                resourceError = true;
            }
        } while (resourceError);

        set.add(meeting);

        //if it clashes allow the user to edit it.
        if (willClash(meeting)){
            System.out.println("Clashing resources, please make the required changes and try again");
            editMeeting(meeting);
        }
    }

    /**
     * Removes a meeting from the system by its ID.
     * 
     * This method prompts the user for the ID of the meeting to remove,
     * searches for the meeting with that ID, and removes it if found.
     * If no meeting with the specified ID exists, an error message is displayed.
     */
    private void removeMeeting(){

        System.out.println("Enter the ID of the meeting to remove");
        int id = Utils.readInteger();

        Iterator<Meeting> iterator = set.iterator();

        while (iterator.hasNext()){
            if (iterator.next().getId() == id){
                iterator.remove();
                System.out.println("Meeting removed");
                return;
            }
        }

        System.out.println("Meeting not found");
    }

    //edits a meeting
    private void editMeeting(){
        System.out.println("Enter the ID of the meeting to edit");
        int id = Utils.readInteger();

        Iterator<Meeting> iterator = set.iterator();

        Meeting meeting = null;

        while (iterator.hasNext()){
            Meeting currentMeeting = iterator.next();
            if (currentMeeting.getId() == id){
                meeting = currentMeeting;
                break;
            }
        }

        if (meeting == null)
            System.out.println("Meeting not found");
        else{
            editMeeting(meeting);
        }
    }

    private void editMeeting(Meeting meeting){
        boolean repeat = false;

        do{
            int ch;
            do{
                //Asks users what details they want to edit
                System.out.println("What details do you want to edit?");
                System.out.println("1. Start time");
                System.out.println("2. End time");
                System.out.println("3. Resources");
                System.out.println("4. Exit");
                ch = Utils.readInteger();
                switch (ch) {
                    case 1: {
                        System.out.println("Enter the new starting time");
                        meeting.setStartTime(inputDateAndTime());
                    }
                    break;
                    case 2: {

                        System.out.println("Enter the new ending time");
                        meeting.setEndTime(inputDateAndTime());
                    }
                    break;
                    case 3: {
                        System.out.println("Editing the meeting's resources");
                        editResources(meeting);

                    }
                    break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invalid option, try again");
                }
            } while (ch != 4);
            repeat = willClash(meeting);
            if (repeat)
                System.out.println("Clashing resources, please make the required changes and try again");
        } while (repeat);
    }

    /**
     * Displays all meetings in the system sorted by start time.
     * 
     * This method creates a comparator to sort meetings by their start time
     * and then displays each meeting using its toDisplayString() method.
     * The meetings are shown in chronological order.
     */
    public void displayAll(){
        //comparator to sort by start time
        Comparator<Meeting> comp = Comparator.comparing(Meeting::getStartTime);
        //sort the set by start time and output
        set.stream().sorted(comp).forEach(m-> System.out.println(m.toDisplayString()));
    }

    /**
     * Collects date and time input from the user and returns a Calendar object.
     * 
     * This method prompts the user to enter year, month, day, hour, and minute.
     * It performs comprehensive validation:
     * - Ensures numeric values are valid integers
     * - Validates month names (accepts full names, abbreviations, or numbers)
     * - Checks day validity for the specific month and year
     * - Validates hour (0-23) and minute (0-59) ranges
     * 
     * The method continues prompting until valid input is provided for each field.
     * 
     * @return a Calendar object representing the date and time entered by the user
     */
    private Calendar inputDateAndTime(){

        GregorianCalendar c;

        boolean success = false;
        int year = 0;
        do{
            try {
                System.out.println("Year");
                year = Utils.readInteger();
  success = true;
            } catch (InputMismatchException e) {
                //clear the input for next try
                Main.getScanner().next();
                System.out.println("incorrect input, try again");
            }
        } while (!success);

        success = false;
        int month = 0;
        do{
            try {
                System.out.println("Month");
                month = monthToInt(Main.getScanner().next());
                if (month == -1)
                    System.out.println("couldn't understand the month, try again");
                else
                    success = true;
            } catch (InputMismatchException e) {
                //clear the input for next try
                Main.getScanner().next();
                System.out.println("incorrect input, try again");
            }
        } while (!success);


        success = false;
        int day = 0;
        do{
            try {
                System.out.println("Day");
                day = Utils.readInteger();

                //check that the day is valid for this month (to block ex. 30th february or 31st april)
                //Note that the month is subtracted by 1 since
                //the GregorianCalendar stores months starting from 0
                int maxDaysInMonth = new GregorianCalendar(year, month - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH);
                if (maxDaysInMonth < day)
                    System.out.println("day is too big for this month, try again");
                else if (day < 1)
                    System.out.println("day cannot be less than 1");
                else
                    success = true;
            } catch (InputMismatchException e) {
                //clear the input for next try
                Main.getScanner().next();
                System.out.println("incorrect input, try again");
            }

        } while (!success);

        success = false;
        int hour = 0;
        do{
            try {
                System.out.println("hour");
                hour = Utils.readInteger();
                if (hour < 0 || hour > 23)
                    System.out.println("invalid hour, try again");
                else
                    success = true;
            } catch (InputMismatchException e) {
                //clear the input for next try
                Main.getScanner().next();
                System.out.println("incorrect input, try again");
            }
        } while (!success);

        success = false;
        int minute;
        do{
            minute = 0;
            try {
                System.out.println("minute");
                minute = Utils.readInteger();

                if (minute < 0 || minute > 59)
                    System.out.println("invalid minute, try again");
                else
                    success = true;
            } catch (InputMismatchException e) {
                //clear the input for next try
                Main.getScanner().next();
                System.out.println("incorrect input, try again");
            }
        } while (!success);

        //create a new calendar to store the dates. Note that the month is subtracted by 1 since
        //the GregorianCalendar stores months starting from 0
        c = new GregorianCalendar(year, month - 1, day, hour, minute);

        return c;
    }

    /**
     * Converts a string representation of a month to an integer.
     * 
     * This method accepts month input in three formats:
     * - Numeric form (1-12)
     * - Full month names (e.g., "january", "february")
     * - Abbreviated month names (e.g., "jan", "feb")
     * 
     * The conversion is case-insensitive and trims whitespace.
     * 
     * @param s the string representation of the month
     * @return the month as an integer (1-12), or -1 if the month cannot be determined
     */
    private int monthToInt(String s){

        //trim the string to remove any extra whitespace
        s = s.trim();

        //try to see if the month was given directly as a number
        if (s.matches("\\d+")){
            int parsed = Integer.parseInt(s);
            if (parsed < 1)
                System.out.println("month cannot be less than 1");
            else if (parsed > 12)
                System.out.println("month cannot be greater than 12");
            else
                return parsed;
        }

        //convert the string to lowercase
        s = s.toLowerCase();

        //try to resolve using the full names
        int index = monthsFull.indexOf(s);
        if (index != -1)
            return index;

        //resolve using the abbreviated names or return -1 otherwise
        return monthsAbbreviated.indexOf(s);
    }

    //edits the resources for a meeting, retrying if there are any issues
    private void editResources(Meeting meeting){

        boolean repeat = false;

        //create a copy of the meeting's resources to be able to roll back if needed
        Set<Resource> resources = new HashSet<>(meeting.getResources());

        do{
            //let the user edit the resources
            showEditMeetingResourcesMenu(resources);

            try {

                repeat = false;
                //try setting the resources
                meeting.setResources(resources);

                repeat = willClash(meeting);

                if (repeat)
                    System.out.println("Clashing resources, please make the required changes and try again");
            } catch (IllegalArgumentException e) {
                System.out.println("Couldn't save the changes for the resources of this meeting due to the following error:");
                System.out.println(e.getMessage());

                int choice = 0;

                do{
                    System.out.println("1. Make changes and try again");
                    System.out.println("2. Discard changes");

                    choice = Utils.readInteger();
                    switch (choice) {
                        case 1:
                            repeat = true;
                            break;
                        case 2:
                            repeat = false;
                            break;
                        default:
                            System.out.println("invalid option, try again");
                    }
                } while (choice != 1 && choice != 2);
            }
        } while (repeat);

    }

    /**
     * Displays an interactive menu for managing meeting resources.
     * 
     * This method provides a user interface for adding and removing resources
     * (people and rooms) from a meeting. Users can:
     * - Add rooms by specifying room number
     * - Add people by specifying person ID
     * - Remove rooms or people from the meeting
     * - Display all currently assigned resources
     * 
     * The method validates that resources exist before adding them and provides
     * appropriate feedback for all operations.
     * 
     * @param resources the set of resources to be modified
     */
    private void showEditMeetingResourcesMenu(Set<Resource> resources){
        int choice;
        do{
            System.out.println("Manage resources for this meeting");
            System.out.println("1. Add");
            System.out.println("2. Remove");
            System.out.println("3. Display All");
            System.out.println("4. Exit");
            System.out.println("Please enter your choice: ");
            choice = Utils.readInteger();
            switch (choice) {
                case 1: {
                    System.out.println("Choose type of resource");
                    System.out.println("1. room");
                    System.out.println("2. person");
                    int resourceType = Utils.readInteger();

                    if (resourceType == 1){
                        System.out.println("input number of room to add");
                        Room room = Main.roomSet.getRoomByNumber(Utils.readInteger());
                        if (room == null)
                            System.out.println("room doesn't exist");
                        else if (!resources.add(room))
                            System.out.println("this room was already assigned to this meeting");

                    } else if (resourceType == 2){
                        System.out.println("input id of person to add");
                        Person person = Main.personSet.getPersonByID(Main.getScanner().next());
                        if (person == null)
                            System.out.println("person doesn't exist");
                        else if (!resources.add(person))
                            System.out.println("this person was already assigned to this meeting");
                    }
                }
                break;
                case 2: {
                    System.out.println("Choose type of resource");
                    System.out.println("1. room");
                    System.out.println("2. person");
                    int resourceType = Utils.readInteger();
                    if (resourceType == 1){
                        System.out.println("Enter the number of the room to remove");
                        int number = Utils.readInteger();

                        Iterator<Resource> iterator = resources.iterator();

                        while (iterator.hasNext()){
                            Resource nextResource = iterator.next();
                            if (nextResource instanceof Room && ((Room) nextResource).getRoomNumber() == number){
                                iterator.remove();
                                System.out.println("room removed from meeting");
                                break;
                            }
                        }

                        System.out.println("room not found in this meeting");

                    } else if (resourceType == 2){
                        System.out.println("Enter the ID of the person to remove");
                        String ID = Main.getScanner().next();

                        Iterator<Resource> iterator = resources.iterator();

                        while (iterator.hasNext()){
                            Resource nextResource = iterator.next();
                            if (nextResource instanceof Person && ((Person) nextResource).getId().equals(ID)){
                                iterator.remove();
                                System.out.println("person removed from meeting");
                                break;
                            }
                        }

                        System.out.println("person not found in this meeting");
                    }
                }
                break;
                case 3: {
                    for (Resource resource : resources){
                        System.out.println(resource.getClass().getName() + ":" + resource);
                    }
                }
                break;
                case 4: {
                    break;
                }
                default: {
                    System.out.println("invalid option: please try again");
                }
            }
        } while (choice != 4);
    }

    /**
     * Determines if a meeting will have scheduling conflicts with existing meetings.
     * 
     * This method checks if the new meeting would clash with any existing meetings
     * by comparing time overlaps and shared resources. Two meetings clash if:
     * 1. Their time periods overlap (start time of one is before end time of the other)
     * 2. They share at least one common resource (person or room)
     * 
     * When conflicts are found, detailed information about the clashing resources
     * is displayed to help the user understand and resolve the conflicts.
     * 
     * @param newMeeting the meeting to check for conflicts
     * @return true if there are scheduling conflicts, false otherwise
     */
    private boolean willClash(Meeting newMeeting){
        return set.stream().anyMatch(oldMeeting -> {

            //check that oldMeeting and newMeeting aren't the same
            //this will happen if the new meeting was edited not created new
            if (oldMeeting == newMeeting)
                return false;

            //check if the time will overlap
            if (newMeeting.startTime.compareTo(oldMeeting.endTime) < 0 && oldMeeting.startTime.compareTo(newMeeting.endTime) < 0){
                //the times overlapped, so we need to check that no resource is assigned to both meetings
                return newMeeting.getResources().stream().anyMatch(r -> {
                    if (oldMeeting.getResources().contains(r)){
                        //output the clashing resource to the user
                        System.out.printf("Resource %s is already in use by meeting %s at the same time\n", r, oldMeeting);
                        return true;
                    }
                    return false;
                });
            }

            //meetings don't even happen at the same time, no need to check resources.
            return false;
        });
    }

    /**
     * Returns an unmodifiable view of the meetings set.
     * 
     * This method provides read-only access to the internal set of meetings,
     * preventing external modification while allowing iteration and querying.
     * 
     * @return an unmodifiable set containing all meetings in the system
     */
    public Set<Meeting> getSet(){
        return Collections.unmodifiableSet(set);
    }
}

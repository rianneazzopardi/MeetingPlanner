
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class ExampleUsage {
    public static void main(String[] args) {
        // Initialize the sets for people, rooms, and meetings
        Main.personSet = new PersonSet();
        Main.roomSet = new RoomSet();
        Main.meetingSet = new MeetingSet();

        // Create some sample resources
        Person person1 = new Person("John", "Doe", "1", "Manager");
        Person person2 = new Person("Jane", "Smith", "2", "Developer");
        Room room1 = new Room(101, "Conference Room");

        // Add resources to their respective sets
        Main.personSet.add(person1);
        Main.personSet.add(person2);
        Main.roomSet.add(room1);

        // Define the start and end time for the meeting
        Calendar startTime = Calendar.getInstance();
        startTime.set(2023, Calendar.NOVEMBER, 1, 10, 0); // November 1, 2023, 10:00 AM

        Calendar endTime = Calendar.getInstance();
        endTime.set(2023, Calendar.NOVEMBER, 1, 11, 0); // November 1, 2023, 11:00 AM

        // Create a set of resources for the meeting
        Set<Resource> resources = new HashSet<>();
        resources.add(person1);
        resources.add(room1);

        // Create a new meeting
        try {
            Meeting meeting = new MeetupMeeting(Main.meetingSet.getNextID(), resources, startTime, endTime);
            Main.meetingSet.add(meeting);

            // Check for clashes
            if (Main.meetingSet.willClash(meeting)) {
                System.out.println("Clashing resources detected. Please adjust the meeting resources.");
            } else {
                System.out.println("Meeting scheduled successfully.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating meeting: " + e.getMessage());
        }
    }
}

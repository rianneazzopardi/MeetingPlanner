import java.util.Calendar;
import java.util.Set;

/**
 * Represents a group meet meeting in the meeting management system.
 * 
 * A group meet meeting is a specific type of meeting that requires at least 2 people
 * and exactly 1 room. The number of people cannot exceed the room's capacity.
 * This class extends the abstract Meeting class and implements the resource
 * validation requirements specific to group meet meetings.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class GroupMeetMeeting extends Meeting{

    /**
     * Constructs a GroupMeetMeeting with the specified parameters.
     * 
     * @param id the unique identifier for this meeting
     * @param resources the set of resources to allocate (must contain at least 2 people and exactly 1 room)
     * @param startTime the starting time of the meeting
     * @param endTime the ending time of the meeting
     * @throws IllegalArgumentException if the resources don't meet group meet requirements
     */
    public GroupMeetMeeting(int id,Set<Resource> resources, Calendar startTime, Calendar endTime){
        super(id,resources, startTime, endTime);
    }

    /**
     * Validates that the provided resources meet the requirements for a group meet meeting.
     * 
     * A group meet meeting must have exactly 1 room, at least 2 people, and the number
     * of people must not exceed the room's capacity.
     * 
     * @param resources the set of resources to validate
     * @throws IllegalArgumentException if there is not exactly 1 room, fewer than 2 people,
     *         or more people than the room's capacity
     */
    @Override
    protected void checkResources(Set<Resource> resources){
        int people = 0;
        int rooms = 0;

        //stores the one and only room being used.
        Room room = null;

        for (Resource resource : resources){
            if (resource instanceof Person)
                people++;
            else if (resource instanceof Room){
                rooms++;
                room = (Room) resource;
            }
        }

        //The following are errors to be displayed in case the user enters unacceptable data
        if (rooms != 1)
            throw new IllegalArgumentException("The group meet needs to have exactly 1 room");
        else if (people<2)
            throw new IllegalArgumentException("The group meet needs to have more than 2 people");
        else if (people>room.getRoomCapacity())
            throw new IllegalArgumentException("The group meet can't have more people than the room's capacity ("+room.getRoomCapacity()+" people)");
    }
}

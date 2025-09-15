import java.util.Calendar;
import java.util.Set;

/**
 * Represents a meetup meeting in the meeting management system.
 * 
 * A meetup meeting is a specific type of meeting that requires exactly 2 people
 * and exactly 1 room. This class extends the abstract Meeting class and implements
 * the resource validation requirements specific to meetup meetings.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class MeetupMeeting extends Meeting{


    /**
     * Constructs a MeetupMeeting with the specified parameters.
     * 
     * @param id the unique identifier for this meeting
     * @param resources the set of resources to allocate (must contain exactly 2 people and 1 room)
     * @param startTime the starting time of the meeting
     * @param endTime the ending time of the meeting
     * @throws IllegalArgumentException if the resources don't meet meetup requirements
     */
    public MeetupMeeting(int id,Set<Resource> resources, Calendar startTime, Calendar endTime){
        super(id,resources, startTime, endTime);
    }

    /**
     * Validates that the provided resources meet the requirements for a meetup meeting.
     * 
     * A meetup meeting must have exactly 2 people and exactly 1 room.
     * 
     * @param resources the set of resources to validate
     * @throws IllegalArgumentException if there are not exactly 2 people or not exactly 1 room
     */
    @Override
    protected void checkResources(Set<Resource> resources){
        int people = 0;
        int rooms = 0;

        for (Resource resource : resources){
            if (resource instanceof Person)
                people++;
            else if (resource instanceof Room)
                rooms++;
        }
        //Exceptions to be displayed in case invalid data is entered
        if (people!=2)
            throw new IllegalArgumentException("The meetup needs to have exactly 2 people");
        else if (rooms != 1){
            throw new IllegalArgumentException("The meetup needs to have exactly 1 room");
        }
    }
}

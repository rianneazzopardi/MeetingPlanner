import java.util.Calendar;
import java.util.Set;

/**
 * Represents a webinar meeting in the meeting management system.
 * 
 * A webinar meeting is a specific type of meeting that requires at least 1 person
 * and exactly 0 rooms (since webinars are typically conducted online).
 * This class extends the abstract Meeting class and implements the resource
 * validation requirements specific to webinar meetings.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class WebinarMeeting extends Meeting{
    /**
     * Constructs a WebinarMeeting with the specified parameters.
     * 
     * @param id the unique identifier for this meeting
     * @param resources the set of resources to allocate (must contain at least 1 person and 0 rooms)
     * @param startTime the starting time of the meeting
     * @param endTime the ending time of the meeting
     * @throws IllegalArgumentException if the resources don't meet webinar requirements
     */
    public WebinarMeeting(int id,Set<Resource> resources, Calendar startTime, Calendar endTime){
        super(id,resources, startTime, endTime);
    }

    /**
     * Validates that the provided resources meet the requirements for a webinar meeting.
     * 
     * A webinar meeting must have at least 1 person and exactly 0 rooms
     * (since webinars are conducted online without physical room requirements).
     * 
     * @param resources the set of resources to validate
     * @throws IllegalArgumentException if there are fewer than 1 person or any rooms are assigned
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
        //Exception to be displayed in case invalid data is inputted
        if (people<1)
            throw new IllegalArgumentException("The webinar needs at least 1 person");
        else if (rooms >0){
            throw new IllegalArgumentException("The webinar needs to have exactly 0 rooms");
        }
    }
}

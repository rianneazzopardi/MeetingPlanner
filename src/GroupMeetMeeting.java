import java.util.Calendar;
import java.util.Set;

/**
 * Represents a group meeting that requires exactly one {@link Room} and at least two {@link Person}s.
 * This class specializes {@link Meeting} by enforcing resource constraints specific to group meetings
 * and by validating those constraints whenever the meeting is created or its resources are updated.
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>Exactly one room must be present.</li>
 *     <li>At least two people must be present.</li>
 *     <li>The number of people must not exceed the room's capacity.</li>
 * </ul>
 */
public class GroupMeetMeeting extends Meeting{

    /**
     * Constructs a group meeting and validates the provided resources against group meeting rules.
     *
     * @param id the unique meeting identifier
     * @param resources the set of resources (people and room) proposed for this meeting
     * @param startTime the meeting start time
     * @param endTime the meeting end time
     * @throws IllegalArgumentException if the resource constraints are not satisfied
     */
    public GroupMeetMeeting(int id,Set<Resource> resources, Calendar startTime, Calendar endTime){
        super(id,resources, startTime, endTime);
    }

    @Override
    /**
     * Validates that the resources contain exactly one {@link Room}, at least two {@link Person}s,
     * and that the number of people does not exceed the room's capacity.
     *
     * @param resources the set of resources to validate; must not be {@code null}
     * @throws IllegalArgumentException if constraints are violated
     */
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

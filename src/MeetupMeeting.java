import java.util.Calendar;
import java.util.Set;

/**
 * Represents a meetup that requires exactly two {@link Person}s and exactly one {@link Room}.
 * This class specializes {@link Meeting} by enforcing meetup-specific resource constraints and
 * validating them on construction and when resources change.
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>Exactly two people must be present.</li>
 *     <li>Exactly one room must be present.</li>
 * </ul>
 */
public class MeetupMeeting extends Meeting{


    /**
     * Constructs a meetup and validates the provided resources against meetup rules.
     *
     * @param id the unique meeting identifier
     * @param resources the set of resources (people and room) proposed for this meeting
     * @param startTime the meeting start time
     * @param endTime the meeting end time
     * @throws IllegalArgumentException if the resource constraints are not satisfied
     */
    public MeetupMeeting(int id,Set<Resource> resources, Calendar startTime, Calendar endTime){
        super(id,resources, startTime, endTime);
    }

    @Override
    /**
     * Validates that the resources contain exactly two {@link Person}s and exactly one {@link Room}.
     *
     * @param resources the set of resources to validate; must not be {@code null}
     * @throws IllegalArgumentException if constraints are violated
     */
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

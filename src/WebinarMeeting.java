import java.util.Calendar;
import java.util.Set;

/**
 * Represents a webinar meeting that requires at least one {@link Person} and no {@link Room}s.
 * This class specializes {@link Meeting} by enforcing webinar-specific resource constraints and
 * validating them on construction and when resources change.
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>At least one person must be present.</li>
 *     <li>No rooms are allowed.</li>
 * </ul>
 */
public class WebinarMeeting extends Meeting{
    /**
     * Constructs a webinar meeting and validates the provided resources against webinar rules.
     *
     * @param id the unique meeting identifier
     * @param resources the set of resources proposed for this meeting
     * @param startTime the meeting start time
     * @param endTime the meeting end time
     * @throws IllegalArgumentException if the resource constraints are not satisfied
     */
    public WebinarMeeting(int id,Set<Resource> resources, Calendar startTime, Calendar endTime){
        super(id,resources, startTime, endTime);
    }

    @Override
    /**
     * Validates that the resources contain at least one {@link Person} and zero {@link Room}s.
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
        //Exception to be displayed in case invalid data is inputted
        if (people<1)
            throw new IllegalArgumentException("The webinar needs at least 1 person");
        else if (rooms >0){
            throw new IllegalArgumentException("The webinar needs to have exactly 0 rooms");
        }
    }
}

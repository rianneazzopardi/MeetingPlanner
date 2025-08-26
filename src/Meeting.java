import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract base class for meetings. Encapsulates identity, schedule, and assigned resources,
 * and defers type-specific resource validation to subclasses via {@link #checkResources(Set)}.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *     <li>Store meeting identity and time window.</li>
 *     <li>Hold assigned resources and expose them as an unmodifiable view.</li>
 *     <li>Invoke subclass validation for resource constraints on creation and update.</li>
 * </ul>
 */
public abstract class Meeting implements Serializable{

    Set<Resource> resources;
    Calendar startTime,endTime;
    int id;

    /**
     * Constructs a meeting and validates the provided resources via {@link #checkResources(Set)}.
     *
     * @param id the unique meeting identifier
     * @param resources the set of resources proposed for this meeting
     * @param startTime the meeting start time
     * @param endTime the meeting end time
     * @throws IllegalArgumentException if subclass validation fails
     */
    public Meeting(int id,Set<Resource> resources,Calendar startTime,Calendar endTime){
        checkResources(resources);

        this.id = id;
        this.resources=resources;
        this.startTime=startTime;
        this.endTime = endTime;


    }

    //method for subclasses to check that their requirements are met.
    /**
     * Validates the given resources for this meeting type. Implementations must throw
     * {@link IllegalArgumentException} when constraints are not satisfied.
     *
     * @param resources the set of resources to validate; must not be {@code null}
     * @throws IllegalArgumentException if the resources do not satisfy this meeting type's requirements
     */
    protected abstract void checkResources(Set<Resource> resources);


    public Calendar getStartTime(){
        return startTime;
    }

    public void setStartTime(Calendar startTime){
        this.startTime = startTime;
    }

    public Calendar getEndTime(){
        return endTime;
    }

    public void setEndTime(Calendar endTime){
        this.endTime = endTime;
    }

    /**
     * Returns an unmodifiable view of the assigned resources.
     *
     * @return an unmodifiable set of resources
     */
    public Set<Resource> getResources(){
        return Collections.unmodifiableSet(resources);
    }

    /**
     * Replaces the assigned resources after validating them via {@link #checkResources(Set)}.
     *
     * @param resources the new set of resources
     * @throws IllegalArgumentException if validation fails
     */
    public void setResources(Set<Resource> resources){
        checkResources(resources);
        this.resources = resources;
    }

    public int getId(){
        return id;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return id == meeting.id;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");

    @Override
    public String toString(){
        return "ID:" + id + dateFormat.format(startTime.getTime())+ " - " +dateFormat.format(endTime.getTime());
    }

    /**
     * Returns a multi-line string suitable for display, including id, times, and resources.
     *
     * @return a formatted display string for UI output
     */
    public String toDisplayString(){
        return "ID:" + id + "\t" +
                "start time:" + dateFormat.format(startTime.getTime()) + "\t" +
                "end time:" + dateFormat.format(endTime.getTime()) + "\t" +
                "Resources: [\n" + resources.stream().map(Objects::toString).collect(Collectors.joining("\n")) + "\n]";
    }



}


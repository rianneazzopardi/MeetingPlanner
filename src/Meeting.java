import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract base class representing a meeting in the meeting management system.
 * 
 * This class provides the common functionality for all types of meetings, including
 * resource management, time scheduling, and basic operations like equality comparison.
 * Each meeting has a unique ID, a set of allocated resources (people and rooms),
 * and defined start and end times.
 * 
 * Concrete subclasses must implement the checkResources method to enforce
 * specific constraints on the resources required for their meeting type.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public abstract class Meeting implements Serializable{

    /** The set of resources (people and rooms) allocated to this meeting */
    Set<Resource> resources;
    /** The starting time of the meeting */
    Calendar startTime;
    /** The ending time of the meeting */
    Calendar endTime;
    /** The unique identifier for this meeting */
    int id;

    /**
     * Constructs a Meeting with the specified parameters.
     * 
     * @param id the unique identifier for this meeting
     * @param resources the set of resources (people and rooms) to allocate to this meeting
     * @param startTime the starting time of the meeting
     * @param endTime the ending time of the meeting
     * @throws IllegalArgumentException if the resources do not meet the requirements for this meeting type
     */
    public Meeting(int id,Set<Resource> resources,Calendar startTime,Calendar endTime){
        checkResources(resources);

        this.id = id;
        this.resources=resources;
        this.startTime=startTime;
        this.endTime = endTime;
    }

    /**
     * Validates that the provided resources meet the requirements for this meeting type.
     * 
     * This abstract method must be implemented by concrete subclasses to enforce
     * specific resource constraints (e.g., minimum number of people, room requirements).
     * 
     * @param resources the set of resources to validate
     * @throws IllegalArgumentException if the resources do not meet the requirements
     */
    protected abstract void checkResources(Set<Resource> resources);


    /**
     * Gets the starting time of the meeting.
     * 
     * @return the starting time of the meeting
     */
    public Calendar getStartTime(){
        return startTime;
    }

    /**
     * Sets the starting time of the meeting.
     * 
     * @param startTime the new starting time to set
     */
    public void setStartTime(Calendar startTime){
        this.startTime = startTime;
    }

    /**
     * Gets the ending time of the meeting.
     * 
     * @return the ending time of the meeting
     */
    public Calendar getEndTime(){
        return endTime;
    }

    /**
     * Sets the ending time of the meeting.
     * 
     * @param endTime the new ending time to set
     */
    public void setEndTime(Calendar endTime){
        this.endTime = endTime;
    }

    /**
     * Gets an unmodifiable view of the resources allocated to this meeting.
     * 
     * @return an unmodifiable set containing the meeting's resources
     */
    public Set<Resource> getResources(){
        return Collections.unmodifiableSet(resources);
    }

    /**
     * Sets the resources allocated to this meeting.
     * 
     * @param resources the new set of resources to allocate
     * @throws IllegalArgumentException if the resources do not meet the requirements for this meeting type
     */
    public void setResources(Set<Resource> resources){
        checkResources(resources);
        this.resources = resources;
    }

    /**
     * Gets the unique identifier of this meeting.
     * 
     * @return the meeting's unique identifier
     */
    public int getId(){
        return id;
    }

    /**
     * Indicates whether some other object is "equal to" this meeting.
     * Two meetings are considered equal if they have the same ID.
     * 
     * @param o the reference object with which to compare
     * @return true if this object is the same as the o argument; false otherwise
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return id == meeting.id;
    }

    /**
     * Returns a hash code value for this meeting.
     * The hash code is based on the meeting's ID.
     * 
     * @return a hash code value for this meeting
     */
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    /** Date formatter for displaying meeting times in a readable format */
    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");

    /**
     * Returns a string representation of this meeting.
     * The format is: "ID:id startTime - endTime"
     * 
     * @return a formatted string containing the meeting's basic information
     */
    @Override
    public String toString(){
        return "ID:" + id + dateFormat.format(startTime.getTime())+ " - " +dateFormat.format(endTime.getTime());
    }

    /**
     * Returns a detailed formatted string suitable for display purposes.
     * 
     * The format includes the meeting ID, start time, end time, and a detailed
     * list of all allocated resources.
     * 
     * @return a detailed formatted string containing all meeting information
     */
    public String toDisplayString(){
        return "ID:" + id + "\t" +
                "start time:" + dateFormat.format(startTime.getTime()) + "\t" +
                "end time:" + dateFormat.format(endTime.getTime()) + "\t" +
                "Resources: [\n" + resources.stream().map(Objects::toString).collect(Collectors.joining("\n")) + "\n]";
    }



}


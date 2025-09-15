import java.util.Objects;

/**
 * Represents a room resource in the meeting management system.
 * 
 * This class extends Resource and implements Comparable to allow rooms to be used
 * as resources in meetings and to be sorted by their room number. Each room has a
 * unique room number and a capacity indicating the maximum number of people it can hold.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class Room extends Resource implements Comparable<Room>{

    /** The unique identifier for this room */
    private int roomNumber;
    /** The maximum number of people this room can accommodate */
    private int roomCapacity;

    /**
     * Default constructor for Room.
     * Creates a Room object with room number and capacity initialized to 0.
     */
    public Room(){
    }

    /**
     * Constructs a Room with the specified room number and capacity.
     * 
     * @param roomNumber the unique identifier for this room
     * @param roomCapacity the maximum number of people this room can accommodate
     */
    public Room(int roomNumber, int roomCapacity) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
    }

    /**
     * Gets the room number.
     * 
     * @return the unique identifier for this room
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * Gets the room capacity.
     * 
     * @return the maximum number of people this room can accommodate
     */
    public int getRoomCapacity() {
        return roomCapacity;
    }

    /**
     * Sets the room number.
     * 
     * @param roomNumber the new room number to set
     */
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Sets the room capacity.
     * 
     * @param roomCapacity the new capacity to set (maximum number of people)
     */
    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    /**
     * Returns a string representation of this room.
     * The format is: "RoomNumber (capacity: Capacity)"
     * 
     * @return a formatted string containing the room number and capacity
     */
    public String toString(){
        return roomNumber + " (capacity: " +roomCapacity+")";
    }

    /**
     * Returns a formatted string suitable for display purposes.
     * The format is tab-separated: "roomNumber:value\troomCapacity:value"
     * 
     * @return a tab-separated string containing the room number and capacity
     */
    @Override
    public String toDisplayString(){
        return "roomNumber:" + roomNumber + "\t"+
                "roomCapacity:" + roomCapacity;
    }

    /**
     * Indicates whether some other object is "equal to" this room.
     * Two rooms are considered equal if they have the same room number.
     * 
     * @param o the reference object with which to compare
     * @return true if this object is the same as the o argument; false otherwise
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber;
    }

    /**
     * Returns a hash code value for this room.
     * The hash code is based on the room number.
     * 
     * @return a hash code value for this room
     */
    @Override
    public int hashCode(){
        return Objects.hash(roomNumber);
    }

    /**
     * Compares this room with the specified room for order.
     * Returns a negative integer, zero, or a positive integer as this room's number
     * is less than, equal to, or greater than the specified room's number.
     * 
     * @param o the room to be compared
     * @return a negative integer, zero, or a positive integer as this room's number
     *         is less than, equal to, or greater than the specified room's number
     * @throws NullPointerException if the specified room is null
     */
    @Override
    public int compareTo(Room o){
        return Integer.compare(roomNumber,o.roomNumber);
    }
}



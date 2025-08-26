import java.util.Objects;

/**
 * Represents a room resource with a unique room number and a capacity.
 */
public class Room extends Resource implements Comparable<Room>{

    private int roomNumber;
    private int roomCapacity;

    public Room(){
    }

    public Room(int roomNumber, int roomCapacity) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String toString(){
        return roomNumber + " (capacity: " +roomCapacity+")";
    }

    @Override //display the roomNumber and roomCapacity
    /**
     * Returns a user-friendly, single-line display string with room number and capacity.
     *
     * @return a concise room summary
     */
    public String toDisplayString(){
        return "roomNumber:" + roomNumber + "\t"+
                "roomCapacity:" + roomCapacity;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber;
    }

    @Override
    public int hashCode(){
        return Objects.hash(roomNumber);
    }

    @Override
    /**
     * Compares rooms by their {@code roomNumber}.
     *
     * @param o the other room
     * @return comparison result by room number
     */
    public int compareTo(Room o){
        return Integer.compare(roomNumber,o.roomNumber);
    }
}



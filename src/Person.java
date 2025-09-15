import java.util.Objects;

/**
 * Represents a person in the meeting management system.
 * 
 * This class extends Resource and implements Comparable to allow persons to be used
 * as resources in meetings and to be sorted by their ID. Each person has a name, 
 * surname, unique identifier, and position within the organization.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class Person extends Resource implements Comparable<Person>{
    /** The person's first name */
    private String name;
    /** The person's surname */
    private String surname;
    /** The person's unique identifier */
    private String id;
    /** The person's position within the organization */
    private String position;

    /**
     * Default constructor for Person.
     * Creates a Person object with all fields initialized to null.
     */
    public Person(){
    }

    /**
     * Constructs a Person with the specified details.
     * 
     * @param name the person's first name
     * @param surname the person's surname
     * @param id the person's unique identifier
     * @param position the person's position within the organization
     */
    public Person(String name, String surname, String id, String position){
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.position = position;
    }

    /**
     * Gets the person's first name.
     * 
     * @return the person's first name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the person's first name.
     * 
     * @param name the new first name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the person's surname.
     * 
     * @return the person's surname
     */
    public String getSurname(){
        return surname;
    }

    /**
     * Sets the person's surname.
     * 
     * @param surname the new surname to set
     */
    public void setSurname(String surname){
        this.surname = surname;
    }

    /**
     * Gets the person's unique identifier.
     * 
     * @return the person's unique identifier
     */
    public String getId(){
        return id;
    }

    /**
     * Sets the person's unique identifier.
     * 
     * @param id the new unique identifier to set
     */
    public void setId(String id){
        this.id = id;
    }
    /**
     * Gets the person's position within the organization.
     * 
     * @return the person's position
     */
    public String getPosition(){
        return position;
    }
    /**
     * Sets the person's position within the organization.
     * 
     * @param position the new position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Returns a string representation of this person.
     * The format is: "FirstName Surname(id: ID) - Position"
     * 
     * @return a formatted string containing the person's details
     */
    @Override
    public String toString() {
        return name+" "+surname + "(id: "+id+")" + " - " + position;
    }

    /**
     * Returns a formatted string suitable for display purposes.
     * The format is tab-separated: "ID:value\tName:value\tSurname:value\tPosition:value\t"
     * 
     * @return a tab-separated string containing all person details
     */
    @Override
    public String toDisplayString(){
        return "ID:" + id + "\t" +
                "Name:" + name + "\t" +
                "Surname:" + surname + "\t" +
                "Position: " + position + "\t";
    }

    /**
     * Indicates whether some other object is "equal to" this person.
     * Two persons are considered equal if they have the same ID.
     * 
     * @param o the reference object with which to compare
     * @return true if this object is the same as the o argument; false otherwise
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    /**
     * Returns a hash code value for this person.
     * The hash code is based on the person's ID.
     * 
     * @return a hash code value for this person
     */
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }


    /**
     * Compares this person with the specified person for order.
     * Returns a negative integer, zero, or a positive integer as this person's ID
     * is less than, equal to, or greater than the specified person's ID.
     * 
     * @param o the person to be compared
     * @return a negative integer, zero, or a positive integer as this person's ID
     *         is less than, equal to, or greater than the specified person's ID
     * @throws NullPointerException if the specified person is null
     */
    @Override
    public int compareTo(Person o){
        return id.compareTo(o.id);
    }
}

import java.io.Serializable;

/**
 * Abstract base class representing a resource in the meeting management system.
 * 
 * This class serves as the foundation for all types of resources that can be
 * allocated to meetings, such as people and rooms. All resources must be
 * serializable to support persistence functionality.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public abstract class Resource implements Serializable{
    
    /**
     * Returns a formatted string representation of this resource suitable for display.
     * Each concrete implementation should provide a meaningful display format
     * that includes the resource's key identifying information.
     * 
     * @return a formatted string containing the resource's display information
     */
    public abstract String toDisplayString();
}

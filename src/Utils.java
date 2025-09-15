import java.util.InputMismatchException;

/**
 * Utility class providing common helper methods for the meeting management system.
 * 
 * This class contains static utility methods that are used throughout the application
 * to perform common operations such as input validation and parsing.
 * 
 * @author Meeting Management System
 * @version 1.0
 */
public class Utils{
    
    /**
     * Reads an integer from the standard input using the main scanner.
     * 
     * This method continuously prompts the user for input until a valid integer
     * is entered. If the user inputs something that is not an integer, it will
     * display an error message and ask the user to try again.
     * 
     * @return the integer value entered by the user
     * @throws RuntimeException if there are issues with the input stream
     */
    public static int readInteger(){
        while (true){
            try {
                return Main.getScanner().nextInt();
            } catch (InputMismatchException e) {
                //discard next token to avoid infinite loop
                Main.getScanner().next();
                System.out.println("invalid input, try again");
            }
        }
    }
}

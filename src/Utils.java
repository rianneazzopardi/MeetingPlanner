import java.util.InputMismatchException;

/**
 * Console input utilities.
 */
public class Utils{
    //utility method to read an integer from Main.getScanner();
    //if the user inputs something that is not an integer, it
    //will ask the user to try again
    /**
     * Reads an integer from the shared scanner, reprompting on invalid input.
     *
     * @return the next valid integer
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

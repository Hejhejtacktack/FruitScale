/**
 * Handles all input from keyboard.
 *
 * @author Erik Hillborg, erik.hillborg@iths.se
 */

import java.util.*;

public class InputDevice{
    private final Scanner scanner;

    public InputDevice() {
        this.scanner = new Scanner(System.in);
    }

    public String readString() throws InputMismatchException {
        String input = this.scanner.nextLine().trim();

        if (input.isBlank()) {
            throw new InputMismatchException();
        }

        return input;
    }

    /**
     * Tries to read input as a string.
     * @return user's input as string.
     */
    public String readName() throws InputMismatchException {
        String name = scanner.nextLine().trim();

        if (isDouble(name) || name.isBlank()) {
            throw new InputMismatchException();
        }

        return name;
    }

    /**
     * Converts user's input to a positive number.
     * @return user's input as double if input is not blank, otherwise 0.
     */
    public double readPrice() throws InputMismatchException {
        String strPrice = scanner.nextLine().trim();

        if (strPrice.isBlank()) {
            return 0;
        } else if (!isDouble(strPrice)) {
            throw new InputMismatchException();
        }

        return Double.parseDouble(strPrice);
    }

    /**
     * Reads yes and no input.
     * @return True if input is 'y' and false if input is 'n'.
     * @throws InputMismatchException
     */
    public boolean readYesOrNo() throws InputMismatchException {
        String input = scanner.nextLine().trim();

        switch (input) {
            case "y", "Y" -> {
                return true;
            }
            case "n", "N" -> {
                return false;
            }
            default -> throw new InputMismatchException();
        }
    }

    /**
     * Tries to read input as string and trims away all non-characters.
     * @return A trimmed String.
     * @throws IllegalArgumentException
     */
    public String readProductGroups() throws IllegalArgumentException {
        String strProductGroup = scanner.nextLine().trim();

        if (isDouble(strProductGroup)) {
            throw new IllegalArgumentException();
        }

        // Removes all tokens that is not a character and a comma
        strProductGroup = strProductGroup.replaceAll("[^A-Za-z0-9,]", "");

        return strProductGroup;
    }

    /**
     * Tries to read input as an integer.
     * @return An int.
     * @throws InputMismatchException
     */
    public int readInteger() throws InputMismatchException {
        String input = scanner.nextLine().trim();

        if (!isInteger(input) || input.isBlank()) {
            throw new InputMismatchException();
        }

        return Math.abs(Integer.parseInt(input));
    }

    /**
     * Tries to read input as a double.
     * @return A double.
     */
    public double readDouble() {
        String input = scanner.nextLine().trim();

        if (!isDouble(input) || input.isBlank()) {
            throw new InputMismatchException();
        }

        return Math.abs(Double.parseDouble(input));
    }

    /**
     * Checks if argument is a double.
     * @param string the string to be checked.
     * @return true if argument is a double, otherwise false.
     */
    private boolean isDouble(String string) {
        try {
            double number = Double.parseDouble(string);
            //System.out.println("Input is double.");
            return true;
        } catch (NumberFormatException nFE) {
            //System.out.println(nFE + "Input is not double.");
            return false;
        }
    }

    /**
     * Checks if argument is an int.
     * @param string the string to be checked.
     * @return true if argument is an int, otherwise false.
     */
    private boolean isInteger(String string) {
        try {
            int number = Integer.parseInt(string);
            //System.out.println("Input is integer.");
            return true;
        } catch (NumberFormatException nFE) {
            //System.out.println(nFE + "Input is not integer.");
            return false;
        }
    }
}

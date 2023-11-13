import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputDevice{
    private final Scanner scanner;
    // TODO ingen outputdevice i inputdevice
    private final OutputDevice outputDevice;

    public InputDevice() {
        this.scanner = new Scanner(System.in);
        this.outputDevice = new OutputDevice(Authentication.LOGGED_OUT);
    }

    public String readString() {
        return this.scanner.nextLine().trim();
    }

    /**
     * Prints name prompt and reads user's input. Forces user to enter a string.
     * Loops if input is a number and if input is blank.
     * @return user's input as string.
     */
    public String readName() {
        String name;
        do {
            this.outputDevice.print("Enter the products name");
            this.outputDevice.print("> ");
            name = scanner.nextLine().trim();

            if (name.isBlank()) {
                System.out.println("Error! Name is required.");
            }
        } while (isDouble(name) || name.isBlank());

        return name;
    }

    /**
     * Prints price prompt and reads user's input.
     * Loops if input isn't a number.
     * Converts user's input to a positive number.
     * @return user's input as double if input is not blank, otherwise 0.
     */
    public Price readPrice() {
        String strPrice;
        boolean pricePerKg;

        this.outputDevice.print("Enter the product's price");
        this.outputDevice.print("> ");
        strPrice = scanner.nextLine().trim();

        if (!strPrice.isBlank()) {
            pricePerKg = readPricePerKg();
        } else {
            strPrice = "0";
            pricePerKg = false;
        }

        return new Price(Double.parseDouble(strPrice), pricePerKg);
    }

    /**
     * Asks if price is per kilo and reads user's input.
     * @return true if price is per kilo, otherwise false.
     */
    public boolean readPricePerKg() {
        do {
            this.outputDevice.print("Is the price in kr/kg? (y/n)");
            this.outputDevice.print("> ");
            String pricePerKilo = scanner.nextLine().trim();

            if (pricePerKilo.equalsIgnoreCase("y")) {
                return true;
            } else if (pricePerKilo.equalsIgnoreCase("n")) {
                return false;
            }
        } while (true);
    }

    /**
     * Prints product group prompt and reads user's input.
     * Loops if input is a number.
     * @return user's input as list if input is not blank, otherwise null.
     */
    public List<String> readProductGroups() {
        String strProductGroup;
        List<String> productGroups;
        do {
            this.outputDevice.print("Enter the products categories\n" +
                    "If more than one, separate with comma (fruit, exotic, citrus etc.)");
            this.outputDevice.print("> ");
            strProductGroup = scanner.nextLine().trim();
            if (strProductGroup.isBlank()) {
                return null;
            }
            // Removes all tokens that is not a character and a comma
            strProductGroup = strProductGroup.replaceAll("[^A-Za-z0-9,]", "");
            // Initializes the list to an ArrayList and
            // splits the input string into separate elements by ',' and adds them to the Arraylist
            productGroups = new ArrayList<>(Arrays.asList(strProductGroup.split(",")));

            // Removes alla empty elements
            productGroups.removeAll(Arrays.asList("", null));

        } while (isDouble(strProductGroup));

        return productGroups;
    }

    /**
     * Prints quantity prompt and reads user's input.
     * Converts user's input to a positive number.
     * @return user's input as an int if input is not blank, otherwise 0;
     */
    public int readQuantity() {
        String strQuantity;
        do {
            this.outputDevice.print("Enter an amount to calculate total price");
            this.outputDevice.print("> ");
            strQuantity = scanner.nextLine().trim();

            if (strQuantity.isBlank()) {
                return 0;
            }

        } while (!isInteger(strQuantity));

        return Math.abs(Integer.parseInt(strQuantity));
    }

    /**
     * Prints weight prompt and reads user's input.
     * Converts user's input to a positive number.
     * @return user's input as an double if input is not blank, otherwise 0;
     */
    public double readWeight() {
        String strWeight;
        do {
            this.outputDevice.print("Enter a weight to calculate total price");
            this.outputDevice.print("> ");
            strWeight = scanner.nextLine().trim();

            if (strWeight.isBlank()) {
                return 0;
            }

        } while (!isDouble(strWeight));

        return Math.abs(Double.parseDouble(strWeight));
    }

    public String readPassword() {
        String password;
        do {
            this.outputDevice.print("Enter password");
            this.outputDevice.print("> ");
            password = scanner.nextLine().trim();
        } while (password.isBlank());

        return password;
    }

    public String readUsername() {
        String username;
        do {
            this.outputDevice.print("Enter username");
            this.outputDevice.print("> ");
            username = scanner.nextLine().trim();
        } while (username.isBlank());

        return username;
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

/**
 * Handles all printing to screen.
 *
 * @author Erik Hillborg, erik.hillborg@iths.se
 */

import java.util.List;

public class OutputDevice {
    private static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private String textColor;

    public OutputDevice(Authentication userStatus) {
        switchSession(userStatus);
    }

    /**
     * Sets text color based on parameter.
     * @param userStatus Status of the session.
     */
    public void switchSession(Authentication userStatus) {
        switch (userStatus) {
            case LOGGED_IN -> this.textColor = ANSI_GREEN;
            case LOGGED_OUT -> this.textColor = ANSI_RESET;
        }
    }

    /**
     * Sets upp the program.
     */
    public void initialize() {
        System.out.println();
        System.out.println("Starting program...");
        System.out.println("Welcome to the fruit scale. You will now get multiple choices.");
    }

    /**
     * Prints to console.
     * @param string String to be printed.
     */
    public void print(String string) {
        if (string.equals("> ")) {
            System.out.print(string);
        } else {
            System.out.println(string);
        }
    }

    /**
     * Prints customer menu.
     */
    public void printCustomerMenu() {
        System.out.println(this.textColor);
        System.out.print("""
                \tMain menu
                Please enter the corresponding integer of your choice.
                Enter Q to exit the program.
                """);
        System.out.print("""
                1. Show all products
                2. Show promotional products
                3. Search for product by name
                4. Search for product by category
                5. View cart
                6. Checkout
                7. Login as employee
                Your choice
                """);
        System.out.print("> ");
    }

    /**
     * Prints employee menu.
     */
    public void printEmployeeMenu() {
        System.out.println(this.textColor);
        System.out.print("""
                \tEmployee menu
                Please enter the corresponding integer of your choice.
                Enter Q to exit the program.
                """);
        System.out.print("""
                1. Show all products
                2. Add new product
                3. Remove product
                4. Add promotional product
                5. Remove promotional product
                6. Edit promotional product
                7. Log out
                Your choice
                """);
        System.out.print("> ");
    }

    /**
     * Prints the products of parameter list
     * @param productsFound List to be printed.
     */
    public void printProducts(List<Product> productsFound) {
        if (productsFound.isEmpty()) {
            System.out.println("No products were found!");
        } else {
            for (Product product : productsFound) {
                System.out.println(product.toString());
            }
            System.out.println(productsFound.size() + " products where found.");
            System.out.println();
        }
    }
}

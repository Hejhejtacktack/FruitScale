import java.util.ArrayList;
import java.util.List;

public class OutputDevice {
    private static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private String textColor;

    public OutputDevice(Authentication userStatus) {
        switchSession(userStatus);
    }

    public void switchSession(Authentication userStatus) {
        switch (userStatus) {
            case LOGGED_IN -> this.textColor = ANSI_GREEN;
            case LOGGED_OUT -> this.textColor = ANSI_RESET;
        }
    }

    public void initialize() {
        System.out.println();
        System.out.println("Starting program...");
        System.out.println("Welcome to the fruit scale. You will now get multiple choices.");
    }

    public void print(String string) {
        if (string.equals("> ")) {
            System.out.print(string);
        } else {
            System.out.println(string);
        }
    }

    public void printMainMenu() {
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
                5. Login as admin
                Your choice
                """);
        System.out.print("> ");
    }

    public void printEmployeeMenu() {
        System.out.println(this.textColor);
        System.out.print("""
                \tEmployee menu
                Please enter the corresponding integer of your choice.
                Enter Q to exit the program.
                """);
        System.out.print("""
                1. Add new product
                2. Remove product
                3. Add promotional product
                4. Remove promotional product
                5. Edit promotional product
                6. Log out
                Your choice
                """);
        System.out.print("> ");
    }

    public void printProductMenu(Product product) {
        System.out.println("\tProduct menu");
        System.out.println("Product:");
        System.out.println(product);
        System.out.println();
    }

    public void printTotalPrice(Double sum) {
        System.out.printf("Total price: %.2f kr\n", sum);
    }

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

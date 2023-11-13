/**
 * A console based program that is an imaginary fruit scale.
 *
 * @author Erik Hillborg, erik.hillborg@iths.se
 */

import java.util.*;

public class FruitScale {
    // TODO ingen inputDevice? Bara outpuDevice?
    private final InputDevice inputDevice;
    private final OutputDevice outputDevice;
    private final List<Product> products;
    private final List<Product> promoProducts;
    private final List<Employee> employees;
    private final Authentication userStatus;

    public FruitScale() {
        this.inputDevice = new InputDevice();
        this.outputDevice = new OutputDevice(Authentication.LOGGED_OUT);
        this.products = new ArrayList<>();
        this.promoProducts = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.userStatus = Authentication.LOGGED_OUT;
    }

    /**
     * Starts the program.
     * Loops until the user inputs "Q" or "q".
     */
    public void runAsCustomer() {
//        System.out.println();
//        System.out.println("Starting program...");
//        System.out.println("Welcome to the fruit scale. You will now get multiple choices.");

        this.employees.add(new Employee("hej", "123"));
        this.employees.add(new Employee("då", "234"));

        this.products.add(new Product("Banana"));
        this.products.add(new Product("potato", new Price(0, false)));
        List<String> tempPG1 = new ArrayList<>();
        tempPG1.add("Fruit");
        tempPG1.add("Stone");
        this.products.add(new Product("pear", tempPG1));
        List<String> tempPG2 = new ArrayList<>();
        tempPG2.add("Fruit");
        tempPG2.add("Stone");
        tempPG2.add("exotic");
        this.products.add(new Product("Apple", new Price(19.45, true), tempPG2));

        outputDevice.initialize();

        String mainMenuChoice;
        do {
            this.outputDevice.printMainMenu();
            mainMenuChoice = this.inputDevice.readString();

            switch (mainMenuChoice) {
                case "1" -> printAllProducts();
                case "2" -> printPromoProducts();
                case "3" -> searchProductByName();
                case "4" -> searchProductByCategory();
                case "5" -> runAsEmployee();
                case "Q", "q" -> exitProgram();
                default -> this.outputDevice.print("Error! Please enter the corresponding integer of your choice.");
            }
        } while (true);
    }

    private void printPromoProducts() {
        this.outputDevice.printProducts(this.promoProducts);
    }

    private void runAsEmployee() {
        String username = this.inputDevice.readUsername();
        String password = this.inputDevice.readPassword();

        if (tryLogin(username, password)) {
            this.outputDevice.switchSession(Authentication.LOGGED_IN);

            boolean run = true;
            String menuChoice;
            do {
                this.outputDevice.printEmployeeMenu();
                menuChoice = this.inputDevice.readString();

                switch (menuChoice) {
                    case "1" -> addProduct();
                    case "2" -> tryRemoveProduct();
                    case "3" -> addPromoProduct();
                    // TODO
                    //case "4" -> tryRemovePromoProduct();
                    //case "5" -> editPromoProduct();
                    case "6" -> {
                        logoutAdmin();
                        run = false;
                    }
                    default -> this.outputDevice.print("Error! Please enter the corresponding integer of your choice.");
                }
            } while (run);
        }
    }

    private void addPromoProduct() {
        // TODO

    }

    private void logoutAdmin() {
        System.out.println("Logging out...");
        this.outputDevice.switchSession(Authentication.LOGGED_OUT);
    }

    private boolean tryLogin(String username, String password) {
        Employee currentEmployee = findEmployee(username);

        if (currentEmployee != null) {
            if (currentEmployee.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    private Employee findEmployee(String username) {
        for (Employee employee : this.employees) {
            if (employee.getUserName().equalsIgnoreCase(username)) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Menu for the current product.
     * Calls printTotalPrice and readWeight() or readQuantity()
     * @param product the current product
     */
    private void productMenu(Product product) {
        this.outputDevice.printProductMenu(product);

        if (product.getPrice().isPerKG()) {
            double weight = inputDevice.readWeight();
            double totalPrice = computeTotalPrice(product, weight);
            this.outputDevice.printTotalPrice(totalPrice);
        } else {
            int quantity = inputDevice.readQuantity();
            double totalPrice = computeTotalPrice(product, (double)quantity);
            this.outputDevice.printTotalPrice(totalPrice);
        }
    }

    /**
     * Prints all products in productList.
     */
    private void printAllProducts() {
        this.outputDevice.print("All registered products:");
        this.outputDevice.printProducts(this.products);
    }

    /**
     * Creates and adds a new product to productList.
     */
    private void addProduct() {
        this.outputDevice.print("Adding new product");

        // Prompt and read name
        String name = this.inputDevice.readName();

        // Prompt and read price
        Price price = this.inputDevice.readPrice();

        // Prompt and read product group
        List<String> productGroup = this.inputDevice.readProductGroups();

        if (price.getValue() == 0 && productGroup == null) {
            this.products.add(new Product(name));
        } else if (price.getValue() == 0) {
            this.products.add(new Product(name, productGroup));
        } else if (productGroup == null) {
            this.products.add(new Product(name, price));
        } else {
            this.products.add(new Product(name, price, productGroup));
        }

        this.outputDevice.print("Product added!");
    }

    /**
     * Removes a product if product exists in productList.
     */
    private void tryRemoveProduct() {
        this.outputDevice.print("Removing product");

        String userStr = this.inputDevice.readName();
        Product productToRemove = findProductByName(userStr);

        // If product was found
        if (productToRemove != null) {
            this.outputDevice.print(productToRemove.getName() + " was removed from registered products!");
            this.products.remove(productToRemove);
        } else { // If no product was found
            this.outputDevice.print("Could not find " + userStr + " in registered products");
        }
    }

    /**
     * Tries to find a product by name.
     * Calls findProductByName() and productMenu() or printProductsFound().
     */
    private void searchProductByName() {
        String userStr;
        do {
            // Prompts user and reads input
            this.outputDevice.print("");
            this.outputDevice.print("""
                    \tSearch by name
                    Enter complete name to continue to product menu
                    Enter 'menu' to return to main menu""");
            userStr = this.inputDevice.readName();

            if (userStr.equalsIgnoreCase("menu")) {
                this.outputDevice.print("Returning to main menu...");
                return;
            }

            Product currentProduct = findProductByName(userStr);

            // If a product was found
            if (currentProduct != null) {
                this.outputDevice.print("Product found!");
                this.outputDevice.print("");
                productMenu(currentProduct);
            } else { // If no product was found
                printProductsFound(userStr);
            }
        } while (true);
    }

    /**
     * Tries to find a product by category.
     * Calls findProductsByCategory(), readName() and productMenu().
     */
    private void searchProductByCategory() {
        HashSet<String> uniqueProductGroupsSet = new HashSet<>();
        // Iterates through the product list and adding the elements to HashSet.
        // HashSet only accepts unique elements so all duplicates are ignored.
        for (Product product : this.products) {
            uniqueProductGroupsSet.addAll(product.getProductGroups());
        }

        do {
            // Printing all unique product groups.
            this.outputDevice.print("");
            this.outputDevice.print("""
                    \tSearch by category
                    Enter 'menu' to return to main menu
                    Registered product groups:""");
            for (String productGroup : uniqueProductGroupsSet) {
                this.outputDevice.print(productGroup);
            }

            // Prompts user and reads input.
            this.outputDevice.print("");
            this.outputDevice.print("Enter a category to continue");
            this.outputDevice.print("> ");
            String userStr = this.inputDevice.readString();

            if (userStr.equalsIgnoreCase("menu")) {
                this.outputDevice.print("Returning to main menu...");
                return;
            }

            // Stores all the found products in array
            Product[] foundProducts = findProductsByCategory(userStr);

            // If products were found
            if (foundProducts.length != 0) {
                for (Product product : foundProducts) {
                    this.outputDevice.print(product.toString());
                }

                // Prompts user and reads input
                this.outputDevice.print("");
                this.outputDevice.print("Enter complete name to continue to product menu\n" +
                        "Enter 'menu' to return to main menu");
                userStr = this.inputDevice.readName();

                if (userStr.equalsIgnoreCase("menu")) {
                    this.outputDevice.print("Returning to main menu...");
                    return;
                }

                // Calls method productMenu() if user's string equals a products name
                for (Product product : foundProducts) {
                    if (product.getName().equalsIgnoreCase(userStr)) {
                        productMenu(product);
                    }
                }
            } else { // If no products were found
                this.outputDevice.print("No products found in category '" + userStr + "'");
            }
        } while (true);
    }

    /**
     * Exits the program.
     */
    private void exitProgram() {
        System.out.println();
        System.out.println("Exiting program...");
        System.exit(0);
    }

    /**
     * Tries to find a product in productList by name property.
     * @param productName The name of the product to be found.
     * @return A product object if found, otherwise null.
     */
    private Product findProductByName(String productName) {
        for (Product product : this.products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Tries to find a product in productList by category property.
     * @param userCategory The category of the products to be found.
     * @return An array of found products.
     */
    private Product[] findProductsByCategory(String userCategory) {
        List<Product> productsFound = new ArrayList<>();
        int i = 0;
        for (Product product : this.products) {
            for (String productGroup : product.getProductGroups()) {
                if (productGroup.equalsIgnoreCase(userCategory)) {
                    productsFound.add(product);
                    i++;
                    break;
                }
            }
        }
        return productsFound.toArray(new Product[i]);
    }

    private void printProductsFound(String userStr) {
        List<Product> productsFound = new ArrayList<>();
        for (Product product : this.products) {
            if (product.getName().toLowerCase().contains(userStr.toLowerCase())) {
                productsFound.add(product);
            }
        }
        this.outputDevice.printProducts(productsFound);
    }

    /**
     * Computes and prints the total price of a product
     * @param product The object to be computed
     * @param amount The amount to be computed
     */
    private double computeTotalPrice(Product product, Double amount) {
        double sum = product.getPrice().getValue() * amount;
        return sum;
    }
}

/**
 * A console based program that is an imaginary fruit scale.
 *
 * @author Erik Hillborg, erik.hillborg@iths.se
 */

import java.util.*;

public class FruitScale {
    private final InputDevice inputDevice;
    private final OutputDevice outputDevice;
    private final List<Product> products;
    private final List<Employee> employees;
    private final Authentication userStatus;
    private final Cart cart;

    public FruitScale() {
        this.inputDevice = new InputDevice();
        this.outputDevice = new OutputDevice(Authentication.LOGGED_OUT);
        this.products = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.userStatus = Authentication.LOGGED_OUT;
        this.cart = new Cart();
    }

    /**
     * Calls methods based on user's input.
     * Loops until the user inputs "Q" or "q".
     */
    public void runAsCustomer() {
        /*
        Adding data just to run program
         */
        this.employees.add(new Employee("oskar", "123"));
        this.employees.add(new Employee("ulf", "234"));

        List<String> tempPG1 = new ArrayList<>();
        tempPG1.add("Fruit");
        tempPG1.add("Rose plant");
        this.products.add(new Product("pear", new Price(34.909090, false), tempPG1));
        List<String> tempPG2 = new ArrayList<>();
        tempPG2.add("Fruit");
        tempPG2.add("Rose plant");
        tempPG2.add("Something");
        this.products.add(new Product("Apple", new Price(19.45, true), tempPG2));

        this.products.add(new PromoProduct("kiwi", new Price(20, false), new ArrayList<>(),
                new Discount(20), 5));
        this.products.add(new PromoProduct("Potatoes", new Price(9.99, true), new ArrayList<>(),
                new Discount(15), 2));

        /*
        Program starts here
         */
        outputDevice.initialize();

        String mainMenuChoice;
        do {
            this.outputDevice.printCustomerMenu();
            try {
                mainMenuChoice = this.inputDevice.readString();
                switch (mainMenuChoice) {
                    case "1" -> this.outputDevice.printProducts(this.products);
                    case "2" -> printPromoProducts();
                    case "3" -> searchProductByName();
                    case "4" -> searchProductByCategory();
                    case "5" -> this.outputDevice.printProducts(this.cart.getItems());
                    case "6" -> checkOut();
                    case "7" -> runAsEmployee();
                    case "Q", "q" -> exitProgram();
                    default -> this.outputDevice.print("Error! Please enter the corresponding integer of your choice.");
                }
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Please enter the corresponding integer of your choice.");
            }
        } while (true);
    }

    /**
     * Calls methods based on user's input.
     * Loops until the user inputs "Q" or "q".
     */
    private void runAsEmployee() {
        if (!logIn()) {
            return;
        }

        this.outputDevice.switchSession(Authentication.LOGGED_IN);

        boolean run = true;
        String menuChoice;
        do {
            this.outputDevice.printEmployeeMenu();
            try {
                menuChoice = this.inputDevice.readString();
                switch (menuChoice) {
                    case "1" -> this.outputDevice.printProducts(this.products);
                    case "2" -> addProduct();
                    case "3" -> tryRemoveProduct();
                    //case "4" -> addPromoProduct();
                    //case "5" -> tryRemovePromoProduct();
                    //case "6" -> editPromoProduct();
                    case "7" -> {
                        logOut();
                        run = false;
                    }
                    case "Q", "q" -> exitProgram();
                    default -> this.outputDevice.print("Error! Please enter the corresponding integer of your choice.");
                }
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Please enter the corresponding integer of your choice.");
            }
        } while (run);
    }

    /**
     * Tries to log in an employee.
     * @return true if login was sucessful, otherwise false.
     */
    private boolean logIn() {
        String username;
        String password;

        do {
            this.outputDevice.print("Enter username");
            this.outputDevice.print("> ");
            try {
                username = this.inputDevice.readString();
                break;
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Username cannot be blank");
            }
        } while (true);

        do {
            this.outputDevice.print("Enter password");
            this.outputDevice.print("> ");
            try {
                password = this.inputDevice.readString();
                break;
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Password cannot be blank");
            }
        } while (true);

        Employee currentEmployee = findEmployee(username);

        if (currentEmployee == null) {
            this.outputDevice.print("Error! Could not find employee");
            return false;
        } else {
            if (currentEmployee.getPassword().equals(password)) {
                this.outputDevice.print("Log in successful!");
                return true;
            } else {
                this.outputDevice.print("Error! Wrong password");
                return false;
            }
        }
    }

    /**
     * Logs out an Employee
     */
    private void logOut() {
        System.out.println("Logging out...");
        this.outputDevice.switchSession(Authentication.LOGGED_OUT);
    }

    /**
     * Prints a string representation of the cart and prompts the user to checkout (end the program)
     */
    private void checkOut() {
        this.outputDevice.print("Your cart: ");

        this.outputDevice.print(this.cart.toString());

        this.outputDevice.print("Would you like to checkout?");
        try {
            boolean checkout = this.inputDevice.readYesOrNo();
            if (checkout) {
                this.outputDevice.print("Thank you! See you soon!");
                exitProgram();
            }
        } catch (InputMismatchException iME) {
            this.outputDevice.print("Error! Enter 'y' or 'n'");
        }
    }

    /**
     * Creates and adds a new product to productList.
     */
    private void addProduct() {
        this.outputDevice.print("Adding new product");

        // Prompt and read name
        String name = promptName();

        // Prompt and read price
        Price price = promptPrice();

        // Prompt and read product group
        List<String> productGroups = promptProductGroups();


        this.products.add(new Product(name, price, productGroups));

        this.outputDevice.print("Product added!");
    }

    /**
     * Removes a product if product exists in productList.
     */
    private void tryRemoveProduct() {
        this.outputDevice.print("Removing product");

        this.outputDevice.print("Enter the name of the product you want to remove");
        this.outputDevice.print("> ");
        String userStr = this.inputDevice.readName();
        Product productToRemove = findProductByName(userStr);

        try {
            this.products.remove(productToRemove);
            this.outputDevice.print(productToRemove.toString());
            this.outputDevice.print(productToRemove.getName() + " was removed from registered products!");
        } catch (NullPointerException nPE) {
            this.outputDevice.print("Error! Could not find " + userStr + " in registered products");
        }
    }

    /**
     * Tries to find a product by name.
     * Calls findProductByName() and addToCart() or printProductsFound().
     */
    private void searchProductByName() {
        String userStr;
        do {
            // Prompts user and reads input
            this.outputDevice.print("");
            this.outputDevice.print("""
                    \tSearch by name
                    Enter complete name to continue to checkout
                    Enter 'menu' to return to main menu""");
            try {
                userStr = this.inputDevice.readName();

                if (userStr.equalsIgnoreCase("menu")) {
                    this.outputDevice.print("Returning to main menu...");
                    return;
                }

                Product currentProduct = findProductByName(userStr);

                // If a product was found
                if (currentProduct != null) {
                    this.outputDevice.print("Product found!");
                    this.outputDevice.print(currentProduct.toString());
                    this.outputDevice.print("");
                    addToCart(currentProduct);
                } else { // If no product was found
                    printProductsFound(userStr);
                }
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Enter characters");
            }
        } while (true);
    }

    /**
     * Tries to find a product by category. If a product was found, addToCart() is called.
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
            this.outputDevice.print("Enter 'menu' to return to main menu");
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
                this.outputDevice.print("Enter complete name to continue to checkout\n" +
                        "Enter 'menu' to return to main menu");
                userStr = this.inputDevice.readName();

                if (userStr.equalsIgnoreCase("menu")) {
                    this.outputDevice.print("Returning to main menu...");
                    return;
                }

                // Calls method addToCart() if user's string equals a products name
                for (Product product : foundProducts) {
                    if (product.getName().equalsIgnoreCase(userStr)) {
                        addToCart(product);
                    }
                }
            } else { // If no products were found
                this.outputDevice.print("No products found in category '" + userStr + "'");
            }
        } while (true);
    }

    /**
     * Prompts user and tries to add a Product to the Cart.
     * @param currentProduct Object to be added to Cart.
     */
    public void addToCart(Product currentProduct) {
        boolean add;

        this.outputDevice.print("Would you like to add " + currentProduct.getName() + " to cart? (y/n)");
        this.outputDevice.print("> ");
        try {
            add = this.inputDevice.readYesOrNo();
        } catch (InputMismatchException iME) {
            this.outputDevice.print("Error! Enter 'y' or 'n'");
            return;
        }

        if (add) {
            boolean isPerKg = currentProduct.getPrice().isPerKG();
            double amount = isPerKg ? promptWeight() : promptQuantity();

            if (currentProduct instanceof PromoProduct promoProduct) {
                this.cart.applyDiscount(promoProduct, amount);

                if (isPerKg) {
                    promoProduct.getPrice().setValue(currentProduct.getPrice().getValue() * amount);
                    this.cart.add(promoProduct);
                    this.outputDevice.print(amount + " kg " + promoProduct.getName() + "s was added to your cart!");
                } else {
                    List<Product> promoProducts = Collections.nCopies((int) amount, promoProduct);
                    this.cart.add(promoProducts);
                    this.outputDevice.print(amount + " st " + promoProduct.getName() + "s was added to your cart!");
                }
            } else {
                this.cart.add(currentProduct);
            }
        }
    }

    /**
     * Prints a string representation of the products that is promotional
     */
    private void printPromoProducts() {
        this.outputDevice.print("Promotional products:");
        for (Product product : this.products) {
            // If product is promotional and if products price is per kg
            if (product instanceof PromoProduct && product.getPrice().isPerKG()) {
                this.outputDevice.print("\n" + "Buy " + ((PromoProduct) product).getPrerequisite() + " kg or more to get "
                        + ((PromoProduct) product).getDiscount().toString() + "% discount");
                this.outputDevice.print(product.toString());
                this.outputDevice.print("Price with discount: "
                        + ((PromoProduct) product).displayReducedPrice());
                // If product is promotional and if products price is not per kg
            } else if (product instanceof PromoProduct && !product.getPrice().isPerKG()) {
                this.outputDevice.print("\n" + "Buy " + ((PromoProduct) product).getPrerequisite() + " st or more to get "
                        + ((PromoProduct) product).getDiscount().toString() + "% discount");
                this.outputDevice.print(product.toString());
                this.outputDevice.print("Price with discount: "
                        + ((PromoProduct) product).displayReducedPrice());
            }
        }
    }

    /**
     * Asks user for product groups and gets user input. Also creates a list oof users input.
     * @return A list of users input.
     */
    private List<String> promptProductGroups() {
        String strProductGroup;
        List<String> productGroups;
        do {
            this.outputDevice.print("Enter the products categories\n" +
                    "If more than one, separate with comma (fruit, exotic, citrus etc.)");
            this.outputDevice.print("Leave blank to not enter any product groups.");
            this.outputDevice.print("> ");

            try {
                strProductGroup = this.inputDevice.readProductGroups();
                break;
            } catch (IllegalArgumentException iME) {
                this.outputDevice.print("Error! Enter characters");
            }
        } while (true);

        if (strProductGroup.isBlank()) {
            return null;
        }

        // Initializes the list to an ArrayList and
        // splits the input string into separate elements by ',' and adds them to the Arraylist
        productGroups = new ArrayList<>(Arrays.asList(strProductGroup.split(",")));

        // Removes all empty elements
        productGroups.removeAll(Arrays.asList("", null));

        return productGroups;
    }

    /**
     * Prompts user for creating a new Price.
     * @return A new Price based on users input.
     */
    private Price promptPrice() {
        double value;
        boolean pricePerKilo = false;

        do {
            this.outputDevice.print("Enter the product's price");
            this.outputDevice.print("Leave blank to not enter price");
            this.outputDevice.print("> ");

            try {
                value = this.inputDevice.readPrice();
                break;
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Enter numbers");
            }
        } while (true);

        if (value != 0) {
            do {
                this.outputDevice.print("Is the price in kr/kg? (y/n)");
                this.outputDevice.print("> ");

                try {
                    pricePerKilo = this.inputDevice.readYesOrNo();
                    break;
                } catch (InputMismatchException iME) {
                    this.outputDevice.print("Error! Enter 'y' or 'n'");
                }
            } while (true);
        }

        return new Price(value, pricePerKilo);
    }

    /**
     * Prompts user for getting a product name.
     * @return A string representing the name.
     */
    private String promptName() {
        String name;
        do {
            this.outputDevice.print("Enter the products name");
            this.outputDevice.print("> ");
            try {
                name = this.inputDevice.readName();
                break;
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Enter characters");
            }
        } while (true);

        return name;
    }

    /**
     * Prompts user and reads quantity
     * @return Quantity as an int
     */
    private int promptQuantity() {
        int quantity;
        do {
            this.outputDevice.print("Enter desired quantity");
            this.outputDevice.print("> ");
            try {
                quantity = this.inputDevice.readInteger();
                return quantity;
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Enter numbers");
            }
        } while (true);
    }

    /**
     * Prompts user and reads quantity
     * @return Weight as a double
     */
    private double promptWeight() {
        double weight;
        do {
            this.outputDevice.print("Enter desired weight (in kg)");
            this.outputDevice.print("> ");
            try {
                weight = this.inputDevice.readDouble();
                return weight;
            } catch (InputMismatchException iME) {
                this.outputDevice.print("Error! Enter numbers");
            }
        } while (true);
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

    /**
     * Prints products found containing parameter
     * @param userStr The string to be matched for finding Products
     */
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
     * Tries to find an Employee
     * @param username String to match registered Employee with
     * @return An Employee if found, otherwise null.
     */
    private Employee findEmployee(String username) {
        for (Employee employee : this.employees) {
            if (employee.getUserName().equalsIgnoreCase(username)) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Exits the program.
     */
    private void exitProgram() {
        System.out.println();
        System.out.println("Exiting program...");
        System.exit(0);
    }
}

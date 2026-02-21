import java.util.*;

// ---------- Custom Exception ----------
class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}

// ---------- Enum for Storage Type ----------
enum StorageType {
    COLD, DRY
}

// ---------- Product Class ----------
class Product {
    private int productId;
    private String name;
    private double price;
    private int quantity;

    public Product(int productId, String name, double price, int quantity) {
        if (price < 0 || quantity < 0)
            throw new IllegalArgumentException("Price/Quantity cannot be negative");

        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters (Encapsulation)
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(double price) {
        if (price < 0)
            throw new IllegalArgumentException("Invalid price");
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Invalid quantity");
        this.quantity = quantity;
    }

    public void displayDetails() {
        System.out.println("ID: " + productId +
                ", Name: " + name +
                ", Price: " + price +
                ", Quantity: " + quantity);
    }
}

// ---------- Perishable Product ----------
class PerishableProduct extends Product {
    private String expiryDate;
    private StorageType storageType;

    public PerishableProduct(int id, String name, double price, int qty,
            String expiryDate, StorageType storageType) {
        super(id, name, price, qty);
        this.expiryDate = expiryDate;
        this.storageType = storageType;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Expiry Date: " + expiryDate +
                ", Storage: " + storageType);
    }
}

// ---------- Inventory Manager ----------
class InventoryManager {
    private Map<Integer, Product> inventory = new HashMap<>();

    public void addProduct(Product product) {
        inventory.put(product.getProductId(), product);
        System.out.println("Product added successfully.");
    }

    public void displayProducts() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory empty.");
            return;
        }
        inventory.values().forEach(Product::displayDetails);
    }

    public Product getProduct(int id) throws Exception {
        if (!inventory.containsKey(id))
            throw new Exception("Invalid product ID");
        return inventory.get(id);
    }

    public void showLowStock() {
        inventory.values().stream()
                .filter(p -> p.getQuantity() < 5)
                .forEach(Product::displayDetails);
    }
}

// ---------- Order Class ----------
class Order {
    private int orderId;
    private int productId;
    private int orderQuantity;
    private int totalAmount;

    public Order(int orderId, Product product, int orderQuantity)
            throws InsufficientStockException {

        if (orderQuantity > product.getQuantity())
            throw new InsufficientStockException("Insufficient stock!");

        this.orderId = orderId;
        this.productId = product.getProductId();
        this.orderQuantity = orderQuantity;

        // Arithmetic calculation
        double amount = product.getPrice() * orderQuantity;

        // Explicit type casting (double → int)
        this.totalAmount = (int) amount;

        product.setQuantity(product.getQuantity() - orderQuantity);

        System.out.println("Order placed successfully!");
        System.out.println("Total Amount: " + totalAmount);
    }
}

// ---------- Main Menu Class ----------
public class InventorySystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();
        int choice;

        do {
            System.out.println("\nWelcome to Inventory Management System");
            System.out.println("1. Add Product");
            System.out.println("2. Add Perishable Product");
            System.out.println("3. Display Inventory");
            System.out.println("4. Place Order");
            System.out.println("5. Show Low Stock Products");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("ID Name Price Quantity: ");
                        manager.addProduct(new Product(
                                sc.nextInt(),
                                sc.next(),
                                sc.nextDouble(),
                                sc.nextInt()));
                        break;

                    case 2:
                        System.out.print("ID Name Price Qty Expiry(COLD/DRY): ");
                        manager.addProduct(new PerishableProduct(
                                sc.nextInt(),
                                sc.next(),
                                sc.nextDouble(),
                                sc.nextInt(),
                                sc.next(),
                                StorageType.valueOf(sc.next())));
                        break;

                    case 3:
                        manager.displayProducts();
                        break;

                    case 4:
                        System.out.print("Order ID Product ID Quantity: ");
                        new Order(
                                sc.nextInt(),
                                manager.getProduct(sc.nextInt()),
                                sc.nextInt());
                        break;

                    case 5:
                        manager.showLowStock();
                        break;

                    case 6:
                        System.out.println("Exiting...");
                        break;
                }
            } catch (InsufficientStockException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 6);
    }
}

package model;

public class Product {
    private int id;
    private String name;
    private int price;
    private String description;
    private int stock;

    public Product(int id, String name, int price, String description, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    // Getter, Setter
    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getDescription() { return description; }
    public int getStock() { return stock; }

    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setStock(int stock) { this.stock = stock; }
}

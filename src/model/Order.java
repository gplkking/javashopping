package model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private int totalPrice;
    private Timestamp orderDate;
    private String status;

    public Order(int id, int userId, int productId, int quantity, int totalPrice, Timestamp orderDate, String status) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Getter, Setter
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public int getTotalPrice() { return totalPrice; }
    public Timestamp getOrderDate() { return orderDate; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}

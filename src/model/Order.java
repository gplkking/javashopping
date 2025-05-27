package model;

import java.util.Date;

public class Order {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private int totalPrice;
    private Date orderDate;

    public Order() {}

    public Order(int id, int userId, int productId, int quantity, int totalPrice, Date orderDate) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // Getter & Setter

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    @Override
    public String toString() {
        return id + " | UserID: " + userId + " | ProductID: " + productId + " | Quantity: " + quantity + " | Total: " + totalPrice;
    }
}

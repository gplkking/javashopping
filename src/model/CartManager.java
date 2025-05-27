package model;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private List<Order> cart = new ArrayList<>();

    public void addToCart(Order order) {
        cart.add(order);
    }

    public List<Order> getCart() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }
}

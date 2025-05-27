package dao;

import db.DBUtil;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public boolean addOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, product_id, quantity, total_price, order_date) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getProductId());
            pstmt.setInt(3, order.getQuantity());
            pstmt.setInt(4, order.getTotalPrice());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getInt("total_price"),
                    rs.getTimestamp("order_date")
                );
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

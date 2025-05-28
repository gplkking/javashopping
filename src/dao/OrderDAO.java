package dao;

import db.DBUtil;
import model.Order;

import java.sql.*;
import java.util.*;

public class OrderDAO {

    public boolean addOrder(Order o) {
        String sql = "INSERT INTO orders (user_id, product_id, quantity, total_price, order_date, status) VALUES (?, ?, ?, ?, NOW(), ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, o.getUserId());
            ps.setInt(2, o.getProductId());
            ps.setInt(3, o.getQuantity());
            ps.setInt(4, o.getTotalPrice());
            ps.setString(5, o.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = userId == 0 ? "SELECT * FROM orders" : "SELECT * FROM orders WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId != 0) ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getInt("total_price"),
                            rs.getTimestamp("order_date"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ✅ 전체 주문 조회 (AdminFrame용)
    public List<Order> getAllOrders() {
        return getOrdersByUser(0);
    }

    public boolean updateOrderQuantity(int orderId, int quantity) {
        String sql = "UPDATE orders SET quantity=?, total_price=(SELECT price * ? FROM product WHERE id=orders.product_id) WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, quantity);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean markOrderAsCompleted(int orderId) {
        String sql = "UPDATE orders SET status='구매완료' WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean requestCancel(int orderId) {
        String sql = "UPDATE orders SET status='주문취소 요청' WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean approveCancel(int orderId) {
        String sql = "UPDATE orders SET status='취소완료' WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public int getTotalSales() {
        String sql = "SELECT SUM(total_price) FROM orders WHERE status='구매완료'";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public Map<Integer, Integer> getProductSales() {
        Map<Integer, Integer> map = new HashMap<>();
        String sql = "SELECT product_id, SUM(quantity) AS total_sold FROM orders WHERE status='구매완료' GROUP BY product_id";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getInt("product_id"), rs.getInt("total_sold"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }
}

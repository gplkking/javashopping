package dao;

import db.DBUtil;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setDescription(rs.getString("description"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
 // 상품 추가
    public boolean addProduct(Product p) {
        String sql = "INSERT INTO product (name, price, description) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getPrice());
            pstmt.setString(3, p.getDescription());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 상품 삭제
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 상품 수정
    public boolean updateProduct(Product p) {
        String sql = "UPDATE product SET name = ?, price = ?, description = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getPrice());
            pstmt.setString(3, p.getDescription());
            pstmt.setInt(4, p.getId());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

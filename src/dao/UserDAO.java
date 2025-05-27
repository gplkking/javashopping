package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBUtil;
import model.User;

public class UserDAO {

    // 로그인 처리: username과 password로 사용자 정보 조회
    public User login(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // DB에서 읽은 데이터 → User 객체로 변환
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 로그인 실패
    }

    // 회원가입 처리: 사용자 정보 DB에 저장
    public boolean register(User user) {
        String sql = "INSERT INTO user (username, password, name, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getRole());
            int rows = pstmt.executeUpdate();
            return rows > 0; // 성공 여부 반환

        } catch (SQLException e) {
            System.err.println("SQL 에러: " + e.getMessage());
            e.printStackTrace();
        }

        return false; // 실패
    }
}

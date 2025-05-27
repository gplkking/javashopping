package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/h_bin?serverTimezone=UTC";
    private static final String USER = "root";         // 본인 DB 사용자명
    private static final String PASSWORD = "rootroot"; // 본인 DB 비밀번호

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL 드라이버 로딩 성공");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL 드라이버 로딩 실패");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/h_bin?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "rootroot";

    public static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("DB 드라이버 로딩 성공");
        } catch (ClassNotFoundException e) {
            System.err.println("DB 드라이버 로딩 실패: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

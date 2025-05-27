package dao;

import java.sql.*;
import java.util.Scanner;

public class UserConsleApp {
    static final String DB_URL = "jdbc:mysql://localhost:3306/h_bin?useSSL=false&serverTimezone=UTC";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "rootroot";

    public static void main(String[] args) {
        try (
            Scanner scanner = new Scanner(System.in)
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ MySQL 연결 성공");

            System.out.println("1. 회원가입 | 2. 로그인");
            System.out.print("선택: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                // 회원가입
                System.out.print("사용자명: ");
                String username = scanner.nextLine();

                System.out.print("비밀번호: ");
                String password = scanner.nextLine();

                System.out.print("이메일: ");
                String email = scanner.nextLine();

                System.out.print("생년월일 (yyyy-mm-dd): ");
                String birthdate = scanner.nextLine();

                String insertSQL = "INSERT INTO users (username, password, email, birthdate) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, email);
                pstmt.setDate(4, Date.valueOf(birthdate));

                int result = pstmt.executeUpdate();
                if (result > 0) {
                    System.out.println("🎉 회원가입 완료! 이제 로그인해보세요.");
                } else {
                    System.out.println("❌ 회원가입 실패.");
                }

            } else if (choice.equals("2")) {
                // 로그인
                System.out.print("사용자명: ");
                String username = scanner.nextLine();

                System.out.print("비밀번호: ");
                String password = scanner.nextLine();

                String selectSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("🎉 로그인 성공! " + rs.getString("username") + "님 환영합니다.");
                } else {
                    System.out.println("❌ 로그인 실패: 아이디 또는 비밀번호가 틀렸습니다.");
                }

            } else {
                System.out.println("❌ 잘못된 선택입니다.");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

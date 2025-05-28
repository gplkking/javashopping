package view;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;

public class HomeFrame extends JFrame {
    private UserDAO userDAO = new UserDAO();

    public HomeFrame() {
        setTitle("홈 화면");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton btnLogin = new JButton("로그인");
        JButton btnRegister = new JButton("회원가입");
        JButton btnProductList = new JButton("상품목록");

        btnLogin.addActionListener(e -> {
            new LoginFrame(); // LoginFrame 열기
            dispose(); // HomeFrame 닫기
        });

        btnRegister.addActionListener(e -> {
            new RegisterFrame(); // 회원가입 화면 열기
            dispose();
        });

        btnProductList.addActionListener(e -> {
            new MainFrame(); // MainFrame 열기 (비로그인 상태)
            dispose();
        });

        add(btnLogin);
        add(btnRegister);
        add(btnProductList);

        setVisible(true);
    }
}

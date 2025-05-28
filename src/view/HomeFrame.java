package view;

import javax.swing.*;
import java.awt.*;

public class HomeFrame extends JFrame {
    public HomeFrame() {
        setTitle("홈 화면");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton btnLogin = new JButton("로그인");
        JButton btnRegister = new JButton("회원가입");
        JButton btnProductList = new JButton("상품목록");

        btnLogin.addActionListener(e -> new LoginFrame());
        btnRegister.addActionListener(e -> new RegisterFrame());
        btnProductList.addActionListener(e -> new MainFrame(null));

        panel.add(btnLogin);
        panel.add(btnRegister);
        panel.add(btnProductList);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}

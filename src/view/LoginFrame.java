package view;

import javax.swing.*;
import dao.UserDAO;
import model.User;
import util.Session;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private JTextField tfUsername;
    private JPasswordField pfPassword;

    public LoginFrame() {
        setTitle("로그인");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("아이디:"));
        tfUsername = new JTextField();
        panel.add(tfUsername);

        panel.add(new JLabel("비밀번호:"));
        pfPassword = new JPasswordField();
        panel.add(pfPassword);

        JButton btnLogin = new JButton("로그인");
        JButton btnCancel = new JButton("취소");
        panel.add(btnLogin);
        panel.add(btnCancel);

        btnLogin.addActionListener(e -> login());
        btnCancel.addActionListener(e -> dispose());

        // 비밀번호 입력 후 엔터
        pfPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    private void login() {
        String username = tfUsername.getText();
        String password = new String(pfPassword.getPassword());
        User user = userDAO.login(username, password);
        if (user != null) {
            Session.setCurrentUser(user);
            JOptionPane.showMessageDialog(this, "로그인 성공!");
            dispose();
            if ("ADMIN".equals(user.getRole())) {
                new AdminFrame(user.getName());
            } else {
                new MainFrame(user.getName());
            }
        } else {
            JOptionPane.showMessageDialog(this, "로그인 실패!");
        }
    }
}

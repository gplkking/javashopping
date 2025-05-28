package view;

import javax.swing.*;
import dao.UserDAO;
import model.User;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegisterFrame extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private JTextField tfUsername, tfName;
    private JPasswordField pfPassword;

    public RegisterFrame() {
        setTitle("회원가입");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("아이디:"));
        tfUsername = new JTextField();
        panel.add(tfUsername);

        panel.add(new JLabel("비밀번호:"));
        pfPassword = new JPasswordField();
        panel.add(pfPassword);

        panel.add(new JLabel("이름:"));
        tfName = new JTextField();
        panel.add(tfName);

        JButton btnRegister = new JButton("가입");
        JButton btnCancel = new JButton("취소");
        panel.add(btnRegister);
        panel.add(btnCancel);

        btnRegister.addActionListener(e -> register());
        btnCancel.addActionListener(e -> dispose());

        // 마지막 입력 후 엔터 가능
        tfName.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    register();
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    private void register() {
        String username = tfUsername.getText();
        String password = new String(pfPassword.getPassword());
        String name = tfName.getText();

        if (userDAO.register(new User(0, username, password, name, "USER"))) {
            JOptionPane.showMessageDialog(this, "가입 성공!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "가입 실패!");
        }
    }
}

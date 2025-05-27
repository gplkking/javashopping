package view;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import model.User;

public class RegisterFrame extends JFrame {
    private JTextField tfUsername, tfName;
    private JPasswordField pfPassword;
    private UserDAO userDAO = new UserDAO();

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

        JButton btnRegister = new JButton("가입하기");
        btnRegister.addActionListener(e -> register());
        panel.add(btnRegister);

        JButton btnCancel = new JButton("취소");
        btnCancel.addActionListener(e -> dispose());
        panel.add(btnCancel);

        add(panel);
        setVisible(true);
    }

    private void register() {
        String username = tfUsername.getText();
        String password = new String(pfPassword.getPassword());
        String name = tfName.getText();

        User user = new User(0, username, password, name, "USER");
        boolean result = userDAO.register(user);

        if (result) {
            JOptionPane.showMessageDialog(this, "회원가입 성공!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "회원가입 실패: 아이디 중복?");
        }
    }
}

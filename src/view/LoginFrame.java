package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.UserDAO;
import model.User;
import util.Session;


public class LoginFrame extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private JTextField tfUsername;
    private JPasswordField pfPassword;

    public LoginFrame() {
        setTitle("로그인");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 5));

        tfUsername = new JTextField();
        pfPassword = new JPasswordField();
        JButton btnLogin = new JButton("로그인");
        JButton btnCancel = new JButton("취소");

        add(new JLabel("아이디:"));
        add(tfUsername);
        add(new JLabel("비밀번호:"));
        add(pfPassword);
        add(btnLogin);
        add(btnCancel);

        // 공통 로그인 처리 로직
        ActionListener loginAction = e -> {
            String username = tfUsername.getText();
            String password = new String(pfPassword.getPassword());

            User user = userDAO.login(username, password);
            if (user != null) {
                Session.setCurrentUser(user);
                JOptionPane.showMessageDialog(this, "로그인 성공!");

                // 관리자/사용자 분기 처리
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                	new AdminFrame(); // 관리자는 AdminFrame 열기
                } else {
                    new MainFrame();  // 일반 사용자는 MainFrame 열기
                }

                dispose(); // LoginFrame 닫기
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 확인하세요.");
            }
        };

        // 버튼 클릭 시 로그인 처리
        btnLogin.addActionListener(loginAction);

        // 엔터 키 이벤트 처리 (비밀번호 입력 후 엔터)
        pfPassword.addActionListener(loginAction);

        // 취소 버튼
        btnCancel.addActionListener(e -> {
            dispose();
            new HomeFrame(); // HomeFrame 돌아가기
        });

        setVisible(true);
    }
}

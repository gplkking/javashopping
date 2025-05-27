package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.UserDAO;
import model.User;

public class LoginFrame extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("로그인");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
        setLocationRelativeTo(null); // 창 가운데 정렬

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblUsername = new JLabel("아이디:");
        JLabel lblPassword = new JLabel("비밀번호:");
        tfUsername = new JTextField();
        pfPassword = new JPasswordField();

        JButton btnLogin = new JButton("로그인");
        JButton btnExit = new JButton("종료");

        panel.add(lblUsername);
        panel.add(tfUsername);
        panel.add(lblPassword);
        panel.add(pfPassword);
        panel.add(btnLogin);
        panel.add(btnExit);

        add(panel);
        
        // 엔터 입력 시 로그인 동작
        pfPassword.addActionListener(e -> login());

        // 로그인 버튼 클릭 이벤트
        btnLogin.addActionListener(e -> login());

        // 종료 버튼 클릭 이벤트
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void login() {
        String username = tfUsername.getText();
        String password = new String(pfPassword.getPassword());

        User user = userDAO.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, user.getName() + "님, 환영합니다!");
            // 로그인 성공 → MainFrame 열기
            new MainFrame(user); // user 객체 전달
            dispose(); // 로그인 창 닫기
        } else {
            JOptionPane.showMessageDialog(this, "로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
        }
    }


    public static void main(String[] args) {
        new LoginFrame();
    }
}

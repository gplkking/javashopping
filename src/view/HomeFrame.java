package view;

import javax.swing.*;
import java.awt.*;
import model.CartManager;

public class HomeFrame extends JFrame {
    private CartManager cartManager = new CartManager();
    private int userId = 1; // 임시 로그인 사용자 ID

    public HomeFrame() {
        setTitle("쇼핑몰 메인 메뉴");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        JButton btnLogin = new JButton("로그인");
        JButton btnRegister = new JButton("회원가입");
        JButton btnProduct = new JButton("상품 목록");
        JButton btnCart = new JButton("장바구니 보기");
        JButton btnAdmin = new JButton("관리자 페이지");
        JButton btnOrder = new JButton("주문 내역 조회");

        btnLogin.addActionListener(e -> new LoginFrame());
        btnRegister.addActionListener(e -> new RegisterFrame());
        btnProduct.addActionListener(e -> new MainFrame(null)); // 임시, 로그인 연동 필요
        btnCart.addActionListener(e -> new CartFrame(userId, cartManager));
        btnAdmin.addActionListener(e -> new AdminFrame());
        btnOrder.addActionListener(e -> new OrderFrame());

        add(btnLogin);
        add(btnRegister);
        add(btnProduct);
        add(btnCart);
        add(btnAdmin);
        add(btnOrder);

        setVisible(true);
    }

    public static void main(String[] args) {
        new HomeFrame();
    }
}

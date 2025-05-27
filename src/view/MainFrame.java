package view;

import javax.swing.*;
import dao.ProductDAO;
import model.Product;
import model.User;
import model.CartManager;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private ProductDAO productDAO = new ProductDAO();
    private JTextArea taProducts;
    private CartManager cartManager = new CartManager();
    private User currentUser; // 로그인한 사용자 정보

    public MainFrame(User user) {
        this.currentUser = user;

        setTitle("사용자: " + currentUser.getName() + "님");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblWelcome = new JLabel(currentUser.getName() + "님 사용중");
        add(lblWelcome, BorderLayout.NORTH);

        taProducts = new JTextArea();
        taProducts.setEditable(false);
        add(new JScrollPane(taProducts), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton btnLoad = new JButton("상품 불러오기");
        JButton btnAddToCart = new JButton("장바구니 담기");
        JButton btnViewCart = new JButton("장바구니 보기");

        btnLoad.addActionListener(e -> loadProducts());
        btnAddToCart.addActionListener(e -> addToCart());
        btnViewCart.addActionListener(e -> new CartFrame(currentUser.getId(), cartManager));

        panel.add(btnLoad);
        panel.add(btnAddToCart);
        panel.add(btnViewCart);

        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadProducts() {
        List<Product> list = productDAO.getAllProducts();
        taProducts.setText("");
        for (Product p : list) {
            taProducts.append(p.getId() + " | " + p.getName() + " | " + p.getPrice() + "원\n");
        }
    }

    private void addToCart() {
        // 기존 코드 그대로 사용 (currentUser.getId() 활용)
    }
}

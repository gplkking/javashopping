package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.ProductDAO;
import dao.OrderDAO;
import model.Product;
import model.Order;
import util.Session;

import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private DefaultTableModel model;
    private JTable table;
    private JTextField tfSearch;
    private String currentUserName;

    public MainFrame(String username) {
        this.currentUserName = username;

        setTitle("메인 화면");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 패널: 사용자 이름, 검색, 버튼들
        JPanel panel = new JPanel();
        JLabel lblUser = new JLabel("사용자: " + currentUserName);
        tfSearch = new JTextField(10);
        JButton btnSearch = new JButton("검색");
        JButton btnAddToCart = new JButton("장바구니에 추가");
        JButton btnCart = new JButton("장바구니 보기");
        JButton btnOrders = new JButton("주문 내역");
        JButton btnLogout = new JButton("로그아웃");

        panel.add(lblUser);
        panel.add(tfSearch);
        panel.add(btnSearch);
        panel.add(btnAddToCart);
        panel.add(btnCart);
        panel.add(btnOrders);
        panel.add(btnLogout);
        add(panel, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "상품명", "가격", "재고"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 사용자 데이터 수정 불가
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 버튼 동작
        btnSearch.addActionListener(e -> loadProducts(tfSearch.getText()));
        btnAddToCart.addActionListener(e -> addToCart());
        btnCart.addActionListener(e -> new CartFrame());
        btnOrders.addActionListener(e -> new OrderFrame());
        btnLogout.addActionListener(e -> logout());

        loadProducts(null);
        setVisible(true);
    }

    private void loadProducts(String keyword) {
        model.setRowCount(0);
        for (Product p : productDAO.getAllProducts()) {
            if (keyword == null || p.getName().contains(keyword)) {
                model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getStock()});
            }
        }
    }

    private void addToCart() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "상품 선택 후 클릭하세요.");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);
        int price = (int) model.getValueAt(row, 2);
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("수량 입력:"));

        Order o = new Order(0, Session.getCurrentUser().getId(), id, quantity, price * quantity, null, "CART");
        if (orderDAO.addOrder(o)) {
            JOptionPane.showMessageDialog(this, "장바구니에 담김!");
        } else {
            JOptionPane.showMessageDialog(this, "실패!");
        }
    }

    private void logout() {
        Session.logout();
        dispose();
        new HomeFrame();
    }
}

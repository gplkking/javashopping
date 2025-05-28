package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.UserDAO;
import dao.ProductDAO;
import dao.OrderDAO;
import model.User;
import model.Product;
import model.Order;
import util.Session;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminFrame extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel model;
    private JLabel lblTotalSales; // 통계용 라벨

    public AdminFrame() {
        User currentUser = Session.getCurrentUser();

        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            JOptionPane.showMessageDialog(null, "관리자만 접근 가능합니다.");
            dispose();
            return;
        }

        setTitle("관리자 페이지 - " + currentUser.getName() + "님");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 5));
        JLabel lblUser = new JLabel("사용자: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        JButton btnUsers = new JButton("회원목록");
        JButton btnProducts = new JButton("상품목록");
        JButton btnAddProduct = new JButton("상품 추가");
        JButton btnSaveProducts = new JButton("상품 변경 저장");
        JButton btnOrders = new JButton("주문목록");
        JButton btnUpdateStatus = new JButton("주문 상태 변경");
        JButton btnStats = new JButton("통계");
        JButton btnBack = new JButton("로그아웃");

        btnUsers.addActionListener(e -> showUsers());
        btnProducts.addActionListener(e -> showProducts());
        btnAddProduct.addActionListener(e -> addProduct());
        btnSaveProducts.addActionListener(e -> saveProductChanges());
        btnOrders.addActionListener(e -> showOrders());
        btnUpdateStatus.addActionListener(e -> updateOrderStatus());
        btnStats.addActionListener(e -> showStats());
        btnBack.addActionListener(e -> backToHome());

        panel.add(lblUser);
        panel.add(btnUsers);
        panel.add(btnProducts);
        panel.add(btnAddProduct);
        panel.add(btnSaveProducts);
        panel.add(btnOrders);
        panel.add(btnUpdateStatus);
        panel.add(btnStats);
        panel.add(btnBack);

        add(panel, BorderLayout.NORTH);

        lblTotalSales = new JLabel("총 매출: 0원");
        lblTotalSales.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblTotalSales, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showUsers() {
        List<User> list = userDAO.getAllUsers();
        model = new DefaultTableModel(new Object[]{"ID", "아이디", "이름", "권한"}, 0);
        for (User u : list) {
            model.addRow(new Object[]{u.getId(), u.getUsername(), u.getName(), u.getRole()});
        }
        updateTable(model);
        lblTotalSales.setText(""); // 총 매출 숨기기
    }

    private void showProducts() {
        List<Product> list = productDAO.getAllProducts();
        model = new DefaultTableModel(new Object[]{"ID", "상품명", "가격", "재고", "설명"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // ID 수정 불가
            }
        };
        for (Product p : list) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getDescription()});
        }
        updateTable(model);
        lblTotalSales.setText(""); // 총 매출 숨기기
    }

    private void showOrders() {
        List<Order> list = orderDAO.getAllOrders();
        model = new DefaultTableModel(new Object[]{"주문ID", "주문자", "상품명", "수량", "총액", "상태"}, 0);
        for (Order o : list) {
            User user = userDAO.getUserById(o.getUserId());
            Product product = productDAO.getProductById(o.getProductId());
            String userName = (user != null) ? user.getName() : "알 수 없음";
            String productName = (product != null) ? product.getName() : "알 수 없음";
            model.addRow(new Object[]{o.getId(), userName, productName, o.getQuantity(), o.getTotalPrice(), o.getStatus()});
        }
        updateTable(model);
        lblTotalSales.setText(""); // 총 매출 숨기기
    }

    private void showStats() {
        model = new DefaultTableModel(new Object[]{"상품명", "판매 수량", "판매 금액"}, 0);
        int totalSales = orderDAO.getTotalSales();
        Map<Integer, Integer> productSales = orderDAO.getProductSales();
        for (Map.Entry<Integer, Integer> entry : productSales.entrySet()) {
            Product p = productDAO.getProductById(entry.getKey());
            String name = (p != null) ? p.getName() : "알 수 없음";
            int qty = entry.getValue();
            int price = (p != null) ? p.getPrice() * qty : 0;
            model.addRow(new Object[]{name, qty, price});
        }
        updateTable(model);
        lblTotalSales.setText("총 매출: " + totalSales + "원");
    }

    private void updateTable(DefaultTableModel model) {
        if (scrollPane != null) remove(scrollPane);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void addProduct() {
        JTextField tfName = new JTextField();
        JTextField tfPrice = new JTextField();
        JTextField tfStock = new JTextField();
        JTextArea taDesc = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(taDesc);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("상품명:"));
        panel.add(tfName);
        panel.add(new JLabel("가격:"));
        panel.add(tfPrice);
        panel.add(new JLabel("재고:"));
        panel.add(tfStock);
        panel.add(new JLabel("설명:"));
        panel.add(descScroll);

        int result = JOptionPane.showConfirmDialog(this, panel, "상품 추가", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = tfName.getText().trim();
            int price = Integer.parseInt(tfPrice.getText().trim());
            int stock = Integer.parseInt(tfStock.getText().trim());
            String desc = taDesc.getText().trim();

            if (productDAO.addProduct(name, price, desc, stock)) {
                JOptionPane.showMessageDialog(this, "상품 추가 성공!");
                showProducts();
            } else {
                JOptionPane.showMessageDialog(this, "상품 추가 실패!");
            }
        }
    }

    private void saveProductChanges() {
        if (model == null) {
            JOptionPane.showMessageDialog(this, "상품 목록을 먼저 조회해주세요.");
            return;
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            int id = (int) model.getValueAt(i, 0);
            String name = (String) model.getValueAt(i, 1);
            int price = Integer.parseInt(model.getValueAt(i, 2).toString());
            int stock = Integer.parseInt(model.getValueAt(i, 3).toString());
            String desc = (String) model.getValueAt(i, 4);
            productDAO.updateProductFull(id, name, price, stock, desc);
        }
        JOptionPane.showMessageDialog(this, "변경 내용 저장 완료!");
    }

    private void updateOrderStatus() {
        String idStr = JOptionPane.showInputDialog("변경할 주문 ID 입력:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        String[] options = {"상품 준비중", "배송중", "배송완료", "취소완료"};
        String status = (String) JOptionPane.showInputDialog(this, "상태 선택:", "상태 변경", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (status == null) return;

        int orderId = Integer.parseInt(idStr);
        if (orderDAO.updateOrderStatus(orderId, status)) {
            if ("취소완료".equals(status)) {
                Order o = orderDAO.getAllOrders().stream().filter(order -> order.getId() == orderId).findFirst().orElse(null);
                if (o != null) {
                    productDAO.updateStock(o.getProductId(), o.getQuantity());
                }
            }
            JOptionPane.showMessageDialog(this, "상태 변경 완료!");
            showOrders();
        } else {
            JOptionPane.showMessageDialog(this, "변경 실패!");
        }
    }

    private void backToHome() {
    	Session.setCurrentUser(null); 
        dispose();
        new view.HomeFrame();
    }
}

package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.UserDAO;
import dao.ProductDAO;
import dao.OrderDAO;
import model.User;
import model.Product;
import model.Order;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminFrame extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JScrollPane productScrollPane;
    private JScrollPane taScrollPane;
    private JTextArea taDisplay;
    private String currentUserName;

    public AdminFrame(String username) {
        this.currentUserName = username;

        setTitle("관리자 페이지");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        taDisplay = new JTextArea();
        taScrollPane = new JScrollPane(taDisplay);
        add(taScrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(2, 5));
        JLabel lblUser = new JLabel("사용자: " + currentUserName+"님");
        JButton btnUsers = new JButton("회원목록");
        JButton btnProducts = new JButton("상품목록");
        JButton btnAddProduct = new JButton("상품 추가");
        JButton btnSaveProducts = new JButton("상품 변경 저장");
        JButton btnOrders = new JButton("주문목록");
        JButton btnUpdateStatus = new JButton("주문 상태 변경");
        JButton btnStats = new JButton("통계");
        JButton btnBack = new JButton("돌아가기");

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
        setVisible(true);
    }

    private void showUsers() {
        if (productScrollPane != null) remove(productScrollPane);
        add(taScrollPane, BorderLayout.CENTER);
        taDisplay.setText("회원 목록:\n");
        List<User> list = userDAO.getAllUsers();
        for (User u : list) {
            taDisplay.append(u.getId() + " / " + u.getUsername() + " / " + u.getName() + " / " + u.getRole() + "\n");
        }
        revalidate();
        repaint();
    }

    private void showProducts() {
        List<Product> list = productDAO.getAllProducts();
        productTableModel = new DefaultTableModel(new Object[]{"ID", "상품명", "가격", "재고", "설명"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // ID만 수정 불가
            }
        };

        for (Product p : list) {
            productTableModel.addRow(new Object[]{
                p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getDescription()
            });
        }

        productTable = new JTable(productTableModel);

        if (productScrollPane != null) remove(productScrollPane);
        remove(taScrollPane);

        productScrollPane = new JScrollPane(productTable);
        add(productScrollPane, BorderLayout.CENTER);

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
                showProducts(); // 추가 후 갱신
            } else {
                JOptionPane.showMessageDialog(this, "상품 추가 실패!");
            }
        }
    }

    private void saveProductChanges() {
        if (productTableModel == null) {
            JOptionPane.showMessageDialog(this, "상품 목록을 먼저 조회해주세요.");
            return;
        }

        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            int id = (int) productTableModel.getValueAt(i, 0);
            String name = (String) productTableModel.getValueAt(i, 1);
            int price = Integer.parseInt(productTableModel.getValueAt(i, 2).toString());
            int stock = Integer.parseInt(productTableModel.getValueAt(i, 3).toString());
            String desc = (String) productTableModel.getValueAt(i, 4);

            productDAO.updateProductFull(id, name, price, stock, desc);
        }
        JOptionPane.showMessageDialog(this, "변경 내용 저장 완료!");
    }

    private void showOrders() {
        if (productScrollPane != null) remove(productScrollPane);
        add(taScrollPane, BorderLayout.CENTER);
        taDisplay.setText("주문 목록:\n");
        List<Order> list = orderDAO.getOrdersByUser(0);
        for (Order o : list) {
            taDisplay.append(o.getId() + " / " + o.getOrderDate() + " / " + o.getProductId() + " / " + o.getQuantity() + " / " + o.getTotalPrice() + " / " + o.getStatus() + "\n");
        }
        revalidate();
        repaint();
    }

    private void showStats() {
        if (productScrollPane != null) remove(productScrollPane);
        add(taScrollPane, BorderLayout.CENTER);
        int totalSales = orderDAO.getTotalSales();
        Map<Integer, Integer> productSales = orderDAO.getProductSales();
        taDisplay.setText("총 매출: " + totalSales + "원\n\n상품별 판매량:\n");
        for (Map.Entry<Integer, Integer> entry : productSales.entrySet()) {
            Product p = productDAO.getProductById(entry.getKey());
            String name = (p != null) ? p.getName() : "알 수 없음";
            taDisplay.append("상품: " + name + " → 판매량: " + entry.getValue() + "\n");
        }
        revalidate();
        repaint();
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
                Order o = orderDAO.getOrdersByUser(0).stream().filter(order -> order.getId() == orderId).findFirst().orElse(null);
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
        dispose();
        new view.HomeFrame();
    }
}

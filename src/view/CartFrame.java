package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Order;
import model.Product;
import util.Session;

import java.awt.*;
import java.util.List;

public class CartFrame extends JFrame {
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();
    private DefaultTableModel model;
    private JTable table;
    private JLabel lblTotal;

    public CartFrame() {
        setTitle("장바구니");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"선택", "상품명", "수량", "총 가격"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // 선택 칸만 클릭 가능
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton btnIncrease = new JButton("수량 +");
        JButton btnDecrease = new JButton("수량 -");
        JButton btnDelete = new JButton("삭제");
        JButton btnCheckout = new JButton("결제");
        lblTotal = new JLabel("총액: 0원");

        btnIncrease.addActionListener(e -> adjustQuantity(1));
        btnDecrease.addActionListener(e -> adjustQuantity(-1));
        btnDelete.addActionListener(e -> deleteSelected());
        btnCheckout.addActionListener(e -> checkoutSelected());

        panel.add(btnIncrease);
        panel.add(btnDecrease);
        panel.add(btnDelete);
        panel.add(btnCheckout);
        panel.add(lblTotal);

        add(panel, BorderLayout.SOUTH);
        loadCart();
        setVisible(true);
    }

    private void loadCart() {
        model.setRowCount(0);
        int total = 0;
        List<Order> list = orderDAO.getOrdersByUser(Session.getCurrentUser().getId());
        for (Order o : list) {
            if ("CART".equals(o.getStatus())) {
                Product p = productDAO.getProductById(o.getProductId());
                String productName = (p != null) ? p.getName() : "알 수 없음";
                model.addRow(new Object[]{false, productName, o.getQuantity(), o.getTotalPrice()});
                total += o.getTotalPrice();
            }
        }
        lblTotal.setText("총액: " + total + "원");
    }

    private void adjustQuantity(int change) {
        List<Order> list = orderDAO.getOrdersByUser(Session.getCurrentUser().getId());
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((Boolean) model.getValueAt(i, 0)) {
                String productName = (String) model.getValueAt(i, 1);
                Product p = productDAO.getAllProducts().stream().filter(pr -> pr.getName().equals(productName)).findFirst().orElse(null);
                if (p == null) continue;

                int productId = p.getId();
                int stock = p.getStock();
                Order o = list.stream().filter(order -> order.getProductId() == productId && "CART".equals(order.getStatus())).findFirst().orElse(null);
                if (o == null) continue;

                int newQty = o.getQuantity() + change;
                if (newQty < 1) newQty = 1;
                if (newQty > stock) {
                    JOptionPane.showMessageDialog(this, "재고 부족! 최대 " + stock + "개까지만 주문 가능.");
                    newQty = stock;
                }

                if (orderDAO.updateOrderQuantity(o.getId(), newQty)) {
                    JOptionPane.showMessageDialog(this, "수량 업데이트 완료!");
                }
            }
        }
        loadCart();
    }

    private void deleteSelected() {
        List<Order> list = orderDAO.getOrdersByUser(Session.getCurrentUser().getId());
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            if ((Boolean) model.getValueAt(i, 0)) {
                String productName = (String) model.getValueAt(i, 1);
                Product p = productDAO.getAllProducts().stream().filter(pr -> pr.getName().equals(productName)).findFirst().orElse(null);
                if (p == null) continue;

                Order o = list.stream().filter(order -> order.getProductId() == p.getId() && "CART".equals(order.getStatus())).findFirst().orElse(null);
                if (o != null) {
                    orderDAO.deleteOrder(o.getId());
                }
            }
        }
        JOptionPane.showMessageDialog(this, "삭제 완료!");
        loadCart();
    }

    private void checkoutSelected() {
        int total = 0;
        List<Order> list = orderDAO.getOrdersByUser(Session.getCurrentUser().getId());
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((Boolean) model.getValueAt(i, 0)) {
                total += (int) model.getValueAt(i, 3);
            }
        }

        if (total == 0) {
            JOptionPane.showMessageDialog(this, "선택된 항목이 없습니다.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "총 " + total + "원 결제하시겠습니까?", "결제 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        for (int i = 0; i < model.getRowCount(); i++) {
            if ((Boolean) model.getValueAt(i, 0)) {
                String productName = (String) model.getValueAt(i, 1);
                Product p = productDAO.getAllProducts().stream().filter(pr -> pr.getName().equals(productName)).findFirst().orElse(null);
                if (p == null) continue;

                Order o = list.stream().filter(order -> order.getProductId() == p.getId() && "CART".equals(order.getStatus())).findFirst().orElse(null);
                if (o != null) {
                    if (orderDAO.markOrderAsCompleted(o.getId())) {
                        productDAO.updateStock(p.getId(), -o.getQuantity());
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(this, "결제 완료!");
        loadCart();
    }
}

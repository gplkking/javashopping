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

public class OrderFrame extends JFrame {
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();
    private DefaultTableModel model;
    private JTable table;

    public OrderFrame() {
        setTitle("구매 내역");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"선택", "구매시간", "상품명", "수량", "총 가격", "상태"}, 0) {
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

        JButton btnCancel = new JButton("주문취소 요청");
        btnCancel.addActionListener(e -> requestCancel());
        add(btnCancel, BorderLayout.SOUTH);

        loadOrders();
        setVisible(true);
    }

    private void loadOrders() {
        model.setRowCount(0);
        List<Order> list = orderDAO.getOrdersByUser(Session.getCurrentUser().getId());
        for (Order o : list) {
            Product p = productDAO.getProductById(o.getProductId());
            String productName = (p != null) ? p.getName() : "알 수 없음";
            model.addRow(new Object[]{false, o.getOrderDate(), productName, o.getQuantity(), o.getTotalPrice(), o.getStatus()});
        }
    }

    private void requestCancel() {
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((Boolean) model.getValueAt(i, 0)) {
                String status = (String) model.getValueAt(i, 5);
                if ("주문취소 요청".equals(status) || "취소완료".equals(status)) {
                    JOptionPane.showMessageDialog(this, "이미 취소 요청 또는 완료된 주문입니다.");
                    continue;
                }

                int confirm = JOptionPane.showConfirmDialog(this, "주문 취소를 요청하시겠습니까?", "취소 요청", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int orderId = orderDAO.getOrdersByUser(Session.getCurrentUser().getId()).get(i).getId();
                    if (orderDAO.requestCancel(orderId)) {
                        JOptionPane.showMessageDialog(this, "주문 취소 요청 완료!");
                    } else {
                        JOptionPane.showMessageDialog(this, "요청 실패!");
                    }
                }
            }
        }
        loadOrders();
    }
}

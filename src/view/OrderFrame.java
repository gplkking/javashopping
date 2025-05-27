package view;

import javax.swing.*;
import dao.OrderDAO;
import model.Order;

import java.awt.*;
import java.util.List;

public class OrderFrame extends JFrame {
    private JTextArea taOrders;

    public OrderFrame() {
        setTitle("주문 내역");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
        setLocationRelativeTo(null);

        taOrders = new JTextArea();
        taOrders.setEditable(false);
        add(new JScrollPane(taOrders), BorderLayout.CENTER);

        loadOrders();
        setVisible(true);
    }

    private void loadOrders() {
        OrderDAO orderDAO = new OrderDAO();
        List<Order> list = orderDAO.getAllOrders();
        taOrders.setText("");
        for (Order o : list) {
            taOrders.append(o.toString() + "\n");
        }
    }
}

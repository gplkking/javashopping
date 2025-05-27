package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import dao.OrderDAO;
import model.CartManager;
import model.Order;

public class CartFrame extends JFrame {
    private CartManager cartManager;
    private JTextArea taCart;
    private JButton btnCheckout;
    private int userId; // 로그인한 사용자 ID (넘겨받기)

    public CartFrame(int userId, CartManager cartManager) {
        this.userId = userId;
        this.cartManager = cartManager;

        setTitle("장바구니");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        taCart = new JTextArea();
        taCart.setEditable(false);
        add(new JScrollPane(taCart), BorderLayout.CENTER);

        btnCheckout = new JButton("결제하기");
        btnCheckout.addActionListener(this::checkout);
        add(btnCheckout, BorderLayout.SOUTH);

        refreshCart();
        setVisible(true);
    }

    private void refreshCart() {
        List<Order> cart = cartManager.getCart();
        taCart.setText("");
        for (Order o : cart) {
            taCart.append(o.toString() + "\n");
        }
    }

    private void checkout(ActionEvent e) {
        List<Order> cart = cartManager.getCart();
        OrderDAO orderDAO = new OrderDAO();
        boolean allSuccess = true;

        for (Order o : cart) {
            o.setUserId(userId); // 로그인한 사용자 ID 설정
            boolean result = orderDAO.addOrder(o);
            if (!result) allSuccess = false;
        }

        if (allSuccess) {
            JOptionPane.showMessageDialog(this, "결제 완료!");
            cartManager.clearCart();
            refreshCart();
        } else {
            JOptionPane.showMessageDialog(this, "결제 실패: DB 오류");
        }
    }
}

package view;

import javax.swing.*;
import dao.ProductDAO;
import model.Product;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AdminFrame extends JFrame {
    private ProductDAO productDAO = new ProductDAO();
    private JTextArea taProducts;

    public AdminFrame() {
        setTitle("관리자 페이지");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
        setLocationRelativeTo(null);

        taProducts = new JTextArea();
        taProducts.setEditable(false);
        add(new JScrollPane(taProducts), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton btnLoad = new JButton("상품 목록");
        JButton btnAdd = new JButton("상품 추가");
        JButton btnDelete = new JButton("상품 삭제");
        JButton btnUpdate = new JButton("상품 수정");

        btnLoad.addActionListener(e -> loadProducts());
        btnAdd.addActionListener(e -> addProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnUpdate.addActionListener(e -> updateProduct());

        panel.add(btnLoad);
        panel.add(btnAdd);
        panel.add(btnDelete);
        panel.add(btnUpdate);

        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadProducts() {
        List<Product> list = productDAO.getAllProducts();
        taProducts.setText("");
        for (Product p : list) {
            taProducts.append(p.toString() + "\n");
        }
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog(this, "상품명:");
        String priceStr = JOptionPane.showInputDialog(this, "가격:");
        String desc = JOptionPane.showInputDialog(this, "설명:");

        try {
            int price = Integer.parseInt(priceStr);
            Product p = new Product(0, name, price, desc);
            // Insert 처리 (ProductDAO에 addProduct 메서드 필요)
            boolean result = productDAO.addProduct(p);
            if (result) {
                JOptionPane.showMessageDialog(this, "상품 등록 완료");
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "등록 실패");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "올바른 입력");
        }
    }

    private void deleteProduct() {
        String idStr = JOptionPane.showInputDialog(this, "삭제할 상품 ID:");
        try {
            int id = Integer.parseInt(idStr);
            boolean result = productDAO.deleteProduct(id);
            if (result) {
                JOptionPane.showMessageDialog(this, "삭제 성공");
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "삭제 실패");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "올바른 입력");
        }
    }

    private void updateProduct() {
        String idStr = JOptionPane.showInputDialog(this, "수정할 상품 ID:");
        try {
            int id = Integer.parseInt(idStr);
            String name = JOptionPane.showInputDialog(this, "새 이름:");
            String priceStr = JOptionPane.showInputDialog(this, "새 가격:");
            String desc = JOptionPane.showInputDialog(this, "새 설명:");
            int price = Integer.parseInt(priceStr);

            Product p = new Product(id, name, price, desc);
            boolean result = productDAO.updateProduct(p);
            if (result) {
                JOptionPane.showMessageDialog(this, "수정 성공");
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "수정 실패");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "올바른 입력");
        }
    }
}

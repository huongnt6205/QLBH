package qlbh;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Home extends JFrame {

    public Home() {
        setTitle("Trang Chủ ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(250, 250, 245)); // nền kem nhạt
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 70, 130)); // xanh navy
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel titleLabel = new JLabel("TRANG CHỦ QUẢN LÝ", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);  // chữ trắng

        // Nút đăng xuất
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setPreferredSize(new Dimension(100, 25));
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(255, 255, 204)); // nền vàng nhạt
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));

        logoutButton.addActionListener(e -> {
        // Đóng cửa sổ Home hiện tại
        dispose();
        // Mở lại màn hình Login_Signup
        Login_Signup loginSignup = new Login_Signup();
        loginSignup.setVisible(true);
});
        // Panel chứa nút đăng xuất
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(logoutPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(new Color(250, 250, 245)); // nền trùng body

        // === Bên trái: Quản lý sản phẩm ===
        JPanel productPanel = new JPanel(new BorderLayout(10, 10));
        productPanel.setBackground(new Color(210, 230, 245)); // xanh biển nhạt
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton productBtn = new JButton("Quản lý Sản phẩm");
        productBtn.addActionListener((ActionEvent e) -> {
            QLSP sp = new QLSP();
            sp.setVisible(true); // Mở màn hình QLSP
        });
        productBtn.setFont(new Font("Arial", Font.BOLD, 14));
        productBtn.setBackground(new Color(0, 70, 130)); // xanh navy
        productBtn.setForeground(Color.WHITE); // chữ trắng
        productBtn.setFocusPainted(false);

        // Panel thống kê sản phẩm
        JPanel productSummaryPanel = new JPanel();
        productSummaryPanel.setLayout(new BoxLayout(productSummaryPanel, BoxLayout.Y_AXIS));
        productSummaryPanel.setBackground(Color.WHITE);
        productSummaryPanel.setBorder(BorderFactory.createTitledBorder("Thống kê sản phẩm"));

        JLabel lblTotal = new JLabel("Tổng số sản phẩm: 16");
        JLabel lblDienThoai = new JLabel("Cushion: 4 sản phẩm");
        JLabel lblLaptop = new JLabel("Phấn mắt: 4 sản phẩm");
        JLabel lblPhuKien = new JLabel("Má hồng: 4 sản phẩm");
        JLabel lblMayTinhBang = new JLabel("Son: 4 sản phẩm");

        Font statsFont = new Font("Arial", Font.PLAIN, 13);
        for (JLabel lbl : new JLabel[]{lblTotal, lblDienThoai, lblLaptop, lblPhuKien, lblMayTinhBang}) {
            lbl.setFont(statsFont);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            productSummaryPanel.add(lbl);
            productSummaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        productPanel.add(productBtn, BorderLayout.NORTH);
        productPanel.add(productSummaryPanel, BorderLayout.CENTER);

        // === Bên phải: Quản lý bán hàng ===
        JPanel salePanel = new JPanel(new BorderLayout(10, 10));
        salePanel.setBackground(new Color(210, 230, 245)); // xanh biển nhạt
        salePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton saleBtn = new JButton("Quản lý Bán hàng");
        saleBtn.addActionListener((ActionEvent e) -> {
            new QLBH(); // Mở màn hình QLBH 
        });
        saleBtn.setFont(new Font("Arial", Font.BOLD, 14));
        saleBtn.setBackground(new Color(0, 70, 130)); // xanh navy
        saleBtn.setForeground(Color.WHITE); // chữ trắng
        saleBtn.setFocusPainted(false);

        // Panel thống kê bán hàng
        JPanel saleSummaryPanel = new JPanel();
        saleSummaryPanel.setLayout(new BoxLayout(saleSummaryPanel, BoxLayout.Y_AXIS));
        saleSummaryPanel.setBackground(Color.WHITE);
        saleSummaryPanel.setBorder(BorderFactory.createTitledBorder("Thống kê bán hàng"));

        JLabel lblTotalOrders = new JLabel("Tổng số đơn hàng: 40");
        JLabel lblCompleted = new JLabel("Đơn hàng hoàn thành: 20");
        JLabel lblPending = new JLabel("Đơn hàng chờ xử lý: 10");
        JLabel lblCancelled = new JLabel("Đơn hàng hủy: 2");

        for (JLabel lbl : new JLabel[]{lblTotalOrders, lblCompleted, lblPending, lblCancelled}) {
            lbl.setFont(statsFont);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            saleSummaryPanel.add(lbl);
            saleSummaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        salePanel.add(saleBtn, BorderLayout.NORTH);
        salePanel.add(saleSummaryPanel, BorderLayout.CENTER);

        centerPanel.add(productPanel);
        centerPanel.add(salePanel);

        add(centerPanel, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel footerPanel = new JPanel();
        footerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        footerPanel.setBackground(new Color(0, 70, 130)); // nền xanh navy
        footerPanel.setOpaque(true);

        JLabel footerLabel = new JLabel("Sinh viên thực hiện: - Nguyễn Hải Hưng Yên" + " - Ngô Thị Hường");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        footerLabel.setForeground(Color.WHITE);  // chữ trắng

        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home());
    }
}

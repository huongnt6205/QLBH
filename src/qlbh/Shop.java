package qlbh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Shop extends JFrame {
    private JPanel menuPanel;
    private JPanel productPanel;
    private List<Product> products;

    // Màu sắc và font mặc định
    private static final Color BG_COLOR = new Color(253, 246, 236);
    private static final Color MENU_COLOR = new Color(212, 191, 170);
    private static final Color MENU_HOVER = new Color(196, 169, 138);
    private static final Color PRICE_COLOR = new Color(184, 92, 56);
    private static final Font FONT_SEGOE_13 = new Font("Segoe UI", Font.PLAIN, 13);

    private boolean menuVisible = false;
    private JButton selectedCategoryButton = null;

    public Shop() {
        loadProductsFromDatabase();

        setTitle("Cửa hàng sản phẩm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Panel danh mục
        menuPanel = new JPanel();
        menuPanel.setBackground(MENU_COLOR);
        menuPanel.setPreferredSize(new Dimension(130, 0));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        String[] categories = {"Trang chủ", "Sản phẩm"};
        for (String cat : categories) {
            JButton btn = createCategoryButton(cat);
            menuPanel.add(btn);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            if (cat.equalsIgnoreCase("Sản phẩm")) {
                selectedCategoryButton = btn;
            }
        }
        menuPanel.setVisible(menuVisible);

        // Panel sản phẩm
        productPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        productPanel.setBackground(BG_COLOR);
        productPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        showAllProducts();

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Nút tìm kiếm
        JButton searchBtn = new JButton("Tìm kiếm sản phẩm");
        searchBtn.addActionListener(e -> new Search());
        searchBtn.setBackground(MENU_COLOR);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);

        // Nút toggle menu
        JButton toggleMenuBtn = new JButton("☰ Danh mục");
        toggleMenuBtn.setBackground(MENU_COLOR);
        toggleMenuBtn.setForeground(Color.WHITE);
        toggleMenuBtn.setFocusPainted(false);
        toggleMenuBtn.addActionListener(e -> {
            menuVisible = !menuVisible;
            menuPanel.setVisible(menuVisible);
            revalidate();
            repaint();
        });

        // Thanh top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_COLOR);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setBackground(BG_COLOR);
        leftPanel.add(toggleMenuBtn);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(BG_COLOR);
        rightPanel.add(searchBtn);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // Giao diện chính
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(menuPanel, BorderLayout.WEST);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadProductsFromDatabase() {
        this.products = Database.getAllProducts();
    }

    private JButton createCategoryButton(String cat) {
        JButton btn = new JButton(cat);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(130, 36));
        btn.setFont(FONT_SEGOE_13);
        btn.setBackground(MENU_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MENU_COLOR.darker(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn != selectedCategoryButton) {
                    btn.setBackground(MENU_HOVER);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn != selectedCategoryButton) {
                    btn.setBackground(MENU_COLOR);
                }
            }
        });

        btn.addActionListener(e -> {
            if (cat.equalsIgnoreCase("Trang chủ")) {
                this.dispose();
                new Home().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Bạn đang ở trang Sản phẩm.");
            }

            if (selectedCategoryButton != null) {
                selectedCategoryButton.setBackground(MENU_COLOR);
            }
            selectedCategoryButton = btn;
            btn.setBackground(MENU_HOVER);
        });

        return btn;
    }

    private void showAllProducts() {
        productPanel.removeAll();
        for (Product p : products) {
            productPanel.add(createProductCard(p));
        }
        productPanel.revalidate();
        productPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 330));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 220, 210), 1));
        card.setOpaque(true);

        ImageIcon icon;
        try {
            if (product.getImage() != null) {
                icon = new ImageIcon(getClass().getClassLoader().getResource(product.getImage()));
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            } else {
                icon = new ImageIcon("images/default.png");
            }
        } catch (Exception e) {
            icon = new ImageIcon("images/default.png");
        }

        JLabel imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(50, 40, 30));

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        JLabel priceLabel = new JLabel(format.format(product.getPrice()), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        priceLabel.setForeground(PRICE_COLOR);

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        JPanel adminInfoPanel = new JPanel();
        adminInfoPanel.setLayout(new BoxLayout(adminInfoPanel, BoxLayout.Y_AXIS));
        adminInfoPanel.setOpaque(true);
        adminInfoPanel.setBackground(Color.WHITE);
        adminInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRICE_COLOR, 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        adminInfoPanel.setVisible(false);

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 13);
        Color infoColor = new Color(70, 70, 70);

        JLabel[] labels = {
                new JLabel("• Số lượng: " + product.getQuantity()),
                new JLabel("• Trạng thái: " + product.getStatus()),
                new JLabel("• Mô tả: " + product.getDescription())
        };

        for (JLabel lbl : labels) {
            lbl.setFont(infoFont);
            lbl.setForeground(infoColor);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            lbl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            adminInfoPanel.add(lbl);
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        bottomPanel.add(adminInfoPanel, BorderLayout.CENTER);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                adminInfoPanel.setVisible(true);
                card.setBorder(BorderFactory.createLineBorder(PRICE_COLOR, 2, true));
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.setBackground(new Color(255, 255, 245));
                card.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                adminInfoPanel.setVisible(false);
                card.setBorder(BorderFactory.createLineBorder(new Color(230, 220, 210), 1));
                card.setCursor(Cursor.getDefaultCursor());
                card.setBackground(Color.WHITE);
                card.repaint();
            }
        });

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Shop::new);
    }
}


    
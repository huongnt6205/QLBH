package qlbh;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Search extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private List<Product> products;  // Danh sách sản phẩm lấy từ Database

    public Search() {
        setTitle("Màn hình Tra cứu sản phẩm");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Lấy danh sách sản phẩm từ database
        products = Database.getAllProducts();

        // Màu nền chính: kem nhẹ
        Color backgroundColor = new Color(248, 245, 240);
        Color blueSoft = new Color(36, 67, 101);
        Color blueLight = new Color(173, 204, 227);
        Color blueButton = new Color(28, 94, 155);

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);

        // Panel thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(backgroundColor);

        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchField.setForeground(blueSoft);
        searchField.setBackground(Color.WHITE);
        searchField.setCaretColor(blueSoft);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(blueLight, 2),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        searchField.setToolTipText("Nhập từ khóa tìm kiếm...");

        searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(blueButton);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(blueSoft);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(blueButton);
            }
        });

        JLabel searchLabel = new JLabel("Tìm sản phẩm:");
        searchLabel.setForeground(blueSoft);
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Bảng tra cứu
        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Danh mục", "Giá (VNĐ)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho chỉnh sửa bảng
            }
        };
        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultTable.setRowHeight(28);
        resultTable.setFillsViewportHeight(true);
        resultTable.setBackground(Color.WHITE);
        resultTable.setForeground(blueSoft);
        resultTable.setGridColor(blueLight);
        resultTable.setShowGrid(true);

        // Header bảng
        resultTable.getTableHeader().setBackground(blueSoft);
        resultTable.getTableHeader().setForeground(Color.WHITE);
        resultTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(blueLight, 2));

        // Panel nút Quay lại
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(backgroundColor);

        JButton backButton = new JButton("← Quay lại");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(blueLight);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(blueSoft);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(blueLight);
            }
        });
        backButton.addActionListener(e -> {
            this.dispose();
            Shop shop = new Shop();
            shop.setVisible(true);
        });
        backPanel.add(backButton);

        // Thêm sự kiện nút tìm kiếm
        searchButton.addActionListener(e -> performSearch());

        // Nhấn Enter trong ô tìm kiếm cũng kích hoạt tìm kiếm
        searchField.addActionListener(e -> performSearch());

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void performSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm để tìm kiếm.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Product> matchedProducts = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        for (Product p : matchedProducts) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice()
            });
        }

        if (matchedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm phù hợp.",
                    "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Search::new);
    }
}

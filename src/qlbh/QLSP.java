package qlbh;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class QLSP extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton addButton, editButton, deleteButton;
    private final int IMAGE_WIDTH = 60;
    private final int IMAGE_HEIGHT = 60;

    public QLSP() {
        setTitle("QLSP");
        setSize(1100, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(245, 240, 230));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 240, 230));

        JLabel titleLabel = new JLabel("Quản lý sản phẩm", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(80, 60, 40));

        JButton backButton = new JButton("← Quay lại");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setBackground(new Color(173, 216, 230));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(120, 35));
        backButton.addActionListener(e -> {
            dispose();     // Đóng QLBH hiện tại
            new Home();    // Mở lại giao diện Home.java
        });
       

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Mã SP", "Tên sản phẩm", "Ảnh", "Danh mục", "Giá bán", "Số lượng", "Trạng thái", "Mô tả"};
        model = new DefaultTableModel(null, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? ImageIcon.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        
        table = new JTable(model);
        table.setRowHeight(IMAGE_HEIGHT + 10);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(200, 180, 150));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 210, 200));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(255, 250, 240));
        add(scrollPane, BorderLayout.CENTER);

        addButton = createStyledButton("Thêm", new Color(144, 190, 109));
        editButton = createStyledButton("Sửa", new Color(255, 200, 87));
        deleteButton = createStyledButton("Xóa", new Color(229, 89, 52));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        buttonPanel.setBackground(new Color(245, 240, 230));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadDataFromDB();

        addButton.addActionListener(e -> openProductDialog(null));
        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm để sửa.");
                return;
            }
            openProductDialog(row);
        });
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = (String) model.getValueAt(row, 0);
                if (deleteProductFromDB(id)) {
                    model.removeRow(row);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!");
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(100, 35));
        return button;
    }

    private ImageIcon loadAndResizeImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private String saveSelectedImage(ImageIcon imageIcon, String productId) {
        try {
            File dir = new File("images");
            if (!dir.exists()) dir.mkdir();

            String imageName = productId + ".png";
            File outputFile = new File(dir, imageName);

            Image img = imageIcon.getImage();
            BufferedImage buffered = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffered.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();

            javax.imageio.ImageIO.write(buffered, "png", outputFile);
            return imageName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

   private void openProductDialog(Integer editRow) {
    JDialog dialog = new JDialog(this, editRow == null ? "Thêm sản phẩm" : "Sửa sản phẩm", true);
    dialog.setSize(500, 600);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(new Color(255, 250, 240));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JTextField idField = new JTextField();
    JTextField nameField = new JTextField();
    JLabel imageLabel = new JLabel("");
    JButton chooseImageBtn = new JButton("Chọn ảnh");
    JTextField categoryField = new JTextField();
    JTextField priceField = new JTextField();
    JTextField quantityField = new JTextField();
    JTextField statusField = new JTextField();
    JTextArea descriptionArea = new JTextArea(5, 20);
    JScrollPane scroll = new JScrollPane(descriptionArea);

    ImageIcon[] selectedImage = new ImageIcon[1];

    addLabelAndComponent(formPanel, gbc, 0, "Mã SP:", idField);
    addLabelAndComponent(formPanel, gbc, 1, "Tên:", nameField);
    addLabelAndComponent(formPanel, gbc, 2, "Ảnh:", imageLabel);
    gbc.gridx = 1; gbc.gridy = 3;
    formPanel.add(chooseImageBtn, gbc);
    addLabelAndComponent(formPanel, gbc, 4, "Danh mục:", categoryField);
    addLabelAndComponent(formPanel, gbc, 5, "Giá bán:", priceField);
    addLabelAndComponent(formPanel, gbc, 6, "Số lượng:", quantityField);
    addLabelAndComponent(formPanel, gbc, 7, "Trạng thái:", statusField);
    addLabelAndComponent(formPanel, gbc, 8, "Mô tả:", scroll);

    if (editRow != null) {
        idField.setText((String) model.getValueAt(editRow, 0));
        nameField.setText((String) model.getValueAt(editRow, 1));
        ImageIcon oldIcon = (ImageIcon) model.getValueAt(editRow, 2);
        selectedImage[0] = oldIcon;
        imageLabel.setIcon(oldIcon);
        categoryField.setText((String) model.getValueAt(editRow, 3));
        priceField.setText((String) model.getValueAt(editRow, 4));
        quantityField.setText((String) model.getValueAt(editRow, 5));
        statusField.setText((String) model.getValueAt(editRow, 6));
        descriptionArea.setText((String) model.getValueAt(editRow, 7));
        idField.setEnabled(false); // không cho sửa mã
    }

    chooseImageBtn.addActionListener(e -> {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            selectedImage[0] = new ImageIcon(file.getAbsolutePath());
            imageLabel.setIcon(new ImageIcon(selectedImage[0].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        }
    });

    JButton saveBtn = new JButton("Lưu");
    saveBtn.addActionListener(e -> {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String price = priceField.getText().trim();
        String quantity = quantityField.getText().trim();
        String status = statusField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (id.isEmpty() || name.isEmpty() || category.isEmpty() || price.isEmpty() || quantity.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        ImageIcon icon = selectedImage[0];
        if (icon == null) {
            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ảnh sản phẩm!");
            return;
        }

        String imageFileName = saveSelectedImage(icon, id); // Lưu ảnh vào thư mục
        Object[] rowData = {id, name, new ImageIcon(icon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH)), category, price, quantity, status, description};

        if (editRow == null) {
            if (insertProductToDB(rowData, imageFileName)) {
                model.addRow(rowData);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thất bại!");
            }
        } else {
            if (updateProductToDB(id, rowData)) {
                for (int i = 0; i < rowData.length; i++) {
                    model.setValueAt(rowData[i], editRow, i);
                }
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật sản phẩm thất bại!");
            }
        }
    });

    JPanel bottom = new JPanel();
    bottom.add(saveBtn);

    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(bottom, BorderLayout.SOUTH);
    dialog.setVisible(true);
}
        public void openEditProductById(String productId) {
         // Tìm vị trí row trong bảng theo id
         for (int i = 0; i < model.getRowCount(); i++) {
             if (model.getValueAt(i, 0).equals(productId)) {
                 // Mở dialog sửa với row tương ứng
                 openProductDialog(i);
                 return;
             }
         }
         // Nếu không tìm thấy thì có thể tải lại dữ liệu rồi thử lại hoặc báo lỗi
         JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với mã: " + productId);
     }



    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, int y, String label, Component comp) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel jlabel = new JLabel(label);
        jlabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(jlabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
    }

    private void loadDataFromDB() {
        model.setRowCount(0);
        String sql = "SELECT * FROM products";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String imageFileName = rs.getString("image");
                ImageIcon icon = loadAndResizeImage("images/" + imageFileName);
                String category = rs.getString("category");
                String price = String.valueOf(rs.getInt("price"));
                String quantity = String.valueOf(rs.getInt("quantity"));
                String status = rs.getString("status");
                String description = rs.getString("description");

                model.addRow(new Object[]{id, name, icon, category, price, quantity, status, description});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu từ database!");
        }
    }

    private boolean insertProductToDB(Object[] rowData, String imageFileName) {
        String sql = "INSERT INTO products (id, name, image, category, price, quantity, status, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, (String) rowData[0]);
            stmt.setString(2, (String) rowData[1]);
            stmt.setString(3, imageFileName);
            stmt.setString(4, (String) rowData[3]);
            stmt.setInt(5, Integer.parseInt((String) rowData[4]));
            stmt.setInt(6, Integer.parseInt((String) rowData[5]));
            stmt.setString(7, (String) rowData[6]);
            stmt.setString(8, (String) rowData[7]);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateProductToDB(String id, Object[] rowData) {
        String sql = "UPDATE products SET name=?, image=?, category=?, price=?, quantity=?, status=?, description=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String imageName = saveSelectedImage((ImageIcon) rowData[2], id);
            stmt.setString(1, (String) rowData[1]);
            stmt.setString(2, imageName);
            stmt.setString(3, (String) rowData[3]);
            stmt.setInt(4, Integer.parseInt((String) rowData[4]));
            stmt.setInt(5, Integer.parseInt((String) rowData[5]));
            stmt.setString(6, (String) rowData[6]);
            stmt.setString(7, (String) rowData[7]);
            stmt.setString(8, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteProductFromDB(String id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class DBConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/qlbh?useSSL=false&serverTimezone=UTC";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QLSP().setVisible(true));
    }
}

package qlbh;

import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.SQLException;


public class QLBH extends JFrame {

    private DefaultTableModel orderModel;
     private JButton addButton, editButton, deleteButton;
    private JTable orderTable;

    public QLBH() {
        setTitle("Quản lý bán hàng");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar trái
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(250, 250, 240));
        sidebar.setLayout(new GridLayout(5, 1, 0, 5));
        sidebar.setPreferredSize(new Dimension(180, getHeight()));

        String[] menuItems = {
            "Đơn hàng", "Khách hàng", "Nhập kho", "Tồn kho", "Doanh số"
        };

        JTabbedPane tabbedPane = new JTabbedPane();

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(255, 165, 0));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(255, 140, 0));
                }
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(255, 165, 0));
                }
            });
            sidebar.add(btn);

            btn.addActionListener(e -> {
                switch (item) {
                    case "Đơn hàng" -> tabbedPane.setSelectedIndex(0);
                    case "Khách hàng" -> tabbedPane.setSelectedIndex(1);
                    case "Nhập kho" -> tabbedPane.setSelectedIndex(2);
                    case "Tồn kho" -> tabbedPane.setSelectedIndex(3);
                    case "Doanh số" -> tabbedPane.setSelectedIndex(4);
                }
            });
        }

        add(sidebar, BorderLayout.WEST);

        // Header trên cùng
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(getWidth(), 50));
        header.setBackground(new Color(255, 204, 153));

        JLabel titleLabel = new JLabel("QUẢN LÝ BÁN HÀNG");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        header.add(titleLabel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // Các tab nội dung
        tabbedPane.addTab("Đơn hàng", createDonHangPanel());
        tabbedPane.addTab("Khách hàng", createKhachHangPanel());
        tabbedPane.addTab("Nhập kho", createNhapKhoPanel());
        tabbedPane.addTab("Tồn kho", new JPanel());
        tabbedPane.addTab("Doanh số", new JPanel());
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createDonHangPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = { "Mã đơn hàng", "Khách hàng", "Sản phẩm", "Số lượng", "Tổng giá trị", "Ngày đặt", "Trạng thái"};
        orderModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // không cho sửa trực tiếp trên bảng
            }
        };
        orderTable = new JTable(orderModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnSearch = new JButton("Tìm");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnSearch);

        panel.add(btnPanel, BorderLayout.SOUTH);

        loadOrdersToTable();

        btnAdd.addActionListener(e -> {
            Order o = showOrderDialog(null);
            if (o != null) {
                OrderSP.insertOrder(o);
                loadOrdersToTable();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng để sửa!");
                return;
            }
            Order o = new Order();
            o.setOrderCode((String) orderModel.getValueAt(row, 0));
            o.setCustomerName((String) orderModel.getValueAt(row, 1));
            o.setProductName((String) orderModel.getValueAt(row, 2));
            o.setQuantity((int) orderModel.getValueAt(row, 3));
            o.setTotalPrice(Double.parseDouble(orderModel.getValueAt(row, 4).toString()));
            o.setOrderDate((String) orderModel.getValueAt(row, 5));
            o.setStatus((String) orderModel.getValueAt(row, 6));

            Order editedOrder = showOrderDialog(o);
            if (editedOrder != null) {
                OrderSP.updateOrder(editedOrder);
                loadOrdersToTable();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng để xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa đơn hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) orderModel.getValueAt(row, 0);
                loadOrdersToTable();
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog(this, "Nhập tên khách hàng cần tìm:");
            if (keyword != null && !keyword.trim().isEmpty()) {
                searchOrders(keyword.trim());
            }
        });

        return panel;
    }

    private void loadOrdersToTable() {
        orderModel.setRowCount(0);
        ArrayList<Order> orders = OrderSP.getAllOrders();
        for (Order o : orders) {
            orderModel.addRow(new Object[]{
                o.getOrderCode(),
                o.getCustomerName(),
                o.getProductName(),
                o.getQuantity(),
                o.getTotalPrice(),
                o.getOrderDate(),
                o.getStatus()
            });
        }
    }

    private void searchOrders(String keyword) {
        orderModel.setRowCount(0);
        ArrayList<Order> results = OrderSP.searchOrders(keyword);
        for (Order o : results) {
            orderModel.addRow(new Object[]{
                o.getOrderCode(),
                o.getCustomerName(),
                o.getProductName(),
                o.getQuantity(),
                o.getTotalPrice(),
                o.getOrderDate(),
                o.getStatus()
            });
        }
    }

    // Hiện dialog nhập sửa đơn hàng
    private Order showOrderDialog(Order order) {
        JTextField txtOrderCode = new JTextField();
        JTextField txtCustomerName = new JTextField();
        JTextField txtProductName = new JTextField();
        JTextField txtQuantity = new JTextField();
        JTextField txtTotalPrice = new JTextField();
        JTextField txtOrderDate = new JTextField();
        JTextField txtStatus = new JTextField();

        if (order != null) {
            txtOrderCode.setText(order.getOrderCode());
            txtCustomerName.setText(order.getCustomerName());
            txtProductName.setText(order.getProductName());
            txtQuantity.setText(String.valueOf(order.getQuantity()));
            txtTotalPrice.setText(String.valueOf(order.getTotalPrice()));
            txtOrderDate.setText(order.getOrderDate());
            txtStatus.setText(order.getStatus());
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Mã đơn hàng:"));
        panel.add(txtOrderCode);
        panel.add(new JLabel("Khách hàng:"));
        panel.add(txtCustomerName);
        panel.add(new JLabel("Sản phẩm:"));
        panel.add(txtProductName);
        panel.add(new JLabel("Số lượng:"));
        panel.add(txtQuantity);
        panel.add(new JLabel("Tổng giá trị:"));
        panel.add(txtTotalPrice);
        panel.add(new JLabel("Ngày đặt (YYYY-MM-DD):"));
        panel.add(txtOrderDate);
        panel.add(new JLabel("Trạng thái:"));
        panel.add(txtStatus);

        int result = JOptionPane.showConfirmDialog(this, panel, order == null ? "Thêm đơn hàng" : "Sửa đơn hàng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Order o = order != null ? order : new Order();
                o.setOrderCode(txtOrderCode.getText().trim());
                o.setCustomerName(txtCustomerName.getText().trim());
                o.setProductName(txtProductName.getText().trim());
                o.setQuantity(Integer.parseInt(txtQuantity.getText().trim()));
                o.setTotalPrice(Double.parseDouble(txtTotalPrice.getText().trim()));
                o.setOrderDate(txtOrderDate.getText().trim());
                o.setStatus(txtStatus.getText().trim());
                return o;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ!");
                return null;
            }
        }

        return null;
    }
    private DefaultTableModel customerModel;
    private JTable customerTable;
    private JPanel createKhachHangPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    String[] cols = {"ID", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ"};
    customerModel = new DefaultTableModel(cols, 0) {
        public boolean isCellEditable(int row, int column) {
            return false; // Không cho sửa trực tiếp trên bảng
        }
    };
    customerTable = new JTable(customerModel);
    customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(customerTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton btnAdd = new JButton("Thêm");
    JButton btnEdit = new JButton("Sửa");
    JButton btnDelete = new JButton("Xóa");
    JButton btnSearch = new JButton("Tìm");

    btnPanel.add(btnAdd);
    btnPanel.add(btnEdit);
    btnPanel.add(btnDelete);
    btnPanel.add(btnSearch);

    panel.add(btnPanel, BorderLayout.SOUTH);

    loadCustomersToTable();

    btnAdd.addActionListener(e -> {
        Customer c = showCustomerDialog(null);
        if (c != null) {
            CustomerSP.insertCustomer(c);
            loadCustomersToTable();
        }
    });

    btnEdit.addActionListener(e -> {
        int row = customerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để sửa!");
            return;
        }
        Customer c = new Customer();
        c.setId((int) customerModel.getValueAt(row, 0));
        c.setName((String) customerModel.getValueAt(row, 1));
        c.setPhone((String) customerModel.getValueAt(row, 2));
        c.setEmail((String) customerModel.getValueAt(row, 3));
        c.setAddress((String) customerModel.getValueAt(row, 4));

        Customer editedCustomer = showCustomerDialog(c);
        if (editedCustomer != null) {
            CustomerSP.updateCustomer(editedCustomer);
            loadCustomersToTable();
        }
    });

    btnDelete.addActionListener(e -> {
        int row = customerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) customerModel.getValueAt(row, 0);
            CustomerSP.deleteCustomer(id);
            loadCustomersToTable();
        }
    });

    btnSearch.addActionListener(e -> {
        String keyword = JOptionPane.showInputDialog(this, "Nhập tên khách hàng cần tìm:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            searchCustomers(keyword.trim());
        }
    });

    return panel;
}

private void loadCustomersToTable() {
    customerModel.setRowCount(0);
    ArrayList<Customer> list = CustomerSP.getAllCustomers();
    for (Customer c : list) {
        customerModel.addRow(new Object[]{
            c.getId(),
            c.getName(),
            c.getPhone(),
            c.getEmail(),
            c.getAddress()
        });
    }
}

private void searchCustomers(String keyword) {
    customerModel.setRowCount(0);
    ArrayList<Customer> list = CustomerSP.searchCustomers(keyword);
    for (Customer c : list) {
        customerModel.addRow(new Object[]{
            c.getId(),
            c.getName(),
            c.getPhone(),
            c.getEmail(),
            c.getAddress()
        });
    }
}
// Dialog thêm/sửa khách hàng
private Customer showCustomerDialog(Customer customer) {
    JTextField txtName = new JTextField();
    JTextField txtPhone = new JTextField();
    JTextField txtEmail = new JTextField();
    JTextField txtAddress = new JTextField();

    if (customer != null) {
        txtName.setText(customer.getName());
        txtPhone.setText(customer.getPhone());
        txtEmail.setText(customer.getEmail());
        txtAddress.setText(customer.getAddress());
    }

    JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
    panel.add(new JLabel("Tên khách hàng:"));
    panel.add(txtName);
    panel.add(new JLabel("Số điện thoại:"));
    panel.add(txtPhone);
    panel.add(new JLabel("Email:"));
    panel.add(txtEmail);
    panel.add(new JLabel("Địa chỉ:"));
    panel.add(txtAddress);

    int result = JOptionPane.showConfirmDialog(this, panel, customer == null ? "Thêm khách hàng" : "Sửa khách hàng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        try {
            Customer c = customer != null ? customer : new Customer();
            c.setName(txtName.getText().trim());
            c.setPhone(txtPhone.getText().trim());
            c.setEmail(txtEmail.getText().trim());
            c.setAddress(txtAddress.getText().trim());
            return c;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ!");
            return null;
        }
    }
    return null;
}

private JPanel createNhapKhoPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    // Bảng
    DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá nhập", "Ngày nhập", "Nhà cung cấp"}, 0);
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Ô nhập liệu
    JPanel inputPanel = new JPanel(new GridLayout(2, 6, 5, 5));
    JTextField tfTenSP = new JTextField();
    JTextField tfSoLuong = new JTextField();
    JTextField tfGiaNhap = new JTextField();
    JTextField tfNgayNhap = new JTextField();
    JTextField tfNhaCungCap = new JTextField();
    JTextField tfTimKiem = new JTextField();

    inputPanel.add(new JLabel("Tên SP:"));
    inputPanel.add(tfTenSP);
    inputPanel.add(new JLabel("Số lượng:"));
    inputPanel.add(tfSoLuong);
    inputPanel.add(new JLabel("Giá nhập:"));
    inputPanel.add(tfGiaNhap);

    inputPanel.add(new JLabel("Ngày nhập:"));
    inputPanel.add(tfNgayNhap);
    inputPanel.add(new JLabel("Nhà cung cấp:"));
    inputPanel.add(tfNhaCungCap);
    inputPanel.add(new JLabel("Tìm kiếm:"));
    inputPanel.add(tfTimKiem);

    panel.add(inputPanel, BorderLayout.NORTH);

    // Nút chức năng
    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton btnThem = new JButton("Thêm");
    JButton btnSua = new JButton("Sửa");
    JButton btnXoa = new JButton("Xóa");
    JButton btnTim = new JButton("Tìm");
    buttonPanel.add(btnThem);
    buttonPanel.add(btnSua);
    buttonPanel.add(btnXoa);
    buttonPanel.add(btnTim);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    // Load dữ liệu
    Runnable loadData = () -> {
        tableModel.setRowCount(0);
        for (Nhapkho nk : NhapkhoSP.getAllNhapKho()) {
            tableModel.addRow(new Object[]{
                nk.getId(), nk.getTenSanPham(), nk.getSoLuong(),
                nk.getGiaNhap(), nk.getNgayNhap(), nk.getNhaCungCap()
            });
        }
    };
    loadData.run();

    // Thêm
    btnThem.addActionListener(e -> {
        Nhapkho nk = new Nhapkho();
        nk.setTenSanPham(tfTenSP.getText());
        nk.setSoLuong(Integer.parseInt(tfSoLuong.getText()));
        nk.setGiaNhap(Double.parseDouble(tfGiaNhap.getText()));
        nk.setNgayNhap(tfNgayNhap.getText());
        nk.setNhaCungCap(tfNhaCungCap.getText());
        NhapkhoSP.insertNhapKho(nk);
        loadData.run();
    });

    // Sửa
    btnSua.addActionListener(e -> {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            Nhapkho nk = new Nhapkho();
            nk.setId((int) tableModel.getValueAt(selected, 0));
            nk.setTenSanPham(tfTenSP.getText());
            nk.setSoLuong(Integer.parseInt(tfSoLuong.getText()));
            nk.setGiaNhap(Double.parseDouble(tfGiaNhap.getText()));
            nk.setNgayNhap(tfNgayNhap.getText());
            nk.setNhaCungCap(tfNhaCungCap.getText());
            NhapkhoSP.updateNhapkho(nk);
            loadData.run();
        }
    });

    // Xóa
    btnXoa.addActionListener(e -> {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            int id = (int) tableModel.getValueAt(selected, 0);
            NhapkhoSP.deleteNhapKho(id);
            loadData.run();
        }
    });

    // Tìm
    btnTim.addActionListener(e -> {
        String keyword = tfTimKiem.getText();
        ArrayList<Nhapkho> list = NhapkhoSP.searchNhapKho(keyword);
        tableModel.setRowCount(0);
        for (Nhapkho nk : list) {
            tableModel.addRow(new Object[]{
                nk.getId(), nk.getTenSanPham(), nk.getSoLuong(),
                nk.getGiaNhap(), nk.getNgayNhap(), nk.getNhaCungCap()
            });
        }
    });

    // Click vào bảng -> hiện thông tin
    table.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            tfTenSP.setText(tableModel.getValueAt(row, 1).toString());
            tfSoLuong.setText(tableModel.getValueAt(row, 2).toString());
            tfGiaNhap.setText(tableModel.getValueAt(row, 3).toString());
            tfNgayNhap.setText(tableModel.getValueAt(row, 4).toString());
            tfNhaCungCap.setText(tableModel.getValueAt(row, 5).toString());
        }
    });

    return panel;
}

     public static class DBConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/qlbh?useSSL=false&serverTimezone=UTC";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection getConnection() throws SQLException {
            return (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
    public static void main(String[] args) {
        // Chạy ứng dụng Swing trên Event Dispatch Thread
        SwingUtilities.invokeLater(QLBH::new);
    }
}

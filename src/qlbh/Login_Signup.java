package qlbh;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Login_Signup extends JFrame {
    private JTabbedPane tabbedPane;

    public Login_Signup() {
        setTitle("Login / Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Đăng nhập", createLoginPanel());
        tabbedPane.addTab("Đăng ký", createSignupPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        JTextField usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Tên đăng nhập"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(33, 150, 243));
        loginBtn.setForeground(Color.WHITE);

        JLabel signupLink = new JLabel("<HTML><U>Bạn chưa có tài khoản? Đăng ký ngay!</U></HTML>");
        signupLink.setForeground(Color.BLUE);
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tabbedPane.setSelectedIndex(1);
            }
        });

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kết nối CSDL kiểm tra đăng nhập
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password); 

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new Shop(); // Giao diện chính sau đăng nhập
                } else {
                    JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(loginBtn);
        panel.add(new JLabel());  // Khoảng trắng
        panel.add(signupLink);

        return panel;
    }

    private JPanel createSignupPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        JTextField usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Họ tên"));

        JTextField emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("E-mail"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));

        JButton signupBtn = new JButton("Đăng ký tài khoản");
        signupBtn.setBackground(new Color(0, 150, 136));
        signupBtn.setForeground(Color.WHITE);

        JLabel loginLink = new JLabel("<HTML><U>Đã có tài khoản? Đăng nhập</U></HTML>");
        loginLink.setForeground(Color.BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tabbedPane.setSelectedIndex(0);
            }
        });

        signupBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra tồn tại user và thêm mới
            try (Connection conn = Database.getConnection()) {
                String checkUser = "SELECT * FROM users WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkUser);
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Tên người dùng đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {
                    String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, password); // Nên mã hóa password thực tế

                    int rowsInserted = stmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Đăng ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        tabbedPane.setSelectedIndex(0);
                    } else {
                        JOptionPane.showMessageDialog(this, "Đăng ký thất bại, vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(usernameField);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(signupBtn);
        panel.add(new JLabel());
        panel.add(loginLink);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login_Signup().setVisible(true));
    }
}

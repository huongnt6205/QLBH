package qlbh;

import java.sql.*;
import java.util.ArrayList;

public class OrderSP {

    // Lấy tất cả đơn hàng
    public static ArrayList<Order> getAllOrders() {
        ArrayList<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = QLBH.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order o = new Order();
                o.setOrderCode(rs.getString("order_code"));
                o.setCustomerName(rs.getString("customer_name"));
                o.setProductName(rs.getString("product_name"));
                o.setQuantity(rs.getInt("quantity"));
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setOrderDate(rs.getString("order_date"));
                o.setStatus(rs.getString("status"));
                 list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm đơn hàng
    public static boolean insertOrder(Order o) {
        String sql = "INSERT INTO orders(order_code, customer_name, product_name, quantity, total_price, order_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getOrderCode());
            ps.setString(2, o.getCustomerName());
            ps.setString(3, o.getProductName());
            ps.setInt(4, o.getQuantity());
            ps.setDouble(5, o.getTotalPrice());
            ps.setString(6, o.getOrderDate());
            ps.setString(7, o.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật đơn hàng
    public static boolean updateOrder(Order o) {
        String sql = "UPDATE orders SET customer_name = ?, product_name = ?, quantity = ?, total_price = ?, order_date = ?, status = ?" +
                     "WHERE order_code = ?";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getCustomerName());
            ps.setString(2, o.getProductName());
            ps.setInt(3, o.getQuantity());
            ps.setDouble(4, o.getTotalPrice());
            ps.setString(5, o.getOrderDate());
            ps.setString(6, o.getStatus());
            ps.setString(7, o.getOrderCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa đơn hàng theo mã đơn hàng
    public static boolean deleteOrder(String orderCode) {
        String sql = "DELETE FROM orders WHERE order_code = ?";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm đơn hàng theo tên khách hàng
    public static ArrayList<Order> searchOrders(String keyword) {
        ArrayList<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_name LIKE ? ";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderCode(rs.getString("order_code"));
                o.setCustomerName(rs.getString("customer_name"));
                o.setProductName(rs.getString("product_name"));
                o.setQuantity(rs.getInt("quantity"));
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setOrderDate(rs.getString("order_date"));
                o.setStatus(rs.getString("status"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

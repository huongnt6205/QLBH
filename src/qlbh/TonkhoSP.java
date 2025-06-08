package qlbh;

import java.sql.*;
import java.util.*;

public class TonkhoSP {

    public static ArrayList<Tonkho> getAllTonkho() {
        ArrayList<Tonkho> list = new ArrayList<>();
        try (Connection conn = QLBH.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tonkho")) {
            while (rs.next()) {
                Tonkho tk = new Tonkho();
                tk.setMaSanPham(rs.getString("ma_san_pham"));
                tk.setTenSanPham(rs.getString("ten_san_pham"));
                tk.setSoLuongTon(rs.getInt("so_luong_ton"));
                tk.setGhiChu(rs.getString("ghi_chu"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insertTonkho(Tonkho tk) {
        String sql = "INSERT INTO tonkho (ma_san_pham, ten_san_pham, so_luong_ton, ghi_chu) VALUES (?, ?, ?, ?)";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tk.getMaSanPham());
            stmt.setString(2, tk.getTenSanPham());
            stmt.setInt(3, tk.getSoLuongTon());
            stmt.setString(4, tk.getGhiChu());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTonkho(Tonkho tk) {
        String sql = "UPDATE tonkho SET ten_san_pham=?, so_luong_ton=?,  ghi_chu=? WHERE ma_san_pham=?";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tk.getTenSanPham());
            stmt.setInt(2, tk.getSoLuongTon());
            stmt.setString(3, tk.getGhiChu());
            stmt.setString(4, tk.getMaSanPham());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTonkho(String maSP) {
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tonkho WHERE ma_san_pham=?")) {
            stmt.setString(1, maSP);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tonkho> searchTonkho(String keyword) {
        ArrayList<Tonkho> list = new ArrayList<>();
        String sql = "SELECT * FROM tonkho WHERE ten_san_pham LIKE ?";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Tonkho tk = new Tonkho();
                tk.setMaSanPham(rs.getString("ma_san_pham"));
                tk.setTenSanPham(rs.getString("ten_san_pham"));
                tk.setSoLuongTon(rs.getInt("so_luong_ton"));
                tk.setGhiChu(rs.getString("ghi_chu"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

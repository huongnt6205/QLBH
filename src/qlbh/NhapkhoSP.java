/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qlbh;

import java.sql.*;
import java.util.*;

public class NhapkhoSP {
    public static ArrayList<Nhapkho> getAllNhapKho() {
        ArrayList<Nhapkho> list = new ArrayList<>();
        try (Connection conn = QLBH.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM nhapkho")) {
            while (rs.next()) {
                Nhapkho nk = new Nhapkho();
                nk.setId(rs.getInt("id"));
                nk.setTenSanPham(rs.getString("ten_san_pham"));
                nk.setSoLuong(rs.getInt("so_luong"));
                nk.setGiaNhap(rs.getDouble("gia_nhap"));
                nk.setNgayNhap(rs.getString("ngay_nhap"));
                nk.setNhaCungCap(rs.getString("nha_cung_cap"));
                list.add(nk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insertNhapKho(Nhapkho nk) {
        String sql = "INSERT INTO nhapkho (ten_san_pham, so_luong, gia_nhap, ngay_nhap, nha_cung_cap) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nk.getTenSanPham());
            stmt.setInt(2, nk.getSoLuong());
            stmt.setDouble(3, nk.getGiaNhap());
            stmt.setString(4, nk.getNgayNhap());
            stmt.setString(5, nk.getNhaCungCap());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateNhapkho(Nhapkho nk) {
        String sql = "UPDATE nhapkho SET ten_san_pham=?, so_luong=?, gia_nhap=?, ngay_nhap=?, nha_cung_cap=? WHERE id=?";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nk.getTenSanPham());
            stmt.setInt(2, nk.getSoLuong());
            stmt.setDouble(3, nk.getGiaNhap());
            stmt.setString(4, nk.getNgayNhap());
            stmt.setString(5, nk.getNhaCungCap());
            stmt.setInt(6, nk.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteNhapKho(int id) {
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM nhapkho WHERE id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Nhapkho> searchNhapKho(String keyword) {
        ArrayList<Nhapkho> list = new ArrayList<>();
        String sql = "SELECT * FROM nhapkho WHERE ten_san_pham LIKE ?";
        try (Connection conn = QLBH.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Nhapkho nk = new Nhapkho();
                nk.setId(rs.getInt("id"));
                nk.setTenSanPham(rs.getString("ten_san_pham"));
                nk.setSoLuong(rs.getInt("so_luong"));
                nk.setGiaNhap(rs.getDouble("gia_nhap"));
                nk.setNgayNhap(rs.getString("ngay_nhap"));
                nk.setNhaCungCap(rs.getString("nha_cung_cap"));
                list.add(nk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}


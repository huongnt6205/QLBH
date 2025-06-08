package qlbh;

import java.sql.*;
import java.util.ArrayList;

public class DoanhThuSP {
    public static ArrayList<DoanhThu> getAllDoanhThu() {
        ArrayList<DoanhThu> list = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = """
                SELECT madon, tensanpham, ngayban, soluong, giatien, tongtien, ghichu
                FROM doanhthu
                ORDER BY ngayban DESC
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DoanhThu dt = new DoanhThu(
                    rs.getString("madon"),
                    rs.getString("tensanpham"),
                    rs.getDate("ngayban"),
                    rs.getInt("soluong"),        
                    rs.getInt("giatien"),        
                    rs.getInt("tongtien"),
                    rs.getString("ghichu")
                );
                list.add(dt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int getTongTienAll() {
        int tongTien = 0;
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT SUM(tongtien) AS tong FROM doanhthu";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tongTien = rs.getInt("tong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tongTien;
    }

    public static DoanhThu getDoanhThuByMaDon(String maDon) {
        try (Connection conn = Database.getConnection()) {
            String sql = """
                SELECT madon, tensanpham, ngayban, soluong, giatien, tongtien, ghichu
                FROM doanhthu
                WHERE madon = ?
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DoanhThu(
                    rs.getString("madon"),
                    rs.getString("tensanpham"),
                    rs.getDate("ngayban"),
                    rs.getInt("soluong"),
                    rs.getInt("giatien"),
                    rs.getInt("tongtien"),
                    rs.getString("ghichu")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertDoanhThu(DoanhThu dt) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO doanhthu(madon, tensanpham, ngayban, soluong, giatien, tongtien, ghichu) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dt.getMaDon());
            ps.setString(2, dt.getTenSanPham());
            ps.setDate(3, new java.sql.Date(dt.getNgayBan().getTime()));
            ps.setInt(4, dt.getSoLuong());
            ps.setInt(5, dt.getGiaTien());
            ps.setInt(6, dt.getTongTien());
            ps.setString(7, dt.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateDoanhThu(DoanhThu dt) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE doanhthu SET tensanpham = ?, ngayban = ?, soluong = ?, giatien = ?, tongtien = ?, ghichu = ? WHERE madon = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dt.getTenSanPham());
            ps.setDate(2, new java.sql.Date(dt.getNgayBan().getTime()));
            ps.setInt(3, dt.getSoLuong());
            ps.setInt(4, dt.getGiaTien());
            ps.setInt(5, dt.getTongTien());
            ps.setString(6, dt.getGhiChu());
            ps.setString(7, dt.getMaDon());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteDoanhThu(String maDon) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM doanhthu WHERE madon = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDon);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

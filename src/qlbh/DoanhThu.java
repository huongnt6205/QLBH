package qlbh;

import java.util.Date;

public class DoanhThu {
    private String maDon;
    private String tenSanPham;
    private Date ngayBan;
    private int soLuong;     
    private int giaTien;      
    private int tongTien;
    private String ghiChu;

    public DoanhThu(String maDon, String tenSanPham, Date ngayBan, int soLuong, int giaTien, int tongTien, String ghiChu) {
        this.maDon = maDon;
        this.tenSanPham = tenSanPham;
        this.ngayBan = ngayBan;
        this.soLuong = soLuong;
        this.giaTien = giaTien;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
    }
    DoanhThu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   // Getter và Setter mới thêm
    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getGiaTien() {
        return giaTien;
    }
    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }

    // Các getter/setter cũ giữ nguyên
    public String getMaDon() { return maDon; }
    public void setMaDon(String maDon) { this.maDon = maDon; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public Date getNgayBan() { return ngayBan; }
    public void setNgayBan(Date ngayBan) { this.ngayBan = ngayBan; }

    public int getTongTien() { return tongTien; }
    public void setTongTien(int tongTien) { this.tongTien = tongTien; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
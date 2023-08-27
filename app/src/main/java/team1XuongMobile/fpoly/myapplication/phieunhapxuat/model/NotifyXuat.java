package team1XuongMobile.fpoly.myapplication.phieunhapxuat.model;

public class NotifyXuat {
    String id_phieu_xuat;
    String ten_nhan_vien;

    String tenSp;
    String ten_kh;
    String ngay_xuat;
    String hinhthuc;
    String ten_don_vi_van_chuyen;
    String so_luong;
    String tong_tien;
    String tong_tien_hang;

    public NotifyXuat() {
    }

    public NotifyXuat(String id_phieu_xuat, String ten_nhan_vien, String tenSp, String ten_kh, String ngay_xuat, String hinhthuc, String ten_don_vi_van_chuyen, String so_luong, String tong_tien, String tong_tien_hang) {
        this.id_phieu_xuat = id_phieu_xuat;
        this.ten_nhan_vien = ten_nhan_vien;
        this.tenSp = tenSp;
        this.ten_kh = ten_kh;
        this.ngay_xuat = ngay_xuat;
        this.hinhthuc = hinhthuc;
        this.ten_don_vi_van_chuyen = ten_don_vi_van_chuyen;
        this.so_luong = so_luong;
        this.tong_tien = tong_tien;
        this.tong_tien_hang = tong_tien_hang;
    }

    public String getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(String so_luong) {
        this.so_luong = so_luong;
    }

    public String getTong_tien() {
        return tong_tien;
    }

    public void setTong_tien(String tong_tien) {
        this.tong_tien = tong_tien;
    }

    public String getTong_tien_hang() {
        return tong_tien_hang;
    }

    public void setTong_tien_hang(String tong_tien_hang) {
        this.tong_tien_hang = tong_tien_hang;
    }

    public String getId_phieu_xuat() {
        return id_phieu_xuat;
    }

    public void setId_phieu_xuat(String id_phieu_xuat) {
        this.id_phieu_xuat = id_phieu_xuat;
    }

    public String getTen_nhan_vien() {
        return ten_nhan_vien;
    }

    public void setTen_nhan_vien(String ten_nhan_vien) {
        this.ten_nhan_vien = ten_nhan_vien;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public String getTen_kh() {
        return ten_kh;
    }

    public void setTen_kh(String ten_kh) {
        this.ten_kh = ten_kh;
    }

    public String getNgay_xuat() {
        return ngay_xuat;
    }

    public void setNgay_xuat(String ngay_xuat) {
        this.ngay_xuat = ngay_xuat;
    }

    public String getHinhthuc() {
        return hinhthuc;
    }

    public void setHinhthuc(String hinhthuc) {
        this.hinhthuc = hinhthuc;
    }

    public String getTen_don_vi_van_chuyen() {
        return ten_don_vi_van_chuyen;
    }

    public void setTen_don_vi_van_chuyen(String ten_don_vi_van_chuyen) {
        this.ten_don_vi_van_chuyen = ten_don_vi_van_chuyen;
    }
}

package team1XuongMobile.fpoly.myapplication.models;

public class PhieuXuat {
    private String id_phieu_xuat;
    private String ngay_xuat;
    private String id_kh;
    private String ten_kh;
    private String id_don_vi_vc;
    private String ten_don_vi_van_chuyen;
    private String tenSp;
    private String idSanPham;
    private String so_luong;
    private String tong_tien;
    private String ghi_chu;
    private String tong_tien_hang;
    private long timestamp;
    private String uid;
    private String kh;
    private String thue_xuat;
    private boolean trangThai;

    public PhieuXuat() {
    }

    public PhieuXuat(String id_phieu_xuat, String ngay_xuat, String id_kh, String ten_kh, String id_don_vi_vc, String ten_don_vi_van_chuyen, String tenSp, String idSanPham, String so_luong, String tong_tien, String ghi_chu, String tong_tien_hang, long timestamp, String uid, String kh, String thue_xuat, boolean trangThai) {
        this.id_phieu_xuat = id_phieu_xuat;
        this.ngay_xuat = ngay_xuat;
        this.id_kh = id_kh;
        this.ten_kh = ten_kh;
        this.id_don_vi_vc = id_don_vi_vc;
        this.ten_don_vi_van_chuyen = ten_don_vi_van_chuyen;
        this.tenSp = tenSp;
        this.idSanPham = idSanPham;
        this.so_luong = so_luong;
        this.tong_tien = tong_tien;
        this.ghi_chu = ghi_chu;
        this.tong_tien_hang = tong_tien_hang;
        this.timestamp = timestamp;
        this.uid = uid;
        this.kh = kh;
        this.thue_xuat = thue_xuat;
        this.trangThai = trangThai;
    }

    public String getId_phieu_xuat() {
        return id_phieu_xuat;
    }

    public void setId_phieu_xuat(String id_phieu_xuat) {
        this.id_phieu_xuat = id_phieu_xuat;
    }

    public String getNgay_xuat() {
        return ngay_xuat;
    }

    public void setNgay_xuat(String ngay_xuat) {
        this.ngay_xuat = ngay_xuat;
    }

    public String getId_kh() {
        return id_kh;
    }

    public void setId_kh(String id_kh) {
        this.id_kh = id_kh;
    }

    public String getTen_kh() {
        return ten_kh;
    }

    public void setTen_kh(String ten_kh) {
        this.ten_kh = ten_kh;
    }

    public String getId_don_vi_vc() {
        return id_don_vi_vc;
    }

    public void setId_don_vi_vc(String id_don_vi_vc) {
        this.id_don_vi_vc = id_don_vi_vc;
    }

    public String getTen_don_vi_van_chuyen() {
        return ten_don_vi_van_chuyen;
    }

    public void setTen_don_vi_van_chuyen(String ten_don_vi_van_chuyen) {
        this.ten_don_vi_van_chuyen = ten_don_vi_van_chuyen;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public String getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(String idSanPham) {
        this.idSanPham = idSanPham;
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

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }

    public String getTong_tien_hang() {
        return tong_tien_hang;
    }

    public void setTong_tien_hang(String tong_tien_hang) {
        this.tong_tien_hang = tong_tien_hang;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public String getThue_xuat() {
        return thue_xuat;
    }

    public void setThue_xuat(String thue_xuat) {
        this.thue_xuat = thue_xuat;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}

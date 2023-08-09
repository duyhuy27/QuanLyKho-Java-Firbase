package team1XuongMobile.fpoly.myapplication.phieunhapxuat.model;

public class PhieuNhap {
    private String idSanPham;
    private String id_nha_cc;
    private String id_phieu_nhap;
    private String ngayNhap;
    private String so_luong;
    private String tenSp;
    private String ten_nha_cc;
    private String thue;
    private long timestamp;
    private String tong_tien;
    private String tong_tien_hang;
    private boolean trangThai;
    private String uid;
    private String kh;

    private String formattedDate;



    public PhieuNhap() {
    }

    public PhieuNhap(String idSanPham, String id_nha_cc, String id_phieu_nhap, String ngayNhap, String so_luong, String tenSp, String ten_nha_cc, String thue, long timestamp, String tong_tien, String tong_tien_hang, boolean trangThai, String uid, String kh, String formattedDate) {
        this.idSanPham = idSanPham;
        this.id_nha_cc = id_nha_cc;
        this.id_phieu_nhap = id_phieu_nhap;
        this.ngayNhap = ngayNhap;
        this.so_luong = so_luong;
        this.tenSp = tenSp;
        this.ten_nha_cc = ten_nha_cc;
        this.thue = thue;
        this.timestamp = timestamp;
        this.tong_tien = tong_tien;
        this.tong_tien_hang = tong_tien_hang;
        this.trangThai = trangThai;
        this.uid = uid;
        this.kh = kh;
        this.formattedDate = formattedDate;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(String idSanPham) {
        this.idSanPham = idSanPham;
    }

    public String getId_nha_cc() {
        return id_nha_cc;
    }

    public void setId_nha_cc(String id_nha_cc) {
        this.id_nha_cc = id_nha_cc;
    }

    public String getId_phieu_nhap() {
        return id_phieu_nhap;
    }

    public void setId_phieu_nhap(String id_phieu_nhap) {
        this.id_phieu_nhap = id_phieu_nhap;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(String so_luong) {
        this.so_luong = so_luong;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public String getTen_nha_cc() {
        return ten_nha_cc;
    }

    public void setTen_nha_cc(String ten_nha_cc) {
        this.ten_nha_cc = ten_nha_cc;
    }

    public String getThue() {
        return thue;
    }

    public void setThue(String thue) {
        this.thue = thue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
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
}
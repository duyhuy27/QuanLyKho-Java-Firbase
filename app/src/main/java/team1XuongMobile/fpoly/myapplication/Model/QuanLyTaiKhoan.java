package team1XuongMobile.fpoly.myapplication.Model;

public class QuanLyTaiKhoan {
    String id_qltk;
    String tennhanvien;
    String email;
    String uid;
    String sdt;


    public QuanLyTaiKhoan() {
    }

    public QuanLyTaiKhoan(String id_qltk, String tennhanvien, String email, String uid, String sdt) {
        this.id_qltk = id_qltk;
        this.tennhanvien = tennhanvien;
        this.email = email;
        this.uid = uid;
        this.sdt = sdt;
    }

    public String getId_qltk() {
        return id_qltk;
    }

    public void setId_qltk(String id_qltk) {
        this.id_qltk = id_qltk;
    }

    public String getTennhanvien() {
        return tennhanvien;
    }

    public void setTennhanvien(String tennhanvien) {
        this.tennhanvien = tennhanvien;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

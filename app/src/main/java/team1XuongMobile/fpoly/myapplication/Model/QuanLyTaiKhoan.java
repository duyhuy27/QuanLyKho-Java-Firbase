package team1XuongMobile.fpoly.myapplication.Model;

public class QuanLyTaiKhoan {
    String id;
    String username;
    String email;
    String uid;
    String trangThai;
    String kh;
    String sdt;


    public QuanLyTaiKhoan() {
    }

    public QuanLyTaiKhoan(String id, String username, String email, String uid, String trangThai, String kh, String sdt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.trangThai = trangThai;
        this.kh = kh;
        this.sdt = sdt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

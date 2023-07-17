package team1XuongMobile.fpoly.myapplication.Model;

public class KhachHang {
    String id_kh;
    String ten_kh;
    String sdt_kh;
    String email_kh;
    String diachi_kh;
    String uid;
    String kh;
    long timestamp;

    public KhachHang() {
    }

    public KhachHang(String id_kh, String ten_kh, String sdt_kh, String email_kh, String diachi_kh, String uid, String kh, long timestamp) {
        this.id_kh = id_kh;
        this.ten_kh = ten_kh;
        this.sdt_kh = sdt_kh;
        this.email_kh = email_kh;
        this.diachi_kh = diachi_kh;
        this.uid = uid;
        this.kh = kh;
        this.timestamp = timestamp;
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

    public String getSdt_kh() {
        return sdt_kh;
    }

    public void setSdt_kh(String sdt_kh) {
        this.sdt_kh = sdt_kh;
    }

    public String getEmail_kh() {
        return email_kh;
    }

    public void setEmail_kh(String email_kh) {
        this.email_kh = email_kh;
    }

    public String getDiachi_kh() {
        return diachi_kh;
    }

    public void setDiachi_kh(String diachi_kh) {
        this.diachi_kh = diachi_kh;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

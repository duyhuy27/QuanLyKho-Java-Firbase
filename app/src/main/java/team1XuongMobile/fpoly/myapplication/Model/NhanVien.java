package team1XuongMobile.fpoly.myapplication.Model;

public class NhanVien {
    String id_nv;
    String email;
    String username;
    String sdt;
    String vai_tro;
    String trang_thai;
    String uid;
    String kh;
    long timestamp;

    public NhanVien(String id_nv, String email, String username, String sdt, String vai_tro, String trang_thai, String uid, String kh, long timestamp) {
        this.id_nv = id_nv;
        this.email = email;
        this.username = username;
        this.sdt = sdt;
        this.vai_tro = vai_tro;
        this.trang_thai = trang_thai;
        this.uid = uid;
        this.kh = kh;
        this.timestamp = timestamp;
    }

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public NhanVien() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId_nv() {
        return id_nv;
    }

    public void setId_nv(String id_nv) {
        this.id_nv = id_nv;
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

    public String getVai_tro() {
        return vai_tro;
    }

    public void setVai_tro(String vai_tro) {
        this.vai_tro = vai_tro;
    }

    public String getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(String trang_thai) {
        this.trang_thai = trang_thai;
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

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

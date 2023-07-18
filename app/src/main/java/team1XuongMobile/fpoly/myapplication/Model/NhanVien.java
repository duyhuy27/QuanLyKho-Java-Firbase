package team1XuongMobile.fpoly.myapplication.Model;

public class NhanVien {
    String avatar;
    String id;
    String email;
    String password;
    String username;
    String sdt;
    String vaiTro;
    String trang_thai;
    String uid;
    String kh;
    long timestamp;

    public NhanVien(String avatar, String id, String email, String password, String username, String sdt, String vaiTro, String trang_thai, String uid, String kh, long timestamp) {
        this.avatar = avatar;
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.sdt = sdt;
        this.vaiTro = vaiTro;
        this.trang_thai = trang_thai;
        this.uid = uid;
        this.kh = kh;
        this.timestamp = timestamp;
    }

    public NhanVien() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
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

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

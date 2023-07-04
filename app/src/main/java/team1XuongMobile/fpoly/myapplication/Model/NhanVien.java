package team1XuongMobile.fpoly.myapplication.Model;

public class NhanVien {
    String id_nv;
    String email;
    String ten;
    String sdt;
    String vai_tro;
    String trang_thai;

    public NhanVien() {
    }

    public NhanVien(String id_nv, String email, String ten, String sdt, String vai_tro, String trang_thai) {
        this.id_nv = id_nv;
        this.email = email;
        this.ten = ten;
        this.sdt = sdt;
        this.vai_tro = vai_tro;
        this.trang_thai = trang_thai;
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

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
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
}

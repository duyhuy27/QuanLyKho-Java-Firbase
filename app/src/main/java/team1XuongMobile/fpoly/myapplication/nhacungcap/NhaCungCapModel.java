package team1XuongMobile.fpoly.myapplication.nhacungcap;

public class NhaCungCapModel {
    String id_nha_cc;
    String dia_chi;
    String ten_nha_cc;
    String so_dien_dienthoai;
    String email;
    String trangThai;
    String uid;
    String kh;
    long timestamp;

    public NhaCungCapModel() {
    }

    public NhaCungCapModel(String id_nha_cc, String dia_chi, String ten_nha_cc, String so_dien_dienthoai, String email, String trangThai, String uid, String kh, long timestamp) {
        this.id_nha_cc = id_nha_cc;
        this.dia_chi = dia_chi;
        this.ten_nha_cc = ten_nha_cc;
        this.so_dien_dienthoai = so_dien_dienthoai;
        this.email = email;
        this.trangThai = trangThai;
        this.uid = uid;
        this.kh = kh;
        this.timestamp = timestamp;
    }

    public String getId_nha_cc() {
        return id_nha_cc;
    }

    public void setId_nha_cc(String id_nha_cc) {
        this.id_nha_cc = id_nha_cc;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    public String getTen_nha_cc() {
        return ten_nha_cc;
    }

    public void setTen_nha_cc(String ten_nha_cc) {
        this.ten_nha_cc = ten_nha_cc;
    }

    public String getSo_dien_dienthoai() {
        return so_dien_dienthoai;
    }

    public void setSo_dien_dienthoai(String so_dien_dienthoai) {
        this.so_dien_dienthoai = so_dien_dienthoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
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

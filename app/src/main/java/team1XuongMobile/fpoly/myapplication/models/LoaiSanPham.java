package team1XuongMobile.fpoly.myapplication.models;

public class LoaiSanPham {
    String id_loai_sp;
    String ten_loai_sp;
    boolean TrangThai;
    String uid;
    String kh;
    long timestamp;

    public LoaiSanPham() {
    }

    public LoaiSanPham(String id_loai_sp, String ten_loai_sp,String uid , boolean trangThai, String kh, long timestamp) {
        this.id_loai_sp = id_loai_sp;
        this.ten_loai_sp = ten_loai_sp;
        TrangThai = trangThai;
        this.uid = uid;
        this.kh = kh;
        this.timestamp = timestamp;
    }

    public LoaiSanPham(String id_loai_sp, String ten_loai_sp,boolean trangThai , String uid, long timestamp) {
        this.id_loai_sp = id_loai_sp;
        this.ten_loai_sp = ten_loai_sp;
        TrangThai = trangThai;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getId_loai_sp() {
        return id_loai_sp;
    }

    public void setId_loai_sp(String id_loai_sp) {
        this.id_loai_sp = id_loai_sp;
    }

    public String getTen_loai_sp() {
        return ten_loai_sp;
    }

    public void setTen_loai_sp(String ten_loai_sp) {
        this.ten_loai_sp = ten_loai_sp;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean trangThai) {
        TrangThai = trangThai;
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

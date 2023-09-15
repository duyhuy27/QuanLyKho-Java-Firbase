package team1XuongMobile.fpoly.myapplication.models;

public class ThuocTinhModels {
    private String id,uid,ten_tt,gia_tri_tt;
    private long timestamp;

    public ThuocTinhModels(String id, String uid, String ten_tt, String gia_tri_tt, long timestamp) {
        this.id = id;
        this.uid = uid;
        this.ten_tt = ten_tt;
        this.gia_tri_tt = gia_tri_tt;
        this.timestamp = timestamp;
    }

    public ThuocTinhModels() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTen_tt() {
        return ten_tt;
    }

    public void setTen_tt(String ten_tt) {
        this.ten_tt = ten_tt;
    }

    public String getGia_tri_tt() {
        return gia_tri_tt;
    }

    public void setGia_tri_tt(String gia_tri_tt) {
        this.gia_tri_tt = gia_tri_tt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

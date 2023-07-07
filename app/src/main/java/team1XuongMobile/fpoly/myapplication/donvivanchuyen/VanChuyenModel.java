package team1XuongMobile.fpoly.myapplication.donvivanchuyen;

public class VanChuyenModel {
    String id_don_vi_vc;
    String ten;
    String hotline;
    String uid;
    String dia_chi;
    long timestamp;

    public VanChuyenModel() {
    }

    public VanChuyenModel(String id_don_vi_vc, String ten, String hotline, String uid, String dia_chi, long timestamp) {
        this.id_don_vi_vc = id_don_vi_vc;
        this.ten = ten;
        this.hotline = hotline;
        this.uid = uid;
        this.dia_chi = dia_chi;
        this.timestamp = timestamp;
    }

    public String getId_don_vi_vc() {
        return id_don_vi_vc;
    }

    public void setId_don_vi_vc(String id_don_vi_vc) {
        this.id_don_vi_vc = id_don_vi_vc;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

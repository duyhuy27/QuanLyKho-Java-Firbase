package team1XuongMobile.fpoly.myapplication.phieunhapxuat.model;

public class NotifyXuat {
    String id_phieu_xuat;
    String kh;
    String id_kh;
    String tenSp;
    String ten_kh;
    String ngay_xuat;
    String hinhthuc;
    String id_don_vi_vc;

    public NotifyXuat() {
    }

    public NotifyXuat(String id_phieu_xuat, String kh, String id_kh, String tenSp, String ten_kh, String ngay_xuat, String hinhthuc, String id_don_vi_vc) {
        this.id_phieu_xuat = id_phieu_xuat;
        this.kh = kh;
        this.id_kh = id_kh;
        this.tenSp = tenSp;
        this.ten_kh = ten_kh;
        this.ngay_xuat = ngay_xuat;
        this.hinhthuc = hinhthuc;
        this.id_don_vi_vc = id_don_vi_vc;
    }

    public String getId_phieu_xuat() {
        return id_phieu_xuat;
    }

    public void setId_phieu_xuat(String id_phieu_xuat) {
        this.id_phieu_xuat = id_phieu_xuat;
    }

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public String getId_kh() {
        return id_kh;
    }

    public void setId_kh(String id_kh) {
        this.id_kh = id_kh;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public String getTen_kh() {
        return ten_kh;
    }

    public void setTen_kh(String ten_kh) {
        this.ten_kh = ten_kh;
    }

    public String getNgay_xuat() {
        return ngay_xuat;
    }

    public void setNgay_xuat(String ngay_xuat) {
        this.ngay_xuat = ngay_xuat;
    }

    public String getHinhthuc() {
        return hinhthuc;
    }

    public void setHinhthuc(String hinhthuc) {
        this.hinhthuc = hinhthuc;
    }

    public String getId_don_vi_vc() {
        return id_don_vi_vc;
    }

    public void setId_don_vi_vc(String id_don_vi_vc) {
        this.id_don_vi_vc = id_don_vi_vc;
    }
}

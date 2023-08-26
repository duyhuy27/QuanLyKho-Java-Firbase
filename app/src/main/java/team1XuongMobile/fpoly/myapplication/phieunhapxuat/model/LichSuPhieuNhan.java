package team1XuongMobile.fpoly.myapplication.phieunhapxuat.model;

public class LichSuPhieuNhan {
    String id_phieu_nhap;
    String idThongbao_phieunhap;
    String ten_nhan_vien;
    String ngay_them_sua;
    String loai_thong_bao;
    String kh;

    public LichSuPhieuNhan(String id_phieu_nhap, String idThongbao_phieunhap, String ten_nhan_vien, String ngay_them_sua, String loai_thong_bao, String kh) {
        this.id_phieu_nhap = id_phieu_nhap;
        this.idThongbao_phieunhap = idThongbao_phieunhap;
        this.ten_nhan_vien = ten_nhan_vien;
        this.ngay_them_sua = ngay_them_sua;
        this.loai_thong_bao = loai_thong_bao;
        this.kh = kh;
    }

    public LichSuPhieuNhan() {
    }

    public String getId_phieu_nhap() {
        return id_phieu_nhap;
    }

    public void setId_phieu_nhap(String id_phieu_nhap) {
        this.id_phieu_nhap = id_phieu_nhap;
    }

    public String getIdThongbao_phieunhap() {
        return idThongbao_phieunhap;
    }

    public void setIdThongbao_phieunhap(String idThongbao_phieunhap) {
        this.idThongbao_phieunhap = idThongbao_phieunhap;
    }

    public String getTen_nhan_vien() {
        return ten_nhan_vien;
    }

    public void setTen_nhan_vien(String ten_nhan_vien) {
        this.ten_nhan_vien = ten_nhan_vien;
    }

    public String getNgay_them_sua() {
        return ngay_them_sua;
    }

    public void setNgay_them_sua(String ngay_them_sua) {
        this.ngay_them_sua = ngay_them_sua;
    }

    public String getLoai_thong_bao() {
        return loai_thong_bao;
    }

    public void setLoai_thong_bao(String loai_thong_bao) {
        this.loai_thong_bao = loai_thong_bao;
    }

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }
}

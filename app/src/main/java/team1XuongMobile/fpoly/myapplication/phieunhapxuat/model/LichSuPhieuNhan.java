package team1XuongMobile.fpoly.myapplication.phieunhapxuat.model;

public class LichSuPhieuNhan {
    String id_phieu_nhap;
    String idThongbao_phieunhap;
    String tennhanvien;
    String ngaynhap;
    String kieuthongbao;
    String kh;


    public LichSuPhieuNhan() {
    }

    public LichSuPhieuNhan(String id_phieu_nhap, String idThongbao_phieunhap, String tennhanvien, String ngaynhap, String kieuthongbao, String kh) {
        this.id_phieu_nhap = id_phieu_nhap;
        this.idThongbao_phieunhap = idThongbao_phieunhap;
        this.tennhanvien = tennhanvien;
        this.ngaynhap = ngaynhap;
        this.kieuthongbao = kieuthongbao;
        this.kh = kh;
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

    public String getTennhanvien() {
        return tennhanvien;
    }

    public void setTennhanvien(String tennhanvien) {
        this.tennhanvien = tennhanvien;
    }

    public String getNgaynhap() {
        return ngaynhap;
    }

    public void setNgaynhap(String ngaynhap) {
        this.ngaynhap = ngaynhap;
    }

    public String getKieuthongbao() {
        return kieuthongbao;
    }

    public void setKieuthongbao(String kieuthongbao) {
        this.kieuthongbao = kieuthongbao;
    }

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }
}

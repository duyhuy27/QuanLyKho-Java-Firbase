package team1XuongMobile.fpoly.myapplication.phieunhapxuat.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<String> selectedNgayXuat = new MutableLiveData<>();
    private MutableLiveData<String> selectedKhachHang = new MutableLiveData<>();
    private MutableLiveData<String> selectedDonViVanChuyen = new MutableLiveData<>();
    private MutableLiveData<String> selectedIDSanPham = new MutableLiveData<>();

    public MutableLiveData<String> getSelectedNgayXuat() {
        return selectedNgayXuat;
    }

    public MutableLiveData<String> getSelectedKhachHang() {
        return selectedKhachHang;
    }

    public MutableLiveData<String> getSelectedDonViVanChuyen() {
        return selectedDonViVanChuyen;
    }

    public MutableLiveData<String> getSelectedIDSanPham() {
        return selectedIDSanPham;
    }

    public void setSelectedNgayXuat(String ngayXuat) {
        selectedNgayXuat.setValue(ngayXuat);
    }

    public void setSelectedKhachHang(String khachHang) {
        selectedKhachHang.setValue(khachHang);
    }

    public void setSelectedDonViVanChuyen(String donViVanChuyen) {
        selectedDonViVanChuyen.setValue(donViVanChuyen);
    }


    public void setSelectedIDSanPham(String idSanPham) {
        selectedIDSanPham.setValue(idSanPham);
    }
}

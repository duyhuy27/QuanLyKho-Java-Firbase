package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.DonViVanChuyenSuaPhieuXuatListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.KhachHangSuaPhieuXuatListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.SanPhamSuaPhieuXuatListener;

public class SuaPhieuXuatActivity extends AppCompatActivity implements SanPhamSuaPhieuXuatListener,
        KhachHangSuaPhieuXuatListener, DonViVanChuyenSuaPhieuXuatListener {
    protected TextView tvTenSpX, tvMaSpX, tvSoTienSpX, tv_tongSoLuongX, tv_soTienHangX, tv_thueX,
            tvKhachHangX, tvDonViVanChuyenX, tv_tamTinhX;
    protected EditText edSoLuongSpX;
    protected ImageView imgTangSlX, imgGiamSlX;
    protected AppCompatButton button_luuPhieuXuat;
    protected RelativeLayout relativeKhachHang, relativeDonViVanChuyen;
    protected LinearLayout linearTrangThaiSpX;
    private String idPhieuXuat, tenSanPham, maSanPham, soTienCuaSanPham, tongSoLuong,
            soTienHang, thue, khachHang, donViVanChuyen, tamTinh, soLuong;
    private double giaTien, thueSanPham, tamTinhSanPham, soTienHangSanPham, tienThue;
    private int soLuongSanPham, tongSoLuongSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_phieu_xuat);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idPhieuXuat = bundle.getString("idPhieuXuat");
        }
        // TextView
        tvTenSpX = findViewById(R.id.tvTenSpX);
        tvMaSpX = findViewById(R.id.tvMaSpX);
        tvSoTienSpX = findViewById(R.id.tvSoTienSpX);
        tv_tongSoLuongX = findViewById(R.id.tv_tongSoLuongX);
        tv_soTienHangX = findViewById(R.id.tv_soTienHangX);
        tv_thueX = findViewById(R.id.tv_thueX);
        tvKhachHangX = findViewById(R.id.tvKhachHangX);
        tvDonViVanChuyenX = findViewById(R.id.tvDonViVanChuyenX);
        tv_tamTinhX = findViewById(R.id.tv_tamTinhX);
        // EditText
        edSoLuongSpX = findViewById(R.id.edSoLuongSpX);
        // AppCompatButton
        button_luuPhieuXuat = findViewById(R.id.button_luuPhieuXuat);
        // ImageView
        imgTangSlX = findViewById(R.id.imgTangSlX);
        imgGiamSlX = findViewById(R.id.imgGiamSlX);
        // RelativeLayout
        relativeKhachHang = findViewById(R.id.relativeKhachHang);
        relativeDonViVanChuyen = findViewById(R.id.relativeDonViVanChuyen);
        // LinearLayout
        linearTrangThaiSpX = findViewById(R.id.linearTrangThaiSpX);

        loadFirebasePhieuXuat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imgTangSlX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTangSoLuongSanPham();
            }
        });
        imgGiamSlX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGiamSoLuongSanPham();
            }
        });
        relativeKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChonKhachHang();
            }
        });
        relativeDonViVanChuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChonDonViVanChuyen();
            }
        });
        linearTrangThaiSpX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChonSanPham();
            }
        });
        button_luuPhieuXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLuuPhieuXuat();
            }
        });
    }

    private void clickLuuPhieuXuat() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tenSp", String.valueOf(tenSanPham));
        hashMap.put("ma_san_pham", String.valueOf(maSanPham));
        hashMap.put("giaSp", String.valueOf(soTienCuaSanPham));
        hashMap.put("so_luong", String.valueOf(soLuongSanPham));
        hashMap.put("tong_tien", String.valueOf(soTienHangSanPham));
        hashMap.put("thue_xuat", String.valueOf(thueSanPham));
        hashMap.put("ten_kh", String.valueOf(khachHang));
        hashMap.put("ten_don_vi_van_chuyen", String.valueOf(donViVanChuyen));
        hashMap.put("tong_tien_hang", String.valueOf(tamTinhSanPham));
        reference.child(idPhieuXuat).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SuaPhieuXuatActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("idPhieuXuat", idPhieuXuat);
                        ChiTietHDXFragment fragment = new ChiTietHDXFragment();
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.layout_content_suaPhieuXuat, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SuaPhieuXuatActivity.this, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clickChonSanPham() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content_suaPhieuXuat, new SanPhamSuaPhieuXuatFragment())
                .addToBackStack(null)
                .commit();
    }

    private void clickChonDonViVanChuyen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content_suaPhieuXuat, new DonViVanChuyenSuaPhieuXuatFragment())
                .addToBackStack(null)
                .commit();
    }

    private void clickChonKhachHang() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content_suaPhieuXuat, new KhachHangSuaPhieuXuatFragment())
                .addToBackStack(null)
                .commit();
    }

    private void clickGiamSoLuongSanPham() {
        if (soLuongSanPham > 1) {
            soLuongSanPham--;
            edSoLuongSpX.setText(String.valueOf(soLuongSanPham));
            soTienHangSanPham = giaTien * soLuongSanPham;
            tv_soTienHangX.setText(String.valueOf(soTienHangSanPham));
            tv_tongSoLuongX.setText(String.valueOf(soLuongSanPham));
            tienThue = (soTienHangSanPham * thueSanPham) / 100;
            tamTinhSanPham = soTienHangSanPham + tienThue;
            tv_tamTinhX.setText(String.valueOf(tamTinhSanPham));
        }
    }

    private void clickTangSoLuongSanPham() {
        if (soLuongSanPham < 1000) {
            soLuongSanPham++;
            edSoLuongSpX.setText(String.valueOf(soLuongSanPham));
            soTienHangSanPham = giaTien * soLuongSanPham;
            tv_soTienHangX.setText(String.valueOf(soTienHangSanPham));
            tv_tongSoLuongX.setText(String.valueOf(soLuongSanPham));
            tienThue = (soTienHangSanPham * thueSanPham) / 100;
            tamTinhSanPham = soTienHangSanPham + tienThue;
            tv_tamTinhX.setText(String.valueOf(tamTinhSanPham));
        }
    }

    private void loadFirebasePhieuXuat() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        reference.child(idPhieuXuat).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenSanPham = String.valueOf(snapshot.child("tenSp").getValue());
                maSanPham = String.valueOf(snapshot.child("ma_san_pham").getValue());
                soTienCuaSanPham = String.valueOf(snapshot.child("giaSp").getValue());
                soLuong = String.valueOf(snapshot.child("so_luong").getValue());
                tongSoLuong = String.valueOf(snapshot.child("so_luong").getValue());
                soTienHang = String.valueOf(snapshot.child("tong_tien").getValue());
                thue = String.valueOf(snapshot.child("thue_xuat").getValue());
                khachHang = String.valueOf(snapshot.child("ten_kh").getValue());
                donViVanChuyen = String.valueOf(snapshot.child("ten_don_vi_van_chuyen").getValue());
                tamTinh = String.valueOf(snapshot.child("tong_tien_hang").getValue());

                suDungDuLieuPhieuXuat(tenSanPham, maSanPham, soTienCuaSanPham, soLuong, tongSoLuong, soTienHang, thue, khachHang, donViVanChuyen, tamTinh);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void suDungDuLieuPhieuXuat(String tenSanPham, String maSanPham, String soTienCuaSanPham, String soLuong, String tongSoLuong, String soTienHang, String thue, String khachHang, String donViVanChuyen, String tamTinh) {
        tvTenSpX.setText(tenSanPham);
        tvMaSpX.setText(maSanPham);
        giaTien = Double.parseDouble(soTienCuaSanPham);
        tvSoTienSpX.setText(String.valueOf(giaTien));
        soLuongSanPham = Integer.parseInt(soLuong);
        edSoLuongSpX.setText(String.valueOf(soLuongSanPham));
        tongSoLuongSanPham = Integer.parseInt(tongSoLuong);
        tv_tongSoLuongX.setText(String.valueOf(tongSoLuongSanPham));
        soTienHangSanPham = Double.parseDouble(soTienHang);
        tv_soTienHangX.setText(String.valueOf(soTienHangSanPham));
        thueSanPham = Double.parseDouble(thue);
        tv_thueX.setText(String.valueOf(thueSanPham));
        tamTinhSanPham = Double.parseDouble(tamTinh);
        tv_tamTinhX.setText(String.valueOf(tamTinhSanPham));
        tvKhachHangX.setText(khachHang);
        tvDonViVanChuyenX.setText(donViVanChuyen);
    }

    @Override
    public void onFragmentResultSanPham(String tenSanPhamSua, String maSanPhamSua, String soTienSanPhamSua) {
        tenSanPham = tenSanPhamSua;
        maSanPham = maSanPhamSua;
        soTienCuaSanPham = soTienSanPhamSua;
        tvTenSpX.setText(tenSanPham);
        tvMaSpX.setText(maSanPham);
        tvSoTienSpX.setText(soTienCuaSanPham);
    }

    @Override
    public void onFragmentResultKhachHang(String tenKhachHang) {
        khachHang = tenKhachHang;
        tvKhachHangX.setText(khachHang);
    }

    @Override
    public void onFragmentResultDonViVanChuyen(String tenDonViVanChuyen) {
        donViVanChuyen = tenDonViVanChuyen;
        tvDonViVanChuyenX.setText(donViVanChuyen);
    }
}
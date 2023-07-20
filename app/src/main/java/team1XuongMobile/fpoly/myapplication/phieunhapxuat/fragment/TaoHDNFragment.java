package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;

public class TaoHDNFragment extends Fragment {
    private TextView tvChonSanPham, tvChonNgayNhap, tvNhaCungCap, tvTenSpHDN, tvMaSpHDN,
            tvSoTienSpHDN, tvTongSoLuongHDN, tvSoTienHangHDN, tvThueHDN, tvTamTinh;
    private String idNCC = "", idSanPham = "", tenSpNhap = "", maSpNhap = "",
            giaNhap = "", thueNhap = "", tenNhaCungCap = "", tongSoLuong,
            tongTien, tongTienHang, ngayNhap;
    private LinearLayout linerChonSp, linearTrangThaiChonSp;
    private RelativeLayout layoutChonNgay;
    private EditText edSoLuong;
    protected ImageView imgTangSl, imgGiamSl;
    private boolean check = true, trangThai = false;
    private double giaNhapBanDau = 0, giaNhapMoi, thue = 0, tamTinh, tienThue;
    private int soLuongSp = 0;
    private AppCompatButton btnTaoDonNhap;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_h_d_n, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        tvTongSoLuongHDN = view.findViewById(R.id.tv_tongSoLuongHDN);
        tvSoTienHangHDN = view.findViewById(R.id.tv_soTienHangHDN);
        tvThueHDN = view.findViewById(R.id.tv_thueHDN);
        tvChonSanPham = view.findViewById(R.id.tv_chonSanPhamNhap);
        tvChonNgayNhap = view.findViewById(R.id.tvChonNgayNhap);
        tvNhaCungCap = view.findViewById(R.id.tvNhaCungCap);
        linerChonSp = view.findViewById(R.id.linearChonSP);
        linearTrangThaiChonSp = view.findViewById(R.id.linearChonSpThanhCong);
        tvTenSpHDN = view.findViewById(R.id.tvTenSpHDN);
        tvMaSpHDN = view.findViewById(R.id.tvMaSpHDN);
        tvSoTienSpHDN = view.findViewById(R.id.tvSoTienSpHDN);
        edSoLuong = view.findViewById(R.id.edSoLuongSp);
        imgTangSl = view.findViewById(R.id.imgTangSl);
        imgGiamSl = view.findViewById(R.id.imgGiamSl);
        tvTamTinh = view.findViewById(R.id.tv_tamTinh);
        layoutChonNgay = view.findViewById(R.id.layoutChonNgay);
        btnTaoDonNhap = view.findViewById(R.id.button_taoDonNhap);

        if (check) {
            linearTrangThaiChonSp.setVisibility(View.GONE);
            linerChonSp.setVisibility(View.VISIBLE);
        } else {
            linearTrangThaiChonSp.setVisibility(View.VISIBLE);
            linerChonSp.setVisibility(View.GONE);
        }

        edSoLuong.setText(String.valueOf(soLuongSp + 1));
        tvTongSoLuongHDN.setText(String.valueOf(soLuongSp));
        imgTangSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuongSp < 1000) {
                    soLuongSp++;
                    edSoLuong.setText(String.valueOf(soLuongSp));

                    giaNhapBanDau = Double.parseDouble(giaNhap);
                    giaNhapMoi = giaNhapBanDau * soLuongSp;
                    tvSoTienHangHDN.setText(String.valueOf(giaNhapMoi));

                    tvTongSoLuongHDN.setText(String.valueOf(soLuongSp));

                    thue = Integer.parseInt(thueNhap);
                    tienThue = (giaNhapMoi * thue) / 100;
                    tamTinh = giaNhapMoi + tienThue;
                    tvTamTinh.setText(String.valueOf(tamTinh));
                }
            }
        });
        imgGiamSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuongSp > 1) {
                    soLuongSp--;
                    edSoLuong.setText(String.valueOf(soLuongSp));
                    giaNhapBanDau = Integer.parseInt(giaNhap);
                    giaNhapMoi = giaNhapBanDau * soLuongSp;
                    tvSoTienHangHDN.setText(String.valueOf(giaNhapMoi));
                    tvTongSoLuongHDN.setText(String.valueOf(soLuongSp));
                    thue = Integer.parseInt(thueNhap);
                    tamTinh = giaNhapMoi + thue;
                    tvTamTinh.setText(String.valueOf(tamTinh));
                }
            }
        });
        tvChonSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhanDuLieuChonSanPham();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, new ChonSanPhamFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        layoutChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        tvChonNgayNhap.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        btnTaoDonNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = firebaseAuth.getCurrentUser();

                tongSoLuong = tvTongSoLuongHDN.getText().toString().trim();
                tongTienHang = tvTamTinh.getText().toString().trim();
                tongTien = tvSoTienHangHDN.getText().toString().trim();
                ngayNhap = tvChonNgayNhap.getText().toString().trim();
                long timestamp = System.currentTimeMillis();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id_phieu_nhap", String.valueOf(timestamp));
                hashMap.put("id_nha_cc", String.valueOf(idNCC));
                hashMap.put("ten_nha_cc", String.valueOf(tenNhaCungCap));
                hashMap.put("tenSp", String.valueOf(tenSpNhap));
                hashMap.put("idSanPham", String.valueOf(idSanPham));
                hashMap.put("so_luong", String.valueOf(tongSoLuong));
                hashMap.put("tong_tien", String.valueOf(tongTien));
                hashMap.put("thue", String.valueOf(thueNhap));
                hashMap.put("tong_tien_hang", String.valueOf(tongTienHang));
                hashMap.put("uid", firebaseUser.getUid());
                hashMap.put("trangThai", trangThai);
                hashMap.put("ngayNhap", String.valueOf(ngayNhap));
                hashMap.put("timestamp", timestamp);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("phieu_nhap");
                reference.child("" + timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                ChiTietHDNFragment fragment = new ChiTietHDNFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("idPhieuNhap", String.valueOf(timestamp));
                                fragment.setArguments(bundle);
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.layout_content, fragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireActivity(), "Tạo hoá đơn nhập thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        nhanDuLieuChonNCC();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        nhanDuLieuChonSanPham();
    }

    private void nhanDuLieuChonNCC() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("idNhaCungCap")) {
                idNCC = bundle.getString("idNhaCungCap");
            }
        }
        loadDataFirebaseNhaCungCap();
        check = true;
    }

    private void nhanDuLieuChonSanPham() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("idSanPham")) {
                idSanPham = bundle.getString("idSanPham");
            }
        }
        check = false;
    }

    private void loadDataFirebaseNhaCungCap() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
        reference.child(String.valueOf(idNCC)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenNhaCungCap = String.valueOf(snapshot.child("ten_nha_cc").getValue());
                tvNhaCungCap.setText(tenNhaCungCap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadDataFirebaseChonSanPham(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenSpNhap = String.valueOf(snapshot.child("tenSp").getValue());
                maSpNhap = String.valueOf(snapshot.child("maSp").getValue());
                giaNhap = String.valueOf(snapshot.child("giaNhap").getValue());
                thueNhap = String.valueOf(snapshot.child("thueDauVao").getValue());

                tvThueHDN.setText(thueNhap + "%");
                tvTenSpHDN.setText(tenSpNhap);
                tvMaSpHDN.setText(maSpNhap);
                tvSoTienSpHDN.setText(giaNhap);
                tvSoTienHangHDN.setText(giaNhap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


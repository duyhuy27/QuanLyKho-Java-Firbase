package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;

public class TaoHDNFragment extends Fragment {
    public TextView tvChonSanPham, tvChonNgayNhap, tvNhaCungCap, tvTenSpHDN, tvMaSpHDN, tvSoTienSpHDN, tvTongSoLuongHDN, tvSoTienHangHDN, tvThueHDN, tvTamTinh;
    private String kh = "", uid = "", idNCC = "", idSanPham = "", tenSpNhap = "", maSpNhap = "",
            giaNhap = "", thueNhap = "", tenNhaCungCap = "", tenNhanVienTao = "",
            tongSoLuong, tongTien, tongTienHang, ngayNhap, id_phieunhap = "";
    private LinearLayout linerChonSp, linearTrangThaiChonSp;
    private EditText edSoLuong;
    private ImageView imgTangSl, imgGiamSl;
    private boolean check;
    private final boolean trangThai = false;
    private double giaNhapBanDau = 0, giaNhapMoi, thue = 0, tamTinh, tienThue;
    private int soLuongSp = 0;
    private AppCompatButton btnTaoDonNhap;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_h_d_n, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        layDuLieuDangNhap();
        // Ánh xạ các view
        bindViews(view);
        // Các sự kiện click
        setupUI();

        nhanDuLieuChonNCC();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        xuLySuKienNhanNutBack(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        nhanDuLieuChonSanPham();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = dateFormat.format(new Date());
        tvChonNgayNhap.setText(formattedDate);
    }

    private void bindViews(View view) {
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
        btnTaoDonNhap = view.findViewById(R.id.button_taoDonNhap);
    }

    private void setupUI() {
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
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, new ChonSanPhamFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnTaoDonNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = firebaseAuth.getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("phieu_nhap");

                tongSoLuong = tvTongSoLuongHDN.getText().toString().trim();
                tongTienHang = tvTamTinh.getText().toString().trim();
                tongTien = tvSoTienHangHDN.getText().toString().trim();
                ngayNhap = tvChonNgayNhap.getText().toString().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String formattedDate = dateFormat.format(new Date());
                id_phieunhap = reference.push().getKey();

                long timestamp = System.currentTimeMillis();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id_phieu_nhap", "" + id_phieunhap);
                hashMap.put("id_nha_cc", String.valueOf(idNCC));
                hashMap.put("ten_nha_cc", String.valueOf(tenNhaCungCap));
                hashMap.put("tenSp", String.valueOf(tenSpNhap));
                hashMap.put("idSanPham", String.valueOf(idSanPham));
                hashMap.put("so_luong", String.valueOf(tongSoLuong));
                hashMap.put("tong_tien", String.valueOf(tongTien));
                hashMap.put("ten_nhan_vien", "" + tenNhanVienTao);
                hashMap.put("thue", String.valueOf(thueNhap));
                hashMap.put("tong_tien_hang", String.valueOf(tongTienHang));
                hashMap.put("uid", firebaseUser.getUid());
                hashMap.put("trangThai", trangThai);
                hashMap.put("ngayNhap", String.valueOf(ngayNhap));
                hashMap.put("formattedDate", formattedDate);
                hashMap.put("timestamp", timestamp);
                hashMap.put("ma_san_pham", String.valueOf(maSpNhap));
                hashMap.put("giaNhap", String.valueOf(giaNhap));
                hashMap.put("kh", String.valueOf(kh));

                reference.child("" + id_phieunhap).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference notifilenhap = FirebaseDatabase.getInstance().getReference("notifile_phieunhap");
                                HashMap<String, Object> hashMapnotifile = new HashMap<>();
                                hashMapnotifile.put("id_phieu_nhap", "" + id_phieunhap);
                                hashMapnotifile.put("id_thongbao_phieunhap", "" + timestamp);
                                hashMapnotifile.put("ten_nhan_vien", "" + tenNhanVienTao);
                                hashMapnotifile.put("ngay_them_sua", String.valueOf(ngayNhap));
                                hashMapnotifile.put("loai_thong_bao", "them");
                                hashMapnotifile.put("kh", String.valueOf(kh));

                                notifilenhap.child("" + timestamp).setValue(hashMapnotifile)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                ChiTietHDNFragment fragment = new ChiTietHDNFragment();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("idPhieuNhap", id_phieunhap);
                                                fragment.setArguments(bundle);
                                                requireActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.layout_content, fragment)
                                                        .addToBackStack(null)
                                                        .commit();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(requireActivity(), "Tạo thông báo đơn nhập thất bại!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireActivity(), "Tạo hoá đơn nhập thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }

    private void xuLySuKienNhanNutBack(View view) {
        // Sử lý sự kiện bấm nút back trên bàn phím
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Kiểm tra xem Fragment có là Fragment cuối cùng trong Back Stack hay không
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        // Nếu không phải Fragment cuối cùng, quay lại Fragment trước đó
                        requireActivity().getSupportFragmentManager().popBackStack();
                        return true;
                    } else {
                        // Nếu là Fragment cuối cùng, quay lại màn hình chính (MainActivity)
                        requireActivity().onBackPressed();
                    }
                }
                return false;
            }
        });
    }

    private void nhanDuLieuChonNCC() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("idNhaCungCap")) {
                idNCC = bundle.getString("idNhaCungCap");
            }
        }
        loadDataFirebaseNhaCungCap();
    }

    private void nhanDuLieuChonSanPham() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("idSanPham") && bundle.containsKey("trangThaiChonSp")) {
                idSanPham = bundle.getString("idSanPham");
                loadDataFirebaseChonSanPham(idSanPham);
                check = bundle.getBoolean("trangThaiChonSp");
                if (check) {
                    linearTrangThaiChonSp.setVisibility(View.VISIBLE);
                    linerChonSp.setVisibility(View.GONE);
                }
            }
        }


    }

    private void sauKhiNhanDuLieuSanPham(String giaNhap, String thueNhap) {
        soLuongSp = 1;
        edSoLuong.setText(String.valueOf(soLuongSp));
        tvTongSoLuongHDN.setText(String.valueOf(soLuongSp));

        giaNhapBanDau = Double.parseDouble(giaNhap);
        giaNhapMoi = giaNhapBanDau * soLuongSp;

        thue = Integer.parseInt(thueNhap);
        tamTinh = giaNhapMoi + thue;
        tvTamTinh.setText(String.valueOf(tamTinh));
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
            @SuppressLint("SetTextI18n")
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


                sauKhiNhanDuLieuSanPham(giaNhap, thueNhap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void layDuLieuDangNhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kh = String.valueOf(snapshot.child("kh").getValue());
                tenNhanVienTao = "" + snapshot.child("username").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.OnFragmentResultListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.OnFragmentResultSanPhamListener;

public class SuaPhieuNhapActivity extends AppCompatActivity implements OnFragmentResultListener, OnFragmentResultSanPhamListener {
    private TextView tvNhaCungCap, tvTenSpHDN, tvMaSpHDN, tvSoTienSpHDN, tv_tongSoLuongHDN,
            tv_soTienHangHDN, tv_thueHDN, tv_tamTinh;
    private EditText edSoLuongSp;
    private AppCompatButton button_luuPhieuNhap;
    private ImageView imgTangSl, imgGiamSl;
    private String idPhieuNhap, tenSanPham, maSanPham, soTienSanPham, tongSoLuong, soTienHang,
            thue, nhaCungCap, tamTinh, soLuong, idSanPHam;
    private int soLuongSanPham, tongSoLuongSanPham;
    private double giaTien, soTienHangSanPham, tamTinhSanPham, thueSanPham, tienThue;
    private RelativeLayout relativeNhaCC;
    private LinearLayout linearChonSpThanhCong;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String kh = "", tenNhanVienSua = "", uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_phieu_nhap);
        firebaseAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idPhieuNhap = bundle.getString("idPhieuNhap");
        }
        // TextView
        tvNhaCungCap = findViewById(R.id.tvNhaCungCap);
        tvTenSpHDN = findViewById(R.id.tvTenSpHDN);
        tvMaSpHDN = findViewById(R.id.tvMaSpHDN);
        tvSoTienSpHDN = findViewById(R.id.tvSoTienSpHDN);
        tv_tongSoLuongHDN = findViewById(R.id.tv_tongSoLuongHDN);
        tv_soTienHangHDN = findViewById(R.id.tv_soTienHangHDN);
        tv_thueHDN = findViewById(R.id.tv_thueHDN);
        tv_tamTinh = findViewById(R.id.tv_tamTinh);
        // EditText
        edSoLuongSp = findViewById(R.id.edSoLuongSp);
        // AppCompatButton
        button_luuPhieuNhap = findViewById(R.id.button_luuPhieuNhap);
        // ImageView
        imgTangSl = findViewById(R.id.imgTangSl);
        imgGiamSl = findViewById(R.id.imgGiamSl);
        // RelativeLayout
        relativeNhaCC = findViewById(R.id.relativeNhaCC);
        // LinearLayout
        linearChonSpThanhCong = findViewById(R.id.linearChonSpThanhCong);
        layDuLieuDangNhap();

        loadFirebasePhieuNhap();

        imgTangSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTangSoLuongSanPham();
            }
        });
        imgGiamSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGiamSoLuongSanPham();
            }
        });
        button_luuPhieuNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLuuPhieuNhap();
            }
        });
        relativeNhaCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChonNhaCungCap();
            }
        });
        linearChonSpThanhCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChonSanPham();
            }
        });
    }

    private void clickLuuPhieuNhap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tenSp", String.valueOf(tenSanPham));
        hashMap.put("ma_san_pham", String.valueOf(maSanPham));
        hashMap.put("giaNhap", String.valueOf(giaTien));
        hashMap.put("so_luong", String.valueOf(soLuongSanPham));
        hashMap.put("tong_tien", String.valueOf(soTienHangSanPham));
        hashMap.put("thue", String.valueOf(thueSanPham));
        hashMap.put("ten_nha_cc", String.valueOf(nhaCungCap));
        hashMap.put("tong_tien_hang", String.valueOf(tamTinhSanPham));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        reference.child(idPhieuNhap).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        long timestamp = System.currentTimeMillis();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String formattedDate = dateFormat.format(new Date());
                        DatabaseReference notifilesuanhap = FirebaseDatabase.getInstance().getReference("notifile_phieunhap");
                        HashMap<String, Object> hashMapsuanotifile = new HashMap<>();
                        hashMapsuanotifile.put("id_phieu_nhap", "" + idPhieuNhap);
                        hashMapsuanotifile.put("id_thongbao_phieunhap", "" + timestamp);
                        hashMapsuanotifile.put("ten_nhan_vien", "" + tenNhanVienSua);
                        hashMapsuanotifile.put("ngay_them_sua", formattedDate);
                        hashMapsuanotifile.put("loai_thong_bao", "Sửa");
                        hashMapsuanotifile.put("kh", String.valueOf(kh));
                        notifilesuanhap.child("" + timestamp).setValue(hashMapsuanotifile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

//
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("total_quantity");

                                        databaseReference.child(idSanPHam).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String old_quantity_product = "" + snapshot.child("total_quantity").getValue();
                                                Log.d("quantity", "onDataChange: old quantity product " + old_quantity_product);
                                                int oldQuantityInt = 0;

                                                try {
                                                    if (old_quantity_product == null) {
                                                        oldQuantityInt = 0;
                                                    } else {
                                                        oldQuantityInt = Integer.parseInt(old_quantity_product);

                                                    }


                                                } catch (Exception e) {
                                                    Log.d("Quantity", "onDataChange: can not parse quantity to int " + e.getMessage());
                                                }
                                                int total_quantity = (oldQuantityInt - tongSoLuongSanPham) + soLuongSanPham;
                                                Log.d("Quantity", "onDataChange: total quantity after parse " + total_quantity);

                                                Log.d("Quantity", "old quantity after update by user: " + tongSoLuongSanPham);
                                                Log.d("Quantity", "new quantity by user click : " + soLuongSanPham);
                                                HashMap<String, Object> hashmapQuantity = new HashMap<>();
                                                hashmapQuantity.put("id_product_quantity", "" + idSanPHam);
                                                hashmapQuantity.put("total_quantity", "" + total_quantity);
                                                Log.d("Quantity", "onDataChange: total quantity " + total_quantity);


                                                databaseReference.child(idSanPHam).setValue(hashmapQuantity)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(SuaPhieuNhapActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                                                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                                sharedPreferences.edit().putString("idPhieuNhap", idPhieuNhap).apply();
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("Quantity", "onFailure: can not up quantity of id by " + e.getMessage());
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SuaPhieuNhapActivity.this, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("chi_tiet_phieu_nhap");
        reference2.child(idPhieuNhap).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SuaPhieuNhapActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SuaPhieuNhapActivity.this, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clickChonSanPham() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content_suaPhieuNhap, new SanPhamSuaPhieuNhapFragment())
                .addToBackStack(null)
                .commit();
    }

    private void clickChonNhaCungCap() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content_suaPhieuNhap, new NhaCungCapSuaPhieuNhapFragment())
                .addToBackStack(null)
                .commit();
    }

    private void clickTangSoLuongSanPham() {
        if (soLuongSanPham < 1000) {
            soLuongSanPham++;
            edSoLuongSp.setText(String.valueOf(soLuongSanPham));
            soTienHangSanPham = giaTien * soLuongSanPham;
            tv_soTienHangHDN.setText(String.valueOf(soTienHangSanPham));
            tv_tongSoLuongHDN.setText(String.valueOf(soLuongSanPham));
            tienThue = (soTienHangSanPham * thueSanPham) / 100;
            tamTinhSanPham = soTienHangSanPham + tienThue;
            tv_tamTinh.setText(String.valueOf(tamTinhSanPham));
        }
    }

    private void clickGiamSoLuongSanPham() {
        if (soLuongSanPham > 1) {
            soLuongSanPham--;
            edSoLuongSp.setText(String.valueOf(soLuongSanPham));
            soTienHangSanPham = giaTien * soLuongSanPham;
            tv_soTienHangHDN.setText(String.valueOf(soTienHangSanPham));
            tv_tongSoLuongHDN.setText(String.valueOf(soLuongSanPham));
            tienThue = (soTienHangSanPham * thueSanPham) / 100;
            tamTinhSanPham = soTienHangSanPham + tienThue;
            tv_tamTinh.setText(String.valueOf(tamTinhSanPham));
        }
    }

    private void loadFirebasePhieuNhap() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        reference.child(String.valueOf(idPhieuNhap)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenSanPham = String.valueOf(snapshot.child("tenSp").getValue());
                maSanPham = String.valueOf(snapshot.child("ma_san_pham").getValue());
                soTienSanPham = String.valueOf(snapshot.child("giaNhap").getValue());
                soLuong = String.valueOf(snapshot.child("so_luong").getValue());
                tongSoLuong = String.valueOf(snapshot.child("so_luong").getValue());
                soTienHang = String.valueOf(snapshot.child("tong_tien").getValue());
                thue = String.valueOf(snapshot.child("thue").getValue());
                nhaCungCap = String.valueOf(snapshot.child("ten_nha_cc").getValue());
                tamTinh = String.valueOf(snapshot.child("tong_tien_hang").getValue());
                idSanPHam = String.valueOf(snapshot.child("idSanPham").getValue());

                suDungDuLieuPhieuNhap(tenSanPham, maSanPham, soTienSanPham, soLuong, tongSoLuong, soTienHang, thue, nhaCungCap, tamTinh);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void suDungDuLieuPhieuNhap(String tenSanPham, String maSanPham, String soTienSanPham, String soLuong, String tongSoLuong, String soTienHang, String thue, String nhaCungCap, String tamTinh) {
        tvTenSpHDN.setText(tenSanPham);
        tvMaSpHDN.setText(maSanPham);
        giaTien = Double.parseDouble(soTienSanPham);
        tvSoTienSpHDN.setText(String.valueOf(giaTien));
        soLuongSanPham = Integer.parseInt(soLuong);
        edSoLuongSp.setText(String.valueOf(soLuongSanPham));
        tongSoLuongSanPham = Integer.parseInt(tongSoLuong);
        tv_tongSoLuongHDN.setText(String.valueOf(tongSoLuongSanPham));
        soTienHangSanPham = Double.parseDouble(soTienHang);
        tv_soTienHangHDN.setText(String.valueOf(soTienHangSanPham));
        thueSanPham = Double.parseDouble(thue);
        tv_thueHDN.setText(String.valueOf(thueSanPham));
        tvNhaCungCap.setText(nhaCungCap);
        tamTinhSanPham = Double.parseDouble(tamTinh);
        tv_tamTinh.setText(String.valueOf(tamTinhSanPham));
    }

    @Override
    public void onFragmentResult(String tenNhaCungCap) {
        nhaCungCap = tenNhaCungCap;
        tvNhaCungCap.setText(nhaCungCap);
    }

    @Override
    public void onFragmentResultSanPham(String tenSanPhamSua, String maSanPhamSua, String soTienSanPhamSua) {
        tenSanPham = tenSanPhamSua;
        maSanPham = maSanPhamSua;
        soTienSanPham = soTienSanPhamSua;
        tvTenSpHDN.setText(tenSanPham);
        tvMaSpHDN.setText(maSanPham);
        tvSoTienSpHDN.setText(soTienSanPham);
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
                tenNhanVienSua = "" + snapshot.child("username").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
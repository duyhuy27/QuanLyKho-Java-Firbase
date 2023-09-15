package team1XuongMobile.fpoly.myapplication.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import team1XuongMobile.fpoly.myapplication.fragments.DonViVanChuyenSuaPhieuXuatFragment;
import team1XuongMobile.fpoly.myapplication.fragments.KhachHangSuaPhieuXuatFragment;
import team1XuongMobile.fpoly.myapplication.fragments.SanPhamSuaPhieuXuatFragment;
import team1XuongMobile.fpoly.myapplication.interfaced.DonViVanChuyenSuaPhieuXuatListener;
import team1XuongMobile.fpoly.myapplication.interfaced.KhachHangSuaPhieuXuatListener;
import team1XuongMobile.fpoly.myapplication.interfaced.SanPhamSuaPhieuXuatListener;

public class SuaPhieuXuatActivity extends AppCompatActivity implements SanPhamSuaPhieuXuatListener,
        KhachHangSuaPhieuXuatListener, DonViVanChuyenSuaPhieuXuatListener {
    protected TextView tvTenSpX, tvMaSpX, tvSoTienSpX, tv_tongSoLuongX, tv_soTienHangX, tv_thueX,
            tvKhachHangX, tvDonViVanChuyenX, tv_tamTinhX, tv_thieu_hang;
    protected EditText edSoLuongSpX;
    protected ImageView imgTangSlX, imgGiamSlX;
    protected AppCompatButton button_luuPhieuXuat;
    protected RelativeLayout relativeKhachHang, relativeDonViVanChuyen;
    protected LinearLayout linearTrangThaiSpX;
    private String idPhieuXuat, tenSanPham, maSanPham, soTienCuaSanPham, tongSoLuong,
            soTienHang, thue, khachHang, donViVanChuyen, tamTinh, soLuong, idSanPham;
    private double giaTien, thueSanPham, tamTinhSanPham, soTienHangSanPham, tienThue;
    private int soLuongSanPham, tongSoLuongSanPham;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String kh = "", tenNhanVienSua = "", uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_phieu_xuat);
        firebaseAuth = FirebaseAuth.getInstance();
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

        tv_thieu_hang = findViewById(R.id.tv_thieu_hang);
        // LinearLayout
        linearTrangThaiSpX = findViewById(R.id.linearTrangThaiSpX);
        layDuLieuDangNhap();
        loadFirebasePhieuXuat();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = dateFormat.format(new Date());

        HashMap<String, Object> hashMapntf_px = new HashMap<>();
        hashMapntf_px.put("tenSp", String.valueOf(tenSanPham));
        hashMapntf_px.put("ma_san_pham", String.valueOf(maSanPham));
        hashMapntf_px.put("so_luong", String.valueOf(soLuongSanPham));
        hashMapntf_px.put("tong_tien", String.valueOf(soTienHangSanPham));
        hashMapntf_px.put("ten_kh", String.valueOf(khachHang));
        hashMapntf_px.put("ten_don_vi_van_chuyen", String.valueOf(donViVanChuyen));
        hashMapntf_px.put("tong_tien_hang", String.valueOf(tamTinhSanPham));
        hashMapntf_px.put("ngay_xuat", formattedDate);
        hashMapntf_px.put("hinhthuc", "sửa");
        hashMapntf_px.put("ten_nhan_vien", tenNhanVienSua);


        reference.child(idPhieuXuat).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SuaPhieuXuatActivity.this, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
        long timestamp = System.currentTimeMillis();
        reference.child(String.valueOf(idPhieuXuat)).child("notify_xuat").child(String.valueOf(timestamp))
                .setValue(hashMapntf_px)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("total_quantity");
        databaseReference.child(idSanPham).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        soLuongInt = Integer.parseInt(soLuong);
                    }


                } catch (Exception e) {
                    Log.d("Quantity", "onDataChange: can not parse quantity to int " + e.getMessage());
                }
                int total_quantity = oldQuantityInt + old_quantity - soLuongSanPham;
                Log.d("Quantity", "onDataChange: total quantity after parse " + total_quantity);
                Log.d("Quantity", "onDataChange: old quantity when create " + old_quantity);

                Log.d("Quantity", "quantity clck by usser : " + soLuongInt);
                HashMap<String, Object> hashmapQuantity = new HashMap<>();
                hashmapQuantity.put("id_product_quantity", "" + idSanPham);
                hashmapQuantity.put("total_quantity", "" + total_quantity);
                Log.d("Quantity", "onDataChange: total quantity " + total_quantity);

                databaseReference.child(idSanPham).setValue(hashmapQuantity).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SuaPhieuXuatActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                sharedPreferences.edit().putString("idPhieuXuat", idPhieuXuat).apply();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
private int total_quantity_product_out_int;
    private void clickGiamSoLuongSanPham() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("total_quantity");

        databaseReference.child(idSanPham).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String total_quantity_product_out = "" + snapshot.child("total_quantity").getValue();
                total_quantity_product_out_int = 0;
                try {
                    if (total_quantity_product_out == null) {
                        total_quantity_product_out_int = 0;
                    } else {
                        total_quantity_product_out_int = Integer.parseInt(total_quantity_product_out);

                    }
                } catch (Exception e) {
                    Log.d("Quantity Out", "onDataChange: can not parse quantity by " + e.getMessage());
                }

                if (soLuongSanPham > 1) {
                    soLuongSanPham--;
                    edSoLuongSpX.setText(String.valueOf(soLuongSanPham));
                    soTienHangSanPham = giaTien * soLuongSanPham;
                    tv_soTienHangX.setText(String.valueOf(soTienHangSanPham));
                    tv_tongSoLuongX.setText(String.valueOf(soLuongSanPham));
                    tienThue = (soTienHangSanPham * thueSanPham) / 100;
                    tamTinhSanPham = soTienHangSanPham + tienThue;
                    tv_tamTinhX.setText(String.valueOf(tamTinhSanPham));
//        }
                    Log.d("Quantity", "onDataChange: old quantity when after update " + old_quantity);

                    if (soLuongSanPham > total_quantity_product_out_int + old_quantity) {
                        tv_thieu_hang.setVisibility(View.VISIBLE);
                    } else {
                        tv_thieu_hang.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
private int soLuongInt;
    private void clickTangSoLuongSanPham() {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("total_quantity");

        databaseReference.child(idSanPham).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String total_quantity_product_out = "" + snapshot.child("total_quantity").getValue();
                total_quantity_product_out_int = 0;

                try {
                    if (total_quantity_product_out == null) {
                        total_quantity_product_out_int = 0;

                    } else {
                        total_quantity_product_out_int = Integer.parseInt(total_quantity_product_out);

                    }
                } catch (Exception e) {
                    Log.d("Quantity Out", "onDataChange: can not parse quantity by " + e.getMessage());
                }
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

                Log.d("Quantity Out", "quantity total of product : " + total_quantity_product_out_int);
                Log.d("Quantity Out", "quantity of product click by user: " + soLuongSanPham);
                Log.d("Quantity", "onDataChange: old quantity when after update " + old_quantity);

                if (soLuongSanPham > total_quantity_product_out_int + old_quantity ) {
                    tv_thieu_hang.setVisibility(View.VISIBLE);
                } else {
                    tv_thieu_hang.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
private int old_quantity = 0;
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
                idSanPham = String.valueOf(snapshot.child("idSanPham").getValue());

                old_quantity = Integer.parseInt(soLuong);

                Log.d("Quantity", "onDataChange: quantity old " + old_quantity);

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
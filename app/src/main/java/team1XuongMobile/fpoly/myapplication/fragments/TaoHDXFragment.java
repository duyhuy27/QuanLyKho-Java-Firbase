package team1XuongMobile.fpoly.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.fragments.ChiTietHDXFragment;
import team1XuongMobile.fpoly.myapplication.fragments.ChonDonViVanChuyenFragment;
import team1XuongMobile.fpoly.myapplication.fragments.ChonKhachHangFragment;
import team1XuongMobile.fpoly.myapplication.fragments.ChonSanPhamXFragment;

public class TaoHDXFragment extends Fragment {
    private TextView tvChonSanPhamX, tvTongSoLuongX, tvSoTienHangX, tvThueX, tvNgayX, tvKhachHangX,
            tvDonViVanChuyenX, tvTamTinhX, tvTenSpX, tvMaSpX, tvSoTienSpX, tv_thieu_hang;
    private EditText edSoLuongX, edGhiChu;
    private ImageView imgTangSlX, imgGiamSlX;
    private LinearLayout linearChonSpX, linearTrangThaiSpX;
    private RelativeLayout relativeChonNgayXuat, relativeKhachHang, relativeDonViVanChuyen;
    private AppCompatButton btnTaoHoaDonX;
    private String tongTienHang, uid, kh, idSanPhamX, tenSpXuat, giaXuat, maSpXuat, thueXuat,
            ngayXuat, idKhachHang, tenKhachHang, idDonViVanChuyen, tenDonViVanChuyen, tongSoLuongX,
            soTienHangX, ngayX, khachHangX, donViVanChuyenX, ghiChuX, tenNhanVienTao;
    private boolean trangThaiChonSanPham;

    private final boolean trangThaiHoaDonXuat = false;
    private int soLuongSp = 0;
    private double giaNhapBanDau = 0, giaNhapMoi, thue = 0, tamTinh, tienThue;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_h_d_x, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        layDuLieuDangNhap();
        // Ánh xạ view
        bindViews(view);
        // Khởi tạo dối tượng mới
        initObjects();

        // Các sự kiện click
        setupUI();
        // xử lý load số lượng
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        ngayXuat = simpleDateFormat.format(new Date());
        tvNgayX.setText(ngayXuat);
        if (trangThaiChonSanPham) {
            linearChonSpX.setVisibility(View.GONE);
            linearTrangThaiSpX.setVisibility(View.VISIBLE);
        }
        tvTenSpX.setText(tenSpXuat);
        tvMaSpX.setText(maSpXuat);
        tvSoTienSpX.setText(soTienHangX);
        tvKhachHangX.setText(tenKhachHang);
        tvDonViVanChuyenX.setText(tenDonViVanChuyen);
        tvSoTienSpX.setText(giaXuat);
    }

    private void bindViews(View view) {
        // TextView
        tvChonSanPhamX = view.findViewById(R.id.tv_chonSanPhamXuat);
        tvTongSoLuongX = view.findViewById(R.id.tv_tongSoLuongX);
        tvSoTienHangX = view.findViewById(R.id.tv_soTienHangX);
        tvThueX = view.findViewById(R.id.tv_thueX);
        tvNgayX = view.findViewById(R.id.tvChonNgayX);
        tvKhachHangX = view.findViewById(R.id.tvKhachHangX);
        tvDonViVanChuyenX = view.findViewById(R.id.tvDonViVanChuyenX);
        tvTamTinhX = view.findViewById(R.id.tv_tamTinhX);
        tvTenSpX = view.findViewById(R.id.tvTenSpX);
        tvMaSpX = view.findViewById(R.id.tvMaSpX);
        tvSoTienSpX = view.findViewById(R.id.tvSoTienSpX);
        // EditText
        edSoLuongX = view.findViewById(R.id.edSoLuongSpX);
        edGhiChu = view.findViewById(R.id.edGhiChuX);
        // ImageView
        imgTangSlX = view.findViewById(R.id.imgTangSlX);
        imgGiamSlX = view.findViewById(R.id.imgGiamSlX);
        // LinearLayout
        linearChonSpX = view.findViewById(R.id.linearChonSpX);
        linearTrangThaiSpX = view.findViewById(R.id.linearTrangThaiSpX);
        // RelativeLayout
        relativeChonNgayXuat = view.findViewById(R.id.relativeChonNgayXuat);
        relativeKhachHang = view.findViewById(R.id.relativeKhachHang);
        relativeDonViVanChuyen = view.findViewById(R.id.relativeDonViVanChuyen);

        tv_thieu_hang = view.findViewById(R.id.tv_thieu_hang);
        // AppCompatButton
        btnTaoHoaDonX = view.findViewById(R.id.button_taoDonXuat);
        edSoLuongX.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        edSoLuongX.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Kiểm tra xem actionId có phải là IME_ACTION_DONE không

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cập nhật lại thông tin theo số lượng nhập

                    giaNhapBanDau = Double.parseDouble(giaXuat);
                    giaNhapMoi = giaNhapBanDau * soLuongSp;
//                    tvSoTienHangX.setText(String.valueOf(giaNhapMoi));

                    BigDecimal bd_giaNhapMoi = new BigDecimal(giaNhapMoi);
                    String bd_giaNhapMoiString = bd_giaNhapMoi.toPlainString();
                    tvSoTienHangX.setText(bd_giaNhapMoiString);

                    tvTongSoLuongX.setText(String.valueOf(soLuongSp));

                    thue = Integer.parseInt(thueXuat);
                    tienThue = (giaNhapMoi * thue) / 100;
                    tamTinh = giaNhapMoi + tienThue;

                    BigDecimal bd_tamTinh = new BigDecimal(tamTinh);
                    String bd_tamTinhString = bd_tamTinh.toPlainString();
                    tvTamTinhX.setText(String.valueOf(bd_tamTinhString));

                    // Ẩn bàn phím ảo
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    edSoLuongX.setCursorVisible(false);
                    // Trả về true để xử lý sự kiện
                    return true;
                }
                // Trả về false để không xử lý sự kiện
                return false;
            }
        });
        edSoLuongX.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edSoLuongX.setCursorVisible(true);
                return false;
            }
        });

    }

    private void initObjects() {
    }

    private void setupUI() {
        tvChonSanPhamX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ChonSanPhamXFragment());
            }
        });
        imgTangSlX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTangSoLuongSanPhamXuat();
            }
        });
        imgGiamSlX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGiamSoLuongSanPhamXuat();
            }
        });
        relativeChonNgayXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        relativeKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ChonKhachHangFragment());
            }
        });
        relativeDonViVanChuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ChonDonViVanChuyenFragment());
            }
        });
        btnTaoHoaDonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTaoHoaDonXuat();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void layDuLieuDangNhap() {
        progressDialog.show();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                kh = String.valueOf(snapshot.child("kh").getValue());
                tenNhanVienTao = "" + snapshot.child("username").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void nhanDuLieuChonSanPhamX(String idSanPhamm, boolean trangThai) {
        Log.d("TaoHDXFragment", "nhanDuLieuChonSanPhamX");
        trangThaiChonSanPham = trangThai;
        idSanPhamX = idSanPhamm;
        loadDataFirebaseChonSanPham(idSanPhamX);
        Log.d("TaoHDXFragment", "idSanPhamX " + idSanPhamX);
        Log.d("TaoHDXFragment", "trangThaiSpX " + trangThaiChonSanPham);
    }

    private void loadDataFirebaseChonSanPham(String id) {
//        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressDialog.dismiss();
                tenSpXuat = String.valueOf(snapshot.child("tenSp").getValue());
                maSpXuat = String.valueOf(snapshot.child("maSp").getValue());
                giaXuat = String.valueOf(snapshot.child("giaBan").getValue());
                thueXuat = String.valueOf(snapshot.child("thueDauRa").getValue());

                tvThueX.setText(thueXuat + "%");
                tvTenSpX.setText(tenSpXuat);
                tvMaSpX.setText(maSpXuat);
                tvSoTienSpX.setText(giaXuat);
                tvSoTienHangX.setText(giaXuat);

                progressDialog.dismiss();

                sauKhiNhanDuLieuSanPham(giaXuat, thueXuat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sauKhiNhanDuLieuSanPham(String giaXuat, String thueXuat) {
        soLuongSp = 1;
        edSoLuongX.setText(String.valueOf(soLuongSp));
        tvTongSoLuongX.setText(String.valueOf(soLuongSp));

        giaNhapBanDau = Double.parseDouble(giaXuat);
        giaNhapMoi = giaNhapBanDau * soLuongSp;

        thue = Integer.parseInt(thueXuat);
        tamTinh = giaNhapMoi + thue;
        tvTamTinhX.setText(String.valueOf(tamTinh));

        // Thêm một TextWatcher cho edSoLuongX
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không làm gì
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không làm gì
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Kiểm tra xem edtext có rỗng không
                if (!s.toString().isEmpty()) {
                    // Chuyển đổi chuỗi thành số nguyên và gán cho biến số lượng
                    soLuongSp = Integer.parseInt(s.toString());
                }
            }
        };

        // Đặt TextWatcher cho edtext
        edSoLuongX.addTextChangedListener(textWatcher);
    }

    private int total_quantity_product_out_int = 0;

    private void clickTangSoLuongSanPhamXuat() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("total_quantity");

        databaseReference.child(idSanPhamX).addListenerForSingleValueEvent(new ValueEventListener() {
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

                soLuongSp++;
                edSoLuongX.setText(String.valueOf(soLuongSp));

                giaNhapBanDau = Double.parseDouble(giaXuat);
                giaNhapMoi = giaNhapBanDau * soLuongSp;

                BigDecimal bd_giaNhapMoi = new BigDecimal(giaNhapMoi);
                String bd_giaNhapMoiString = bd_giaNhapMoi.toPlainString();
                tvSoTienHangX.setText(bd_giaNhapMoiString);

                tvTongSoLuongX.setText(String.valueOf(soLuongSp));

                thue = Integer.parseInt(thueXuat);
                tienThue = (giaNhapMoi * thue) / 100;
                tamTinh = giaNhapMoi + tienThue;

                BigDecimal bd_tamTinh = new BigDecimal(tamTinh);
                String bd_tamTinhString = bd_tamTinh.toPlainString();
                tvTamTinhX.setText(String.valueOf(bd_tamTinhString));

                Log.d("Quantity Out", "quantity total of product : " + total_quantity_product_out_int);
                Log.d("Quantity Out", "quantity of product click by user: " + soLuongSp);

                if (soLuongSp > total_quantity_product_out_int) {
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

    private void clickGiamSoLuongSanPhamXuat() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("total_quantity");

        databaseReference.child(idSanPhamX).addListenerForSingleValueEvent(new ValueEventListener() {
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
                if(soLuongSp > 1){
                    soLuongSp--;
                    edSoLuongX.setText(String.valueOf(soLuongSp));
                    giaNhapBanDau = Double.parseDouble(giaXuat);
                    giaNhapMoi = giaNhapBanDau * soLuongSp;
                }


                BigDecimal bd_giaNhapMoi = new BigDecimal(giaNhapMoi);
                String bd_giaNhapMoiString = bd_giaNhapMoi.toPlainString();
                tvSoTienHangX.setText(bd_giaNhapMoiString);

                tvTongSoLuongX.setText(String.valueOf(soLuongSp));
                thue = Integer.parseInt(thueXuat);
                tamTinh = giaNhapMoi + thue;

                BigDecimal bd_tamTinh = new BigDecimal(tamTinh);
                String bd_tamTinhString = bd_tamTinh.toPlainString();
                tvTamTinhX.setText(String.valueOf(bd_tamTinhString));

                Log.d("Quantity Out", "quantity total of product : " + total_quantity_product_out_int);
                Log.d("Quantity Out", "quantity of product click by user: " + soLuongSp);

                if (soLuongSp > total_quantity_product_out_int) {
                    tv_thieu_hang.setVisibility(View.VISIBLE);
                } else {
                    tv_thieu_hang.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        int previousQuantity = soLuongSp; // Store the previous quantity
//


        // Check if the current quantity exceeds totalSoluong and show a warning
    }


    private void clickChonNgayXuat() {
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
                ngayXuat = simpleDateFormat.format(calendar.getTime());
                tvNgayX.setText(ngayXuat);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void nhanIdKhachHang(String tenKhachHangg, String id) {
        tenKhachHang = tenKhachHangg;
        idKhachHang = id;
        Log.d("TaoHDXFragment", "idKhachHang " + idKhachHang);
    }

    private void loadFirebaseKhachHang(String idKhachHang) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("khach_hang");
        reference.child(String.valueOf(idKhachHang)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenKhachHang = String.valueOf(snapshot.child("ten_kh").getValue());

                tvKhachHangX.setText(tenKhachHang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void nhanIdDonViVanChuyen(String tenDonViVanChuyenFromVanChuyen, String id) {
        tenDonViVanChuyen = tenDonViVanChuyenFromVanChuyen;
        idDonViVanChuyen = id;
    }

    private void loadFirebaseDonViVanChuyen(String idDonViVanChuyen) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("don_vi_vc");
        reference.child(String.valueOf(idDonViVanChuyen)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenDonViVanChuyen = String.valueOf(snapshot.child("ten").getValue());

                tvDonViVanChuyenX.setText(tenDonViVanChuyen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickTaoHoaDonXuat() {
        firebaseUser = firebaseAuth.getCurrentUser();

        tongSoLuongX = tvTongSoLuongX.getText().toString().trim();
        soTienHangX = tvSoTienHangX.getText().toString().trim();
        ngayX = tvNgayX.getText().toString().trim();
        khachHangX = tvKhachHangX.getText().toString().trim();
        donViVanChuyenX = tvDonViVanChuyenX.getText().toString().trim();
        ghiChuX = edGhiChu.getText().toString().trim();
        tongTienHang = tvTamTinhX.getText().toString().trim();
        long timestamp = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = dateFormat.format(new Date());


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id_phieu_xuat", String.valueOf(timestamp));
        hashMap.put("ngay_xuat", String.valueOf(ngayX));
        hashMap.put("id_kh", String.valueOf(idKhachHang));
        hashMap.put("ten_kh", String.valueOf(khachHangX));
        hashMap.put("id_don_vi_vc", String.valueOf(idDonViVanChuyen));
        hashMap.put("ten_don_vi_van_chuyen", String.valueOf(donViVanChuyenX));
        hashMap.put("tenSp", String.valueOf(tenSpXuat));
        hashMap.put("giaSp", String.valueOf(giaXuat));
        hashMap.put("idSanPham", String.valueOf(idSanPhamX));
        hashMap.put("so_luong", String.valueOf(tongSoLuongX));
        hashMap.put("tong_tien", String.valueOf(soTienHangX));
        hashMap.put("ghi_chu", String.valueOf(ghiChuX));
        hashMap.put("tong_tien_hang", String.valueOf(tongTienHang));
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", firebaseUser.getUid());
        hashMap.put("kh", String.valueOf(kh));
        hashMap.put("formattedDate", formattedDate);
        hashMap.put("thue_xuat", String.valueOf(thueXuat));
        hashMap.put("ten_nhan_vien", "" + tenNhanVienTao);
        hashMap.put("trangThai", trangThaiHoaDonXuat);
        hashMap.put("ma_san_pham", String.valueOf(maSpXuat));

        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("id_phieu_xuat", String.valueOf(timestamp));
        hashMap1.put("so_luong", String.valueOf(tongSoLuongX));
        hashMap1.put("ngay_xuat", String.valueOf(ngayX));
        hashMap1.put("ten_kh", String.valueOf(khachHangX));
        hashMap1.put("ten_nhan_vien", "" + tenNhanVienTao);
        hashMap1.put("tong_tien", String.valueOf(soTienHangX));
        hashMap1.put("tong_tien_hang", String.valueOf(tongTienHang));
        hashMap1.put("ten_don_vi_van_chuyen", String.valueOf(donViVanChuyenX));
        hashMap1.put("tenSp", String.valueOf(tenSpXuat));
        hashMap1.put("hinhthuc", "thêm");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        reference.child(String.valueOf(timestamp)).setValue(hashMap);
        // lưu tất cả các giá trị của hashMap vào nút phiếu xuất
        reference.child(String.valueOf(timestamp)).child("notify_xuat").child(String.valueOf(timestamp))

                .setValue(hashMap1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("total_quantity");
                        databaseReference.child(idSanPhamX).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                int total_quantity = oldQuantityInt - soLuongSp;
                                Log.d("Quantity", "onDataChange: total quantity after parse " + total_quantity);

                                Log.d("Quantity", "quantity clck by usser : " + soLuongSp);
                                HashMap<String, Object> hashmapQuantity = new HashMap<>();
                                hashmapQuantity.put("id_product_quantity", "" + idSanPhamX);
                                hashmapQuantity.put("total_quantity", "" + total_quantity);
                                Log.d("Quantity", "onDataChange: total quantity " + total_quantity);

                                databaseReference.child(idSanPhamX).setValue(hashmapQuantity).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                ChiTietHDXFragment fragment = new ChiTietHDXFragment();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("idPhieuXuat", String.valueOf(timestamp));
                                                fragment.setArguments(bundle);
                                                replaceFragment(fragment);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}
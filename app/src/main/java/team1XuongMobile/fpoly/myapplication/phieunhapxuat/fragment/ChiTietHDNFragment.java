package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;

public class ChiTietHDNFragment extends Fragment {
    private ImageView imgTrangThaiChiTietHoaDonNhap, imgChonChiTiet, img_box;
    private TextView tvTrangThaiChiTietHoaDonNhap, tvSoDienThoaiCTHDN, tvDiaChiNCC_CTHDN,
            tvTenDonViCungCapCTHDN, tvTenNguoiTaoCTHDN, tv_tenSanPhamCTHDN, tvMaSpCTHDN,
            tvGiaTienCTHDN, tvTongTienSpCTHDN, tv_tienHangCTHDN, tv_thueCTHDN,
            tv_tongTienCTHDN, tvTongTienCTHDN, tvSoLuongHDN;
    private AppCompatButton btnXacNhan;
    private String idPhieuNhap, idNhaCungCap, soDienThoaiNCC, diaChiNCC, tenNhaCC, tenNguoiTao,
            idSanPham, tenSanPham, maSanPham, giaTien, tongTien, thueSanPham, imgSanPham, soLuong;
    private boolean trangThai = false;
    private RelativeLayout layoutXacNhan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("haihuy262", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_h_d_n, container, false);
        imgTrangThaiChiTietHoaDonNhap = view.findViewById(R.id.imgTrangThaiChiTietHDN);
        tvTrangThaiChiTietHoaDonNhap = view.findViewById(R.id.tvTrangThaiChiTietHDN);
        tvSoDienThoaiCTHDN = view.findViewById(R.id.tvSoDienThoaiCTHDN);
        tvDiaChiNCC_CTHDN = view.findViewById(R.id.tvDiaChiNCC_CTHDN);
        tvTenDonViCungCapCTHDN = view.findViewById(R.id.tvTenDonViCungCapCTHDN);
        tvTenNguoiTaoCTHDN = view.findViewById(R.id.tvTenNguoiTaoCTHDN);
        tv_tenSanPhamCTHDN = view.findViewById(R.id.tv_tenSanPhamCTHDN);
        tv_tienHangCTHDN = view.findViewById(R.id.tv_tienHangCTHDN);
        tv_thueCTHDN = view.findViewById(R.id.tv_thueCTHDN);
        tv_tongTienCTHDN = view.findViewById(R.id.tv_tongTienCTHDN);
        tvMaSpCTHDN = view.findViewById(R.id.tvMaSpCTHDN);
        tvGiaTienCTHDN = view.findViewById(R.id.tvGiaTienCTHDN);
        tvSoLuongHDN = view.findViewById(R.id.tvSoLuongHDN);
        tvTongTienSpCTHDN = view.findViewById(R.id.tvTongTienSpCTHDN);
        tvTongTienCTHDN = view.findViewById(R.id.tvTongTienCTHDN);
        imgChonChiTiet = view.findViewById(R.id.imgChiTiet);
        img_box = view.findViewById(R.id.img_box);
        btnXacNhan = view.findViewById(R.id.btnXacNhanHDN);
        layoutXacNhan = view.findViewById(R.id.layoutXacNhan);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUpdate(true);
                loadFirebasePhieuNhap();
                layoutXacNhan.setVisibility(View.GONE);
            }
        });
        if (getArguments() != null) {
            idPhieuNhap = getArguments().getString("idPhieuNhap");
        }else {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            idPhieuNhap = sharedPreferences.getString("idPhieuNhap", null);
        }
        loadFirebasePhieuNhap();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("haihuy262", "onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("haihuy262", "onStart");
    }

    private void loadChiTietPhieuNhap() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chi_tiet_phieu_nhap");
        reference.child(idPhieuNhap).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenNguoiTao = String.valueOf(snapshot.child("ten_nhan_vien").getValue());
                tvTenNguoiTaoCTHDN.setText(tenNguoiTao);

                soLuong = String.valueOf(snapshot.child("so_luong").getValue());
                tvSoLuongHDN.setText(soLuong);

                tongTien = String.valueOf(snapshot.child("tong_tien").getValue());
                tvTongTienSpCTHDN.setText(tongTien);
                tv_tongTienCTHDN.setText(tongTien);
                tvTongTienCTHDN.setText(tongTien);

                tenSanPham = String.valueOf(snapshot.child("tenSp").getValue());
                maSanPham = String.valueOf(snapshot.child("maSp").getValue());
                giaTien = String.valueOf(snapshot.child("giaNhap").getValue());
                thueSanPham = String.valueOf(snapshot.child("thueDauVao").getValue());
                imgSanPham = String.valueOf(snapshot.child("img").getValue());

                Glide.with(requireContext()).load(imgSanPham).into(img_box);
                tv_tenSanPhamCTHDN.setText(tenSanPham);
                tvMaSpCTHDN.setText(maSanPham);
                tvGiaTienCTHDN.setText(giaTien);
                tv_tienHangCTHDN.setText(giaTien);
                tv_thueCTHDN.setText(thueSanPham);

                tenNhaCC = String.valueOf(snapshot.child("ten_nha_cc").getValue());
                soDienThoaiNCC = String.valueOf(snapshot.child("so_dien_dienthoai").getValue());
                diaChiNCC = String.valueOf(snapshot.child("dia_chi").getValue());

                tvTenDonViCungCapCTHDN.setText(tenNhaCC);
                tvSoDienThoaiCTHDN.setText(soDienThoaiNCC);
                tvDiaChiNCC_CTHDN.setText(diaChiNCC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("haihuy262", "onResume");
        if (getArguments() != null) {
            idPhieuNhap = getArguments().getString("idPhieuNhap");
        }else {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            idPhieuNhap = sharedPreferences.getString("idPhieuNhap", null);
        }
        loadFirebasePhieuNhap();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("haihuy262", "onPause");
        luuDuLieuChiTietPhieuNhap();
    }

    private void luuDuLieuChiTietPhieuNhap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idPhieuNhap", idPhieuNhap);
        hashMap.put("trangThai", trangThai);
        hashMap.put("ten_nhan_vien", tenNguoiTao);
        hashMap.put("so_luong", soLuong);
        hashMap.put("tong_tien", tongTien);
        hashMap.put("id_nha_cc", idNhaCungCap);
        hashMap.put("idSanPham", idSanPham);
        hashMap.put("tenSp", tenSanPham);
        hashMap.put("maSp", maSanPham);
        hashMap.put("giaNhap", giaTien);
        hashMap.put("thueDauVao", thueSanPham);
        hashMap.put("img", imgSanPham);
        hashMap.put("ten_nha_cc", tenNhaCC);
        hashMap.put("so_dien_dienthoai", soDienThoaiNCC);
        hashMap.put("dia_chi", diaChiNCC);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chi_tiet_phieu_nhap");
        reference.child(String.valueOf(idPhieuNhap)).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void loadFirebasePhieuNhap() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        reference.child(String.valueOf(idPhieuNhap)).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trangThai = Boolean.parseBoolean(String.valueOf(snapshot.child("trangThai").getValue()));
                if (trangThai) {
                    tvTrangThaiChiTietHoaDonNhap.setText("Hoàn thành");
                    imgTrangThaiChiTietHoaDonNhap.setImageResource(R.drawable.baseline_radio_button_checked_24);
                    layoutXacNhan.setVisibility(View.GONE);
                } else {
                    tvTrangThaiChiTietHoaDonNhap.setText("Chưa hoàn thành");
                    imgTrangThaiChiTietHoaDonNhap.setImageResource(R.drawable.trang_thai_chua_hoan_thanh);
                }
                clickLuaChon(trangThai);

                tenNguoiTao = String.valueOf(snapshot.child("ten_nhan_vien").getValue());
                tvTenNguoiTaoCTHDN.setText(tenNguoiTao);

                soLuong = String.valueOf(snapshot.child("so_luong").getValue());
                tvSoLuongHDN.setText(soLuong);

                tongTien = String.valueOf(snapshot.child("tong_tien").getValue());
                tvTongTienSpCTHDN.setText(tongTien);
                tv_tongTienCTHDN.setText(tongTien);
                tvTongTienCTHDN.setText(tongTien);

                idNhaCungCap = String.valueOf(snapshot.child("id_nha_cc").getValue());

                layDuLieuNhaCungCap(idNhaCungCap);

                idSanPham = String.valueOf(snapshot.child("idSanPham").getValue());
                layDuLieuSanPham(idSanPham);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void layDuLieuSanPham(String idSanPham) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.child(idSanPham).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenSanPham = String.valueOf(snapshot.child("tenSp").getValue());
                maSanPham = String.valueOf(snapshot.child("maSp").getValue());
                giaTien = String.valueOf(snapshot.child("giaNhap").getValue());
                thueSanPham = String.valueOf(snapshot.child("thueDauVao").getValue());
                imgSanPham = String.valueOf(snapshot.child("img").getValue());

                Glide.with(requireContext()).load(imgSanPham).into(img_box);
                tv_tenSanPhamCTHDN.setText(tenSanPham);
                tvMaSpCTHDN.setText(maSanPham);
                tvGiaTienCTHDN.setText(giaTien);
                tv_tienHangCTHDN.setText(giaTien);
                tv_thueCTHDN.setText(thueSanPham);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void layDuLieuNhaCungCap(String idNhaCungCap) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
        reference.child(idNhaCungCap).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenNhaCC = String.valueOf(snapshot.child("ten_nha_cc").getValue());
                soDienThoaiNCC = String.valueOf(snapshot.child("so_dien_dienthoai").getValue());
                diaChiNCC = String.valueOf(snapshot.child("dia_chi").getValue());

                tvTenDonViCungCapCTHDN.setText(tenNhaCC);
                tvSoDienThoaiCTHDN.setText(soDienThoaiNCC);
                tvDiaChiNCC_CTHDN.setText(diaChiNCC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void firebaseUpdate(boolean trangThai) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("trangThai", trangThai);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        reference.child(idPhieuNhap).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(requireActivity(), "Xác nhận hoá đơn thành công!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireActivity(), "Xác nhận hoá đơn thất bại công!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clickLuaChon(boolean trangThai) {
        imgChonChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trangThai) {

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_chuc_nang, null);
                    builder.setView(view);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    AppCompatButton btnXoa = view.findViewById(R.id.button_xoa);
                    AppCompatButton btnSua = view.findViewById(R.id.button_sua);
                    btnSua.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            clickUpdate(idPhieuNhap);
                        }
                    });
                    btnXoa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            clickDelete();
                        }
                    });
                }
            }
        });
    }

    private void clickUpdate(String idPhieuNhap) {
        Intent intent = new Intent(requireActivity(), SuaPhieuNhapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idPhieuNhap", idPhieuNhap);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void clickDelete() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xoá không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reference.child(idPhieuNhap).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(requireContext(), "Xoá thành công!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireContext(), "Xoá thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Không", null);
        builder.show();
    }
}

package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChiTietHDXFragment extends Fragment {
    private AppCompatButton btnXacNhanX;
    private TextView tvTenTrangThaiX, tvTongSoTienX, tvTenNguoiTaoX, tvTenDonViVanChuyenX, tvSoDienThoaiX,
            tvDiaChiNCCX, tvSoLuongX, tv_tenSanPham, tvMaSanPhamX, tvGiaTienX, tvTongTienSanPhamX,
            tv_tienHang, tv_thue, tv_tongTien;
    private ImageView imgIconTrangThaiX, img_box, imgChiTietX;
    private String idPhieuXuat, tongTien, tenNguoiTao, tenDonViVanChuyen, idDonViVanChuyen,
            soDienThoaiDonViVanChuyen, diaChiDonViVanChuyen, soLuongSanPhamX, idSanPhamX, tenSanPhamX,
            maSanPhamX, giaTienX, thueX, tongTienBaoGomThue, img;
    private RelativeLayout relativeXacNhan;
    private boolean trangThai = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_h_d_x, container, false);
        // Ánh xạ view
        bindViews(view);
        // Khởi tạo dối tượng mới
        initObjects();
        // Các sự kiện click
        setupUI();

        nhanIdPhieuXuat();
        loadFirebasePhieuXuat();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Nhận id phiếu xuất gửi từ tạo hoá đơn xuất

    }

    private void bindViews(View view) {
        // AppCompatButton
        btnXacNhanX = view.findViewById(R.id.btnXacNhanX);
        // TextView
        tvTenTrangThaiX = view.findViewById(R.id.tvTenTrangThaiX);
        tvTongSoTienX = view.findViewById(R.id.tvTongSoTienX);
        tvTenNguoiTaoX = view.findViewById(R.id.tvTenNguoiTaoX);
        tvTenDonViVanChuyenX = view.findViewById(R.id.tvTenDonViVanChuyenX);
        tvSoDienThoaiX = view.findViewById(R.id.tvSoDienThoaiX);
        tvDiaChiNCCX = view.findViewById(R.id.tvDiaChiNCCX);
        tvSoLuongX = view.findViewById(R.id.tvSoLuongX);
        tv_tenSanPham = view.findViewById(R.id.tv_tenSanPham);
        tvMaSanPhamX = view.findViewById(R.id.tvMaSanPhamX);
        tvGiaTienX = view.findViewById(R.id.tvGiaTienX);
        tvTongTienSanPhamX = view.findViewById(R.id.tvTongTienSanPhamX);
        tv_tienHang = view.findViewById(R.id.tv_tienHang);
        tv_thue = view.findViewById(R.id.tv_thue);
        tv_tongTien = view.findViewById(R.id.tv_tongTien);
        // ImageView
        imgIconTrangThaiX = view.findViewById(R.id.imgIconTrangThaiX);
        img_box = view.findViewById(R.id.img_box);
        imgChiTietX = view.findViewById(R.id.imgChiTietX);
        // RelativeLayout
        relativeXacNhan = view.findViewById(R.id.relativeXacNhan);
    }

    private void initObjects() {

    }

    private void setupUI() {
        btnXacNhanX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickXacNhanXuatHang();
            }
        });
        imgChiTietX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChiTiet();
            }
        });
    }

    private void clickChiTiet() {
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
                clickUpdate(idPhieuXuat);
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickDelete(idPhieuXuat);
            }
        });
    }

    private void clickDelete(String idPhieuXuat) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xoá không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reference.child(idPhieuXuat).removeValue()
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

    private void clickUpdate(String idPhieuXuat) {
        Intent intent = new Intent(requireActivity(), SuaPhieuXuatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idPhieuXuat", idPhieuXuat);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void nhanIdPhieuXuat() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("idPhieuXuat")) {
                idPhieuXuat = bundle.getString("idPhieuXuat");
                Log.d("checkId", "id " + idPhieuXuat);
            }
        }
    }

    private void clickXacNhanXuatHang() {
        firebaseUpdateTrangThai();
        loadFirebasePhieuXuat();
        relativeXacNhan.setVisibility(View.GONE);
    }

    private void loadFirebasePhieuXuat() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        reference.child(String.valueOf(idPhieuXuat)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trangThai = Boolean.parseBoolean(String.valueOf(snapshot.child("trangThai").getValue()));
                if (trangThai) {
                    tvTenTrangThaiX.setText("Hoàn thành");
                    imgIconTrangThaiX.setImageResource(R.drawable.baseline_radio_button_checked_24);
                    relativeXacNhan.setVisibility(View.GONE);
                } else {
                    tvTenTrangThaiX.setText("Chưa hoàn thành");
                    imgIconTrangThaiX.setImageResource(R.drawable.trang_thai_chua_hoan_thanh);
                }
                tongTien = String.valueOf(snapshot.child("tong_tien").getValue());
                tvTongTienSanPhamX.setText(tongTien);
                tv_tienHang.setText(tongTien);

                tongTienBaoGomThue = String.valueOf(snapshot.child("tong_tien_hang").getValue());
                tvTongSoTienX.setText(tongTienBaoGomThue);
                tv_tongTien.setText(tongTienBaoGomThue);

                tenNguoiTao = String.valueOf(snapshot.child("ten_nhan_vien").getValue());
                tvTenNguoiTaoX.setText(tenNguoiTao);

                idDonViVanChuyen = String.valueOf(snapshot.child("id_don_vi_vc").getValue());
                layDuLieuDonViVanChuyen(idDonViVanChuyen);

                soLuongSanPhamX = String.valueOf(snapshot.child("so_luong").getValue());
                tvSoLuongX.setText(soLuongSanPhamX);

                idSanPhamX = String.valueOf(snapshot.child("idSanPham").getValue());
                layDuLieuSanPham(idSanPhamX);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void layDuLieuSanPham(String idSanPhamX) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.child(idSanPhamX).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenSanPhamX = String.valueOf(snapshot.child("tenSp").getValue());
                maSanPhamX = String.valueOf(snapshot.child("maSp").getValue());
                giaTienX = String.valueOf(snapshot.child("giaBan").getValue());
                thueX = String.valueOf(snapshot.child("thueDauRa").getValue());
                img = String.valueOf(snapshot.child("img").getValue());

                tv_tenSanPham.setText(tenSanPhamX);
                tvMaSanPhamX.setText(maSanPhamX);
                tvGiaTienX.setText(giaTienX);
                tv_thue.setText(thueX);
                Glide.with(requireContext()).load(img).into(img_box);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void layDuLieuDonViVanChuyen(String idDonViVanChuyen) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("don_vi_vc");
        reference.child(idDonViVanChuyen).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenDonViVanChuyen = String.valueOf(snapshot.child("ten").getValue());
                diaChiDonViVanChuyen = String.valueOf(snapshot.child("dia_chi").getValue());
                soDienThoaiDonViVanChuyen = String.valueOf(snapshot.child("hotline").getValue());

                tvTenDonViVanChuyenX.setText(tenDonViVanChuyen);
                tvSoDienThoaiX.setText(soDienThoaiDonViVanChuyen);
                tvDiaChiNCCX.setText(diaChiDonViVanChuyen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void firebaseUpdateTrangThai() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("trangThai", true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        reference.child(idPhieuXuat).updateChildren(hashMap)
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
}
package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

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
    private ImageView imgTrangThaiChiTietHoaDonNhap, imgChonChiTiet;
    private TextView tvTrangThaiChiTietHoaDonNhap;
    private AppCompatButton btnXacNhan;
    private String idPhieuNhap;
    private boolean trangThai = false;
    private RelativeLayout layoutXacNhan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_h_d_n, container, false);
        imgTrangThaiChiTietHoaDonNhap = view.findViewById(R.id.imgTrangThaiChiTietHDN);
        tvTrangThaiChiTietHoaDonNhap = view.findViewById(R.id.tvTrangThaiChiTietHDN);
        imgChonChiTiet = view.findViewById(R.id.imgChiTiet);
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
        }
        loadFirebasePhieuNhap();
        return view;
    }

    private void loadFirebasePhieuNhap() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        reference.child(String.valueOf(idPhieuNhap)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trangThai = Boolean.parseBoolean(String.valueOf(snapshot.child("trangThai").getValue()));
                if (trangThai) {
                    tvTrangThaiChiTietHoaDonNhap.setText("Hoàn thành");
                    imgTrangThaiChiTietHoaDonNhap.setImageResource(R.drawable.baseline_radio_button_checked_24);
                } else {
                    tvTrangThaiChiTietHoaDonNhap.setText("Chưa hoàn thành");
                    imgTrangThaiChiTietHoaDonNhap.setImageResource(R.drawable.trang_thai_chua_hoan_thanh);
                }
                clickLuaChon(trangThai);
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
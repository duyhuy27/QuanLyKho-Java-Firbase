package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ChiTietHDNFragment extends Fragment {
    private ImageView imgTrangThaiChiTietHoaDonNhap;
    private TextView tvTrangThaiChiTietHoaDonNhap;
    private AppCompatButton btnXacNhan;
    private String idPhieuNhap;
    private boolean trangThai = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_h_d_n, container, false);
        imgTrangThaiChiTietHoaDonNhap = view.findViewById(R.id.imgTrangThaiChiTietHDN);
        tvTrangThaiChiTietHoaDonNhap = view.findViewById(R.id.tvTrangThaiChiTietHDN);
        btnXacNhan = view.findViewById(R.id.btnXacNhanHDN);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUpdate(true);
                loadFirebasePhieuNhap();
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
}
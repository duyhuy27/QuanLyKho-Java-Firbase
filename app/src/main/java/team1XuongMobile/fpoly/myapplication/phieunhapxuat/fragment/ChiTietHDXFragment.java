package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ChiTietHDXFragment extends Fragment {
    private AppCompatButton btnXacNhanX;
    private TextView tvTenTrangThaiX;
    private ImageView imgIconTrangThaiX;
    private String idPhieuXuat;
    private RelativeLayout relativeXacNhan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_h_d_x, container, false);
        // Ánh xạ view
        bindViews(view);
        // Khởi tạo dối tượng mới
        initObjects();
        // Các sự kiện click
        setupUI();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Nhận id phiếu xuất gửi từ tạo hoá đơn xuất
        nhanIdPhieuXuat();
    }

    private void bindViews(View view) {
        // AppCompatButton
        btnXacNhanX = view.findViewById(R.id.btnXacNhanX);
        // TextView
        tvTenTrangThaiX = view.findViewById(R.id.tvTenTrangThaiX);
        // ImageView
        imgIconTrangThaiX = view.findViewById(R.id.imgIconTrangThaiX);
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
    }

    private void nhanIdPhieuXuat() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("idPhieuXuat")) {
                idPhieuXuat = bundle.getString("idPhieuXuat");
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
                boolean trangThai = Boolean.parseBoolean(String.valueOf(snapshot.child("trangThai").getValue()));
                if (trangThai) {
                    tvTenTrangThaiX.setText("Hoàn thành");
                    imgIconTrangThaiX.setImageResource(R.drawable.baseline_radio_button_checked_24);
                } else {
                    tvTenTrangThaiX.setText("Chưa hoàn thành");
                    imgIconTrangThaiX.setImageResource(R.drawable.trang_thai_chua_hoan_thanh);
                }
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
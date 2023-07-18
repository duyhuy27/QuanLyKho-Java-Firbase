package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonNCC;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;

public class TaoHDNFragment extends Fragment {
    public TextView chonSanPham, chonNgayNhap, nhaCungCap, tvTenSpHDN, tvMaSpHDN, tvSoTienSpHDN;
    private String idNCC, idSanPham, tenNCC, tenSpHDN, maSpHDN, soTienHDN;
    private LinearLayout linerChonSp, linearTrangThaiChonSp;
    private EditText edSoLuong;
    private int soLuongSp = 1;
    private ImageView imgTangSl, imgGiamSl;
    private final ChonNCC selectedNhaCungCap = new ChonNCC();
    private final ChonSanPham selectedSanPham = new ChonSanPham();
    boolean check = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_h_d_n, container, false);

        chonSanPham = view.findViewById(R.id.tv_chonSanPhamNhap);
        chonNgayNhap = view.findViewById(R.id.tvChonNgayNhap);
        nhaCungCap = view.findViewById(R.id.tvNhaCungCap);
        linerChonSp = view.findViewById(R.id.linearChonSP);
        linearTrangThaiChonSp = view.findViewById(R.id.linearChonSpThanhCong);
        tvTenSpHDN = view.findViewById(R.id.tvTenSpHDN);
        tvMaSpHDN = view.findViewById(R.id.tvMaSpHDN);
        tvSoTienSpHDN = view.findViewById(R.id.tvSoTienSpHDN);
        edSoLuong = view.findViewById(R.id.edSoLuongSp);
        imgTangSl = view.findViewById(R.id.imgTangSl);
        imgGiamSl = view.findViewById(R.id.imgGiamSl);

        if (check) {
            linearTrangThaiChonSp.setVisibility(View.GONE);
        } else {
            linearTrangThaiChonSp.setVisibility(View.VISIBLE);
            linerChonSp.setVisibility(View.GONE);
        }

        edSoLuong.setText(String.valueOf(soLuongSp));
        imgTangSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuongSp < 1000) {
                    soLuongSp++;
                    edSoLuong.setText(String.valueOf(soLuongSp));
                }
            }
        });
        imgGiamSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuongSp > 0) {
                    soLuongSp--;
                    edSoLuong.setText(String.valueOf(soLuongSp));
                }
            }
        });
        chonSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, new ChonSanPhamFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        chonNgayNhap.setOnClickListener(new View.OnClickListener() {
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
                        chonNgayNhap.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        nhanDuLieuChonNCC();
        nhanDuLieuChonSanPham();
        return view;
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
        loadDataFirebaseChonSanPham();
        check = false;
    }

    public void loadDataFirebaseNhaCungCap() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
        reference.child(String.valueOf(idNCC)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nhaCungCap.setText(String.valueOf(snapshot.child("ten_nha_cc").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDataFirebaseChonSanPham() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.child(String.valueOf(idSanPham)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTenSpHDN.setText(String.valueOf(snapshot.child("tenSp").getValue()));
                tvMaSpHDN.setText(String.valueOf(snapshot.child("maSp").getValue()));
                tvSoTienSpHDN.setText(String.valueOf(snapshot.child("giaNhap").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

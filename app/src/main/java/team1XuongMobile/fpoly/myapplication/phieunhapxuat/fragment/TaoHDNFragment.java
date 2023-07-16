package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;


public class TaoHDNFragment extends Fragment {
    public TextView chonSanPham, chonNgayNhap, nhaCungCap, tvTenSpHDN, tvMaSpHDN, tvSoTienSpHDN;
    private String idNCC, tenNCC, tenSpHDN, maSpHDN, soTienHDN;
    private LinearLayout linerChonSp, linearTrangThaiChonSp;
    private EditText edSoLuong;
    private int soLuongSp = 1;
    private ImageView imgTangSl, imgGiamSl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_h_d_n, container, false);

        chonSanPham = view.findViewById(R.id.tv_chonSanPhamNhap);
        chonNgayNhap = view.findViewById(R.id.tvChonNgayNhap);
        nhaCungCap = view.findViewById(R.id.tvNhaCungCap);
        linerChonSp = view.findViewById(R.id.linearChonSP);
        linearTrangThaiChonSp = view.findViewById(R.id.linearChonSpThanhCong);
        tvTenSpHDN = view.findViewById(R.id.tvTenspHDN);
        tvMaSpHDN = view.findViewById(R.id.tvMaSpHDN);
        tvSoTienSpHDN = view.findViewById(R.id.tvSoTienSpHDN);
        edSoLuong = view.findViewById(R.id.edSoLuongSp);
        imgTangSl = view.findViewById(R.id.imgTangSl);
        imgGiamSl = view.findViewById(R.id.imgGiamSl);

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

        if (getArguments() != null) {
            if (getArguments().containsKey("tenSp") && getArguments().containsKey("maSp") && getArguments().containsKey("soTienSp")) {
                tvTenSpHDN.setText(getArguments().getString("tenSp"));
                tvMaSpHDN.setText(getArguments().getString("maSp"));
                tvSoTienSpHDN.setText(getArguments().getString("soTienSp"));

                linerChonSp.setVisibility(View.GONE);
                linearTrangThaiChonSp.setVisibility(View.VISIBLE);
            } else if (getArguments().containsKey("title") && getArguments().containsKey("idNCC")) {
                tenNCC = getArguments().getString("title");
                idNCC = getArguments().getString("idNCC");
                nhaCungCap.setText(tenNCC);
            }
        }


        userOnclick();
        return view;
    }

    private void userOnclick() {
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
                onclickChonNgayNhap();
            }
        });
    }

    public void onclickChonNgayNhap() {
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
}

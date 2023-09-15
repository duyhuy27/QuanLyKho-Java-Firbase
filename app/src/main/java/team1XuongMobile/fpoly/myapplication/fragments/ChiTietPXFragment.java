package team1XuongMobile.fpoly.myapplication.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.R;


public class ChiTietPXFragment extends Fragment {
    TextView tvId_px ,tvTen_sp , tvGia_sp , tvThue , tvSoluong_sp , tvDvvc , tvKhachhang , tv_nvtao , tvGhichu , tvTrangthaipx , tvtongtien , tvngayxuat;
    boolean trangthai;
    AppCompatButton btnquaylai;

    String IdpxString ="", tenspString = "", trangthaipxstring = "",soluongString ="",giaString ="",thueString="",dvvcString = "",khachhangString="",nvtaoString="",ghichuString="",
    tongtienString="",ngayxuatString="", key_idpx = "";

    public static final String KEY_ID_PHIEU_XUAT = "id_px_bd";

    public ChiTietPXFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_p_x, container, false);

        tvId_px = view.findViewById(R.id.tv_chitietpx_id_px);
        tvTen_sp = view.findViewById(R.id.tv_chitietpx_tensp);
        tvGia_sp = view.findViewById(R.id.tv_chitietpx_giasp);
        tvThue = view.findViewById(R.id.tv_chitietpx_thue);
        tvSoluong_sp = view.findViewById(R.id.tv_chitietpx_slsp);
        tvDvvc = view.findViewById(R.id.tv_chitietpx_dvvc);
        tvKhachhang = view.findViewById(R.id.tv_chitietpx_kh);
        tv_nvtao = view.findViewById(R.id.tv_chitietpx_nvtao);
        tvGhichu = view.findViewById(R.id.tv_chitietpx_ghichupx);
        tvTrangthaipx = view.findViewById(R.id.tv_chitietpx_trangthaipx);
        tvtongtien = view.findViewById(R.id.tv_chitietpx_tongtien);
        tvngayxuat = view.findViewById(R.id.tv_chitietpx_ngayxuat);
        btnquaylai = view.findViewById(R.id.btn_chitietpx_back);

        btnquaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        loadDataPXChuyenSang();
        setDataPXLenView();
        return view;
    }

    private void loadDataPXChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            key_idpx = bundle.getString(KEY_ID_PHIEU_XUAT);
            Log.e("zzzzzz", "id nhan duoc: " + key_idpx);
        }
    }

    private void setDataPXLenView() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        ref.child(key_idpx)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        IdpxString = "" + snapshot.child("id_phieu_xuat").getValue();


                        giaString =  "" + snapshot.child("giaSp").getValue();
                        tvGia_sp.setText(giaString);
                        thueString = "" + snapshot.child("thue_xuat").getValue();
                        soluongString = "" + snapshot.child("so_luong").getValue();
                        dvvcString = "" + snapshot.child("ten_don_vi_van_chuyen").getValue();
                        khachhangString = "" + snapshot.child("ten_kh").getValue();

                        String idkihieu = "" + snapshot.child("kh").getValue();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
                        ref.orderByChild("kh").equalTo(idkihieu).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Lấy ra tên của các phieu_xuat trùng kh
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        String nvtaoString = "" + child.child("username").getValue();
                                        tv_nvtao.setText(nvtaoString);

                                        // Làm gì đó với tên
                                    }
                                } else {
                                    // Không có phieu_xuat nào trùng kh
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Xử lý lỗi
                            }
                        });


                        ngayxuatString = "" + snapshot.child("ngay_xuat").getValue();
                        ghichuString = "" + snapshot.child("ghi_chu").getValue();
                        tongtienString = "" + snapshot.child("tong_tien_hang").getValue();
                        trangthai = Boolean.parseBoolean("" + snapshot.child("trangThai").getValue());
                        if(trangthai){
                            tvTrangthaipx.setTextColor(Color.GREEN);
                            tvTrangthaipx.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_radio_button_checked_24, 0);
                            tvTrangthaipx.setText("đã thanh toán");
                        }
                        else {
                            tvTrangthaipx.setTextColor(Color.RED);
                            tvTrangthaipx.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_status_red, 0);
                            tvTrangthaipx.setText("chưa thanh toán");
                        }

                        tvId_px.setText(IdpxString);
                        tvTen_sp.setText(tenspString);
                        tvGia_sp.setText(giaString);
                        tvThue.setText(thueString);
                        tvSoluong_sp.setText(soluongString);
                        tvDvvc.setText(dvvcString);
                        tvKhachhang.setText(khachhangString);
                        tvGhichu.setText(ghichuString);
                        tvtongtien.setText(tongtienString);
                        tvngayxuat.setText(ngayxuatString);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
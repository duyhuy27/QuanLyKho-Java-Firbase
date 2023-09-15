package team1XuongMobile.fpoly.myapplication.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.R;


public class ChiTietPNFragment extends Fragment {
    TextView tennv, mahoadon, ngaynhap, tensanpham, soluong, nhacungcap, giatien, trangthai;
    AppCompatButton quaylai;
    String tennvstring = "", mahoadonstring = "", ngaynhapstring = "", tensanphamstring = "", soluongstring = "",
            nhacungcapstring = "", giatienstring = "", trangthaistring = "", idPN = "";

    public static final String KEY_ID_PHIEU_NHAP = "idPN";
    FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_pn, container, false);
        tennv = view.findViewById(R.id.tv_chitietphieunhap_tennhanvien);
        mahoadon = view.findViewById(R.id.tv_chitietphieunhap_mahoadon);
        ngaynhap = view.findViewById(R.id.tv_chitietphieunhap_ngaynhap);
        tensanpham = view.findViewById(R.id.tv_chitietphieunhap_tensp);
        soluong = view.findViewById(R.id.tv_chitietphieunhap_soluong);
        nhacungcap = view.findViewById(R.id.tv_chitietphieunhap_nhacungcap);
        giatien = view.findViewById(R.id.tv_chitietphieunhap_gianhap);
        trangthai = view.findViewById(R.id.tv_chitietphieunhap_trangthaiphieunhap);
        quaylai = view.findViewById(R.id.btn_chitietphieunhap_back);
        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        loandatachuyensang();
        SetDataLenView();
        return view;
    }

    public void loandatachuyensang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idPN = bundle.getString(KEY_ID_PHIEU_NHAP);
        }
    }

    public void SetDataLenView() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        ref.child(idPN)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tennvstring = "" + snapshot.child("ten_nhan_vien").getValue();
                        mahoadonstring = "" + snapshot.child("id_phieu_nhap").getValue();
                        ngaynhapstring = "" + snapshot.child("formattedDate").getValue();
                        tensanphamstring = "" + snapshot.child("tenSp").getValue();
                        soluongstring = "" + snapshot.child("so_luong").getValue();
                        giatienstring = "" + snapshot.child("tong_tien_hang").getValue();
                        nhacungcapstring = "" + snapshot.child("ten_nha_cc").getValue();
                        trangthaistring = "" + snapshot.child("trangThai").getValue();
                        if (trangthaistring.equalsIgnoreCase("true")) {
                            trangthai.setTextColor(Color.GREEN);
                            trangthai.setText("Đã Thanh Toán");
                        } else if (trangthaistring.equalsIgnoreCase("false")) {
                            trangthai.setTextColor(Color.RED);
                            trangthai.setText("Chưa Thanh Toán");
                        }
                        tennv.setText(tennvstring);
                        mahoadon.setText(mahoadonstring);
                        ngaynhap.setText(ngaynhapstring);
                        tensanpham.setText(tensanphamstring);
                        giatien.setText(giatienstring);
                        soluong.setText(soluongstring);
                        nhacungcap.setText(nhacungcapstring);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
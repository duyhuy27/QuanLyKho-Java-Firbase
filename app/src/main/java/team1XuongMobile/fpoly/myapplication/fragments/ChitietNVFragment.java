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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import team1XuongMobile.fpoly.myapplication.R;


public class ChitietNVFragment extends Fragment {
    TextView ten, email, sdt, vaitro, trangthai, ngay_vaolam, thamnien;
    AppCompatButton quaylai;
    long thoigian_hientai;
    String tenstring = "", emailstring = "", sdtstring = "", vaitrostring = "", trangthaistring = "", ngay_vaolamstring = "", idNV = "", khstring;

    public static final String KEY_ID_NHAN_VIEN = "idNV";
    String idTK = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    public ChitietNVFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chitiet_n_v, container, false);
        ten = view.findViewById(R.id.tv_chitietpx_id_px);
        email = view.findViewById(R.id.tv_chitietpx_tensp);
        sdt = view.findViewById(R.id.giapx);
        vaitro = view.findViewById(R.id.tongtien);
        ngay_vaolam = view.findViewById(R.id.tv_chitietpx_thue);
        thamnien = view.findViewById(R.id.tv_chitietpx_slsp);
        trangthai = view.findViewById(R.id.tv_chitietpx_trangthaipx);
        quaylai = view.findViewById(R.id.btn_chitietnhanvien_back);
        firebaseAuth = FirebaseAuth.getInstance();
        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        loadDataNVChuyenSang();
        setDataNVLenView();

        return view;
    }

    private void loadDataNVChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idNV = bundle.getString(KEY_ID_NHAN_VIEN);
        }
    }


    private void setDataNVLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.child(idNV)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tenstring = "" + snapshot.child("username").getValue();
                        emailstring = "" + snapshot.child("email").getValue();
                        sdtstring = "" + snapshot.child("sdt").getValue();
                        ngay_vaolamstring = "" + snapshot.child("ngay_vaolam").getValue();
                        vaitrostring = "" + snapshot.child("vaiTro").getValue();
                        trangthaistring = "" + snapshot.child("trang_thai").getValue();
                        if (trangthaistring.equalsIgnoreCase("Đang Làm")) {
                            trangthai.setTextColor(Color.GREEN);
                            trangthai.setText(trangthaistring);
                        } else if (trangthaistring.equalsIgnoreCase("Đã Nghỉ")) {
                            trangthai.setTextColor(Color.RED);
                            trangthai.setText(trangthaistring);
                        } else if (trangthaistring.equalsIgnoreCase("Tạm Nghỉ")) {
                            trangthai.setTextColor(Color.YELLOW);
                            trangthai.setText(trangthaistring);
                        }


                        ten.setText(tenstring);
                        email.setText(emailstring);
                        sdt.setText(sdtstring);
                        ngay_vaolam.setText(ngay_vaolamstring);
                        vaitro.setText(vaitrostring);
                        thamnien.setText(String.valueOf(ThamNienNV()));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    public long ThamNienNV() {
        Calendar currentDate = Calendar.getInstance();

        // Lấy ngày hiện tại trong milliseconds
        long currentTimeMillis = currentDate.getTimeInMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        try {
            // Chuyển đổi chuỗi ngày tháng thành đối tượng Date
            Date ngay_vaolamdate = sdf.parse(ngay_vaolamstring);

            // Tạo đối tượng Calendar và thiết lập ngày tháng năm
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ngay_vaolamdate);

            // Lấy giá trị getTimeInMillis()
            thoigian_hientai = calendar.getTimeInMillis();

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Tính số ngày đến thời điểm hiện tại
        long daysDifference = ((currentTimeMillis - thoigian_hientai) / (24 * 60 * 60 * 1000)) + 1;
        return daysDifference;

    }
}
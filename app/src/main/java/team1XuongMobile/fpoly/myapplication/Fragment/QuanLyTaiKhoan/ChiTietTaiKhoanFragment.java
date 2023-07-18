package team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.R;


public class ChiTietTaiKhoanFragment extends Fragment {
    TextView ten, email, sdt, vaitro, trangthai;
    AppCompatButton quaylai;
    String tentkstring = "", emailtkstring = "", sdttkstring = "", vaitrotkstring = "", trangthaitkstring = "";
    FirebaseAuth firebaseAuth;
    String id = "";


    public static final String KEY_ID_TAI_KHOAN = "id";


    public ChiTietTaiKhoanFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_tai_khoan, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        ten = view.findViewById(R.id.tv_chitiettaikhoan_tentaikhoan);
        email = view.findViewById(R.id.tv_chitiettaikhoan_emailtaikhoan);
        sdt = view.findViewById(R.id.tv_chitiettaikhoan_sdttaikhoan);
        vaitro = view.findViewById(R.id.tv_chitiettaikhoan_vaitrotaikhoan);
        trangthai = view.findViewById(R.id.tv_chitiettaikhoan_trangthaitaikhoan);
        quaylai = view.findViewById(R.id.btn_chitiettaikhoan_back);
        loadDataTKChuyenSang();
        setDataNVLenView();
        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void loadDataTKChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString(KEY_ID_TAI_KHOAN);
            Log.d("quanquan", "id_tk " + id);

        }
    }

    private void setDataNVLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(id)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tentkstring = "" + snapshot.child("username").getValue();
                        emailtkstring = "" + snapshot.child("email").getValue();
                        sdttkstring = "" + snapshot.child("sdt").getValue();
                        vaitrotkstring = "" + snapshot.child("vaiTro").getValue();
                        trangthaitkstring = "" + snapshot.child("trangThai").getValue();

                        ten.setText(tentkstring);
                        email.setText(emailtkstring);
                        sdt.setText(sdttkstring);
                        vaitro.setText(vaitrotkstring);
                        if (trangthaitkstring.equals("Đang Hoạt Động") == true) {
                            trangthai.setTextColor(Color.GREEN);
                            trangthai.setText(trangthaitkstring);
                        } else if (trangthaitkstring.equals("Ngưng Hoạt Động") == true) {
                            trangthai.setTextColor(Color.RED);
                            trangthai.setText(trangthaitkstring);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
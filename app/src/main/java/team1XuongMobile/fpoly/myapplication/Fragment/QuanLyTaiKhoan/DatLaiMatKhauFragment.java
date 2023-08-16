package team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;


public class DatLaiMatKhauFragment extends Fragment {
    TextView ten, email;
    AppCompatButton datlaimatkhau;
    ImageButton back;
    String tentkstring = "", emailtkstring = "", trangthaitkstring = "", sdttkstring = "", idNVstring = "";
    FirebaseAuth firebaseAuth;


    private ProgressDialog progressDialog;


    String id = "";


    public static final String KEY_ID_TAI_KHOAN = "id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dat_lai_mat_khau, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        ten = view.findViewById(R.id.tv_datlaimatkhau_tentaikhoan);
        email = view.findViewById(R.id.tv_datlaimatkhau_emailtaikhoan);
        datlaimatkhau = view.findViewById(R.id.btn_datlaimatkhau_datlai);
        back = view.findViewById(R.id.imgbt_datlaimatkhau_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        loadDataTKChuyenSang();
        setDataNVLenView();
        datlaimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trangthaitkstring.equals("Ngưng Hoạt Động") == true) {
                    Toast.makeText(getContext(), "Tài Khoản Đã Ngưng Hoạt Động Không Thể Đổi Mật Khẩu Được", Toast.LENGTH_SHORT).show();
                } else {
                    DatLaiMatKhau(emailtkstring);

                }


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
                        idNVstring = "" + snapshot.child("idNV").getValue();
                        trangthaitkstring = "" + snapshot.child("trangThai").getValue();
                        ten.setText(tentkstring);
                        email.setText(emailtkstring);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void DatLaiMatKhau(String emails) {
        progressDialog.setTitle("Đang Thực Thi");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(emails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Đăng Nhập Email Để Đổi Lại Mật Khẩu", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Lỗi Không Gửi Được Xác Nhận Cho Email", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
package team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;


public class ThemTaiKhoanFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    Spinner tennhanvien, vaitro;
    EditText email, sdt;
    Button hoantat;
    ImageButton back;
    String tennhanvienstring = "", vaitrostring = "", emailstring = "", sdtstring = "";
    FirebaseUser firebaseUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_tai_khoan, container, false);
        tennhanvien = view.findViewById(R.id.spinner_themntaikhoan_tennhanvien);
        vaitro = view.findViewById(R.id.spinner_themntaikhoan_vaitro);
        email = view.findViewById(R.id.ed_themtaikhoan_emailtaikhoan);
        sdt = view.findViewById(R.id.ed_themtaikhoan_sdttaikhoan);
        hoantat = view.findViewById(R.id.btn_themtaikhoan_hoantat);
        back = view.findViewById(R.id.imgbt_themtaikhoan_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        ArrayList<String> dataListspinner = new ArrayList<String>();
        dataListspinner.add("Admin");
        dataListspinner.add("Nhân Viên Kho");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, dataListspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaitro.setAdapter(adapter);
        hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDataInput();
                firebaseAuth.createUserWithEmailAndPassword(emailstring, sdtstring)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                validateDataInput();
                                luuDuLieuQLTKLenFirebase();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Không tạo được tài khoản", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return view;

    }

    private void validateDataInput() {
        if (email.length() == 0) {
            email.requestFocus();
            email.setError("Không để trống phần email");
        } else if (sdt.length() == 0) {
            sdt.requestFocus();
            sdt.setError("Không để trống phần số điện thoại");
        } else if (!TextUtils.isDigitsOnly(sdt.getText().toString())) {
            sdt.requestFocus();
            sdt.setError("Số điện thoại phải là số");
        } else {
            luuDuLieuQLTKLenFirebase();
        }
    }

    private void luuDuLieuQLTKLenFirebase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        tennhanvienstring = (String) tennhanvien.getSelectedItem();
        emailstring = email.getText().toString();
        sdtstring = sdt.getText().toString();
        vaitrostring = (String) vaitro.getSelectedItem();
        progressDialog.setTitle("Dang luu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id_qltk", "" + timestamp);
        hashMap.put("ten", "" + tennhanvienstring);
        hashMap.put("vai_tro", "" + vaitrostring);
        hashMap.put("email", "" + emailstring);
        hashMap.put("sdt", "" + sdtstring);
        hashMap.put("uid", firebaseUser.getUid());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Thêm Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
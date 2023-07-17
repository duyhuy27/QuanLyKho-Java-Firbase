package team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.ChitietNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.SuaNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVienFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.view.fragment.XacMinhThanhCongFragment;


public class ThemTaiKhoanFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String idNV = "";
    public static final String KEY_ID_NHAN_VIEN = "idNV";

    Spinner quyentruycap;
    TextView tennhanvien, email, sdt;
    String email_da_dn, password_da_dn;

    Button hoantat;
    ImageButton back;
    String tennhanvienstring = "", quyentruycapstring = "", emailstring = "", sdtstring = "", uidstring = "";
    FirebaseUser firebaseUser;
    String uidtk;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_tai_khoan, container, false);
        tennhanvien = view.findViewById(R.id.tv_themtaikhoan_tennhanvien);
        email = view.findViewById(R.id.tv_themtaikhoan_email);
        sdt = view.findViewById(R.id.tv_themtaikhoan_sdt);
        quyentruycap = view.findViewById(R.id.spinner_themntaikhoan_quyentruycap);
        hoantat = view.findViewById(R.id.btn_themtaikhoan_hoantat);
        back = view.findViewById(R.id.imgbt_themtaikhoan_back);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        ArrayList<String> dataListspinner = new ArrayList<>();
        dataListspinner.add("admin");
        dataListspinner.add("nhanVien");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dataListspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quyentruycap.setAdapter(adapter);
        laydulieudangnhap();
        Log.d("quanjkl", "email dang nhap: "+email_da_dn+"password dang nhap" + password_da_dn);
        loadDataNVChuyenSang();
        laytaikhoantuNV();

        hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.createUserWithEmailAndPassword(emailstring, sdtstring)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    luuDuLieuQLTKLenFirebase();
                                    Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signInWithEmailAndPassword(email_da_dn, password_da_dn)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        NhanVienFragment nhanVienFragment = new NhanVienFragment();
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, nhanVienFragment).addToBackStack(null).commit();
                                                    } else {
                                                    }
                                                }
                                            });

                                } else {
                                    Toast.makeText(getContext(), "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;

    }


    private void luuDuLieuQLTKLenFirebase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        uidtk = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        quyentruycapstring = (String) quyentruycap.getSelectedItem();
        progressDialog.setTitle("Dang lưu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idTK",""+timestamp);
        hashMap.put("uid", uidtk);
        hashMap.put("username", "" + tennhanvienstring);
        hashMap.put("email", "" + emailstring);
        hashMap.put("sdt", "" + sdtstring);
        hashMap.put("timestamp", timestamp);
        hashMap.put("vaiTro", "" + quyentruycapstring);
        hashMap.put("kh", "a");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Quan_Ly_Tai_Khoan");
        ref.child(uidtk)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                    }
                });
    }

    private void loadDataNVChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idNV = bundle.getString(KEY_ID_NHAN_VIEN);
        }
    }



    public void laydulieudangnhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email_da_dn = ""+snapshot.child("email").getValue();
                password_da_dn = ""+snapshot.child("password").getValue();
                uidstring = ""+snapshot.child("uid").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void laytaikhoantuNV() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.child(idNV).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tennhanvienstring = "" + snapshot.child("username").getValue();
                emailstring = "" + snapshot.child("email").getValue();
                sdtstring = "" + snapshot.child("sdt").getValue();

                tennhanvien.setText(tennhanvienstring);
                email.setText(emailstring);
                sdt.setText(sdtstring);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
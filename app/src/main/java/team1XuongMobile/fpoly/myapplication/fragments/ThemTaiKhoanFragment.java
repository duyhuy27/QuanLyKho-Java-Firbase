package team1XuongMobile.fpoly.myapplication.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import team1XuongMobile.fpoly.myapplication.fragments.NhanVienFragment;
import team1XuongMobile.fpoly.myapplication.R;


public class ThemTaiKhoanFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String idNV = "";
    public static final String KEY_ID_NHAN_VIEN = "idNV";

    Spinner quyentruycap;
    TextView tennhanvien, email, sdt;
    String email_da_dn, password_da_dn;
    AppCompatButton hoantat;
    ImageButton back;
    String tennhanvienstring = "", quyentruycapstring = "", emailstring = "",
            sdtstring = "", uidstring = "", khstring = "", trangthaiString, idNVString = "";
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
        Log.d("quanjkl", "email dang nhap: " + email_da_dn + "password dang nhap" + password_da_dn);
        loadDataNVChuyenSang();
        laytaikhoantuNV();

        hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trangthaiString.equalsIgnoreCase("Đã Nghỉ")) {
                    Log.d("quanquan", "hoantat: " + trangthaiString);
                    Toast.makeText(getContext(), "Nhân Viên Đã Nghỉ Không Thể Bổ Nhiệm", Toast.LENGTH_SHORT).show();
                } else if (trangthaiString.equalsIgnoreCase("Tạm Nghỉ")) {
                    Toast.makeText(getContext(), "Nhân Viên Không Thể Bổ Nhiệm Khi Đang Nghỉ", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(emailstring, md5(sdtstring))
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
        hashMap.put("id", "" + timestamp);
        hashMap.put("idNV", "" + idNVString);
        hashMap.put("trangThai", "Đang Hoạt Động");
        hashMap.put("uid", uidtk);
        hashMap.put("username", "" + tennhanvienstring);
        hashMap.put("email", "" + emailstring);
        hashMap.put("password", "" + md5(sdtstring));
        hashMap.put("sdt", "" + sdtstring);
        hashMap.put("timestamp", timestamp);
        hashMap.put("vaiTro", "" + quyentruycapstring);
        hashMap.put("kh", khstring);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
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
        HashMap<String, Object> hashMapvt = new HashMap<>();
        hashMapvt.put("vaiTro", "" + quyentruycapstring);
        DatabaseReference refnv = FirebaseDatabase.getInstance().getReference("nhan_vien");
        refnv.child("" + idNV)
                .updateChildren(hashMapvt).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
                email_da_dn = "" + snapshot.child("email").getValue();
                password_da_dn = "" + snapshot.child("password").getValue();
                uidstring = "" + snapshot.child("uid").getValue();
                khstring = "" + snapshot.child("kh").getValue();

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
                trangthaiString = "" + snapshot.child("trang_thai").getValue();
                idNVString = "" + snapshot.child("id").getValue();
                Log.d("quanquan", "vaitro string" + trangthaiString);

                tennhanvien.setText(tennhanvienstring);
                email.setText(emailstring);
                sdt.setText(sdtstring);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
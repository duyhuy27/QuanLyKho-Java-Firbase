package team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import team1XuongMobile.fpoly.myapplication.R;


public class ThemTaiKhoanFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String idNV = "";
    public static final String KEY_ID_NHAN_VIEN = "idNV";
    Spinner quyentruycap;
    TextView tennhanvien, email, sdt;

    Button hoantat;
    ImageButton back;
    String tennhanvienstring = "", quyentruycapstring = "", emailstring = "", sdtstring = "";
    FirebaseUser firebaseUser;


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
        loadDataNVChuyenSang();
        laytaikhoantuNV();
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
        dataListspinner.add("admin");
        dataListspinner.add("nhanVien");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, dataListspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quyentruycap.setAdapter(adapter);
        hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.createUserWithEmailAndPassword(emailstring, sdtstring)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                luuDuLieuQLTKLenFirebase();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("quanquan", "onFailure: "+e.getMessage());
                                Toast.makeText(getContext(), "Không tạo được tài khoản", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return view;

    }


    private void luuDuLieuQLTKLenFirebase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        quyentruycapstring = (String) quyentruycap.getSelectedItem();
        progressDialog.setTitle("Dang luu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id_qltk", "" + timestamp);
        hashMap.put("ten", "" + tennhanvienstring);
        hashMap.put("email", "" + emailstring);
        hashMap.put("sdt", "" + sdtstring);
        hashMap.put("vaiTro", "" + quyentruycapstring);
        hashMap.put("uid", firebaseUser.getUid());
        hashMap.put("timestamp", timestamp);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Quan_Ly_Tai_Khoan");
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

    private void loadDataNVChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idNV = bundle.getString(KEY_ID_NHAN_VIEN);
        }
    }

    public void laytaikhoantuNV() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.child(idNV).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tennhanvienstring = "" + snapshot.child("ten").getValue();
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
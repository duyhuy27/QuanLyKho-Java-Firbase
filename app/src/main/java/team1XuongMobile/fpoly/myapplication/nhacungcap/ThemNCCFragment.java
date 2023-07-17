package team1XuongMobile.fpoly.myapplication.nhacungcap;

import static androidx.constraintlayout.motion.widget.TransitionBuilder.validate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentThemNCCBinding;

public class ThemNCCFragment extends Fragment {
    private FragmentThemNCCBinding binding;
    private String ten ="", dia_chi= "", sdt = "", email = "", kh = "";
    private boolean trangThai = false;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    public static final String TAG = "ThemesNCCFragment";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        laydulieudangnhap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentThemNCCBinding.inflate(inflater, container, false);
        progressDialog = new ProgressDialog(getContext());
        firebaseAuth = FirebaseAuth.getInstance();


        listener();
        return binding.getRoot();
    }
    private void listener(){
        binding.buttonHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        binding.buttonTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
    private void validate() {
        ten = binding.edTenNCC.getText().toString();
        dia_chi = binding.edDiaChiNCC.getText().toString();
        sdt = binding.edSoDienThoaiNCC.getText().toString();
        email = binding.edEmailNCC.getText().toString();
        trangThai = binding.swButtonTrangThai.isChecked();

        if (ten.isEmpty() || dia_chi.isEmpty()|| sdt.isEmpty()||email.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Vui lòng nhập đúng định dạng email", Toast.LENGTH_SHORT).show();
        } else {
            luuDuLieuFirebase();
        }

    }
    private void luuDuLieuFirebase() {
        progressDialog.setTitle("Đang lưu");
        progressDialog.show();

        firebaseUser = firebaseAuth.getCurrentUser();
        long timestamp = System.currentTimeMillis();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id_nha_cc","" + timestamp);
        hashMap.put("ten_nha_cc", "" + ten);
        hashMap.put("dia_chi", "" + dia_chi);
        hashMap.put("so_dien_dienthoai", "" + sdt);
        hashMap.put("email", "" + email);
        hashMap.put("trangThai", "" + trangThai);
        hashMap.put("uid", firebaseUser.getUid());
        hashMap.put("timstamp", timestamp);
        hashMap.put("kh", kh);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("nha_cung_cap");
        ref.child("" + timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Lưu thất bại", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: lỗi lưu lên firebase: "+ e.getMessage());
                    }
                });


    }
    public void laydulieudangnhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kh  = "" + snapshot.child("kh").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
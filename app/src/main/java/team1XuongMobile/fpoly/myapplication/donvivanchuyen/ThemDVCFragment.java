package team1XuongMobile.fpoly.myapplication.donvivanchuyen;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import team1XuongMobile.fpoly.myapplication.databinding.FragmentThemDVCBinding;


public class ThemDVCFragment extends Fragment {
    private FragmentThemDVCBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private String ten = "", dia_chi = "", sdt = "", kh = "";
    private final String TAG = "ThemDVCFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        laydulieudangnhap();
        

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentThemDVCBinding.inflate(inflater, container, false);
        progressDialog = new ProgressDialog(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        listener();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    public void listener(){
        binding.buttonHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDataInput();
                // tắt bàn phim
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


                View view1 = requireActivity().getCurrentFocus();
                if (view1 != null) {
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

            }
        });
        binding.buttonTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void validateDataInput() {
        ten = binding.edTenDVC.getText().toString().trim();
        dia_chi = binding.edDiaChiDVC.getText().toString().trim();
        sdt = binding.edSoDienThoaiDVC.getText().toString().trim();
        if (ten.isEmpty() || dia_chi.isEmpty() || sdt.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            luuDuLieuVaoFireBase();

        }
    }
    public void luuDuLieuVaoFireBase(){
        progressDialog.setTitle("Đag lưu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        firebaseUser = firebaseAuth.getCurrentUser();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id_don_vi_vc","" + timestamp);
        hashMap.put("ten","" + ten);
        hashMap.put("dia_chi","" + dia_chi);
        hashMap.put("hotline","" + sdt);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid",firebaseUser.getUid());
        hashMap.put("kh", kh);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("don_vi_vc");
        ref.child(""+timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Thêm mới thất bại", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: Lỗi vì"+e.getMessage());
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
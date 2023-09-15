package team1XuongMobile.fpoly.myapplication.fragments;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentSuaDVCBinding;


public class SuaDVCFragment extends Fragment {
    public static final String KEY_ID = "id";
    private String id = "";
    private FragmentSuaDVCBinding binding;
    private static final String TAG = "SuaDVCFragment";
    String ten = "", sdt = "", dia_chi = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuaDVCBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle!= null) {
             id = bundle.getString(KEY_ID);
        }
        layDuLieuFirebase();
        listener();

        return binding.getRoot();
    }
    private void layDuLieuFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("don_vi_vc");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 ten ="" + snapshot.child("ten").getValue();
                 sdt = "" + snapshot.child("hotline").getValue();
                 dia_chi = "" + snapshot.child("dia_chi").getValue();
                binding.edTenDVC.setText(ten);
                binding.edSoDienThoaiDVC.setText(sdt);
                binding.edDiaChiDVC.setText(dia_chi);
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: loi lay du lieu tu firebase: "+error.getMessage());
            }
        });
    }
    
    
    private void listener() {
        binding.buttonTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        binding.buttonHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ten = binding.edTenDVC.getText().toString().trim();
                 sdt = binding.edSoDienThoaiDVC.getText().toString().trim();
                 dia_chi = binding.edDiaChiDVC.getText().toString().trim();
                 if (ten.isEmpty() || sdt.isEmpty() || dia_chi.isEmpty()) {
                     Toast.makeText(getContext(), "Vui lòng nhập đủ tất cả các trường", Toast.LENGTH_SHORT).show();
                 }else {
                     HashMap<String,Object> hashMap = new HashMap<>();
                     hashMap.put("ten","" + ten);
                     hashMap.put("dia_chi","" + dia_chi);
                     hashMap.put("hotline","" + sdt);
                     
                     DatabaseReference ref = FirebaseDatabase.getInstance().getReference("don_vi_vc");
                     ref.child(id).updateChildren(hashMap)
                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void unused) {
                                     Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                     getActivity().getSupportFragmentManager().popBackStack();
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                 }
                             });
                 }

                // tắt bàn phim
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


                View view1 = requireActivity().getCurrentFocus();
                if (view1 != null) {
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
            }
        });
    }
    
    
    
    
}
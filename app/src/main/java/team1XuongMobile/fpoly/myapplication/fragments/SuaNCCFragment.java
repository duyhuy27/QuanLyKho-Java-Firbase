package team1XuongMobile.fpoly.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentSuaNCCBinding;


public class SuaNCCFragment extends Fragment {
    private FragmentSuaNCCBinding binding;
    public static final String KEY_ID = "id";
    private String id = "";
    private String ten ="", dia_chi= "", sdt = "", email = "";
    private boolean trangThai = false;
    public static final String TAG = "SuaNCCFragment";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuaNCCBinding.inflate(inflater, container, false);
        //lay du lieu tu fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString(KEY_ID);
        }


        layDuLieuFirebase();
        listener();
        return binding.getRoot();
    }
    private void layDuLieuFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nha_cung_cap");

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ten ="" + snapshot.child("ten_nha_cc").getValue();
                dia_chi =""+ snapshot.child("dia_chi").getValue();
                sdt ="" + snapshot.child("so_dien_dienthoai").getValue();
                email ="" + snapshot.child("email").getValue();
                trangThai = Boolean.parseBoolean("" + snapshot.child("trangThai").getValue());
                binding.edTenNCC.setText(ten);
                binding.edDiaChiNCC.setText(dia_chi);
                binding.edEmailNCC.setText(email);
                binding.edSoDienThoaiNCC.setText(sdt);
                binding.swButtonTrangThai.setChecked(trangThai);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: loi lay du lieu tren firebase"+error.getMessage());

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
        binding.buttonTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        binding.buttonHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ten = binding.edTenNCC.getText().toString().trim();
                dia_chi = binding.edDiaChiNCC.getText().toString().trim();
                sdt = binding.edSoDienThoaiNCC.getText().toString().trim();
                email = binding.edEmailNCC.getText().toString().trim();
                trangThai = binding.swButtonTrangThai.isChecked();
                if (ten.isEmpty() || dia_chi.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đủ tất cả các trường", Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getContext(), "Vui lòng đúng định dạng email", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ten_nha_cc","" + ten);
                    hashMap.put("dia_chi", "" + dia_chi);
                    hashMap.put("so_dien_dienthoai", "" + sdt);
                    hashMap.put("email", "" + email);
                    hashMap.put("trangThai", "" + trangThai);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
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
                                    Log.d(TAG, "onFailure: loi update: " + e.getMessage());
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
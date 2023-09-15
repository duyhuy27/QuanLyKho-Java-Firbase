package team1XuongMobile.fpoly.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentChiTietDVCBinding;


public class ChiTietDVCFragment extends Fragment {
    private FragmentChiTietDVCBinding binding;
    private String id="", ten = "", sdt = "", dia_chi = "";
    public static final String KEY_ID = "id";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChiTietDVCBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        bundle.getString(KEY_ID);
        if (binding != null) {
            id = bundle.getString(KEY_ID);
        }
        layDuLieu();
        binding.buttonTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    private void layDuLieu() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("don_vi_vc");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ten =""+snapshot.child("ten").getValue();
                sdt = ""+snapshot.child("hotline").getValue();
                dia_chi = ""+snapshot.child("dia_chi").getValue();

                binding.tvIdDVC.setText(id);
                binding.tvTenDVC.setText(ten);
                binding.tvSoDienThoaiDVC.setText(sdt);
                binding.tvDiaChiDVC.setText(dia_chi);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
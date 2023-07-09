package team1XuongMobile.fpoly.myapplication.nhacungcap;

import android.graphics.Color;
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
import team1XuongMobile.fpoly.myapplication.databinding.FragmentChiTietNCCBinding;


public class ChiTietNCCFragment extends Fragment {
    private FragmentChiTietNCCBinding binding;
    String id = "";
    public static final String KEY_ID = "id";
    private String ten ="", dia_chi= "", sdt = "", idncc ="";
    private boolean trangThai = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChiTietNCCBinding.inflate(inflater, container, false);
        layDuLieuFragment();
        binding.buttonTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }
    private void layDuLieuFragment(){
        Bundle bundle = getArguments();
        if (bundle!= null) {
            id = bundle.getString(KEY_ID);
        }
        layDuLieuTrenFirebase();

    }
    private void layDuLieuTrenFirebase(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idncc ="" + snapshot.child("id_nha_cc").getValue();
                ten ="" + snapshot.child("ten_nha_cc").getValue();
                dia_chi ="" + snapshot.child("dia_chi").getValue();
                sdt = "" + snapshot.child("so_dien_dienthoai").getValue();
                trangThai = Boolean.parseBoolean("" + snapshot.child("trangThai").getValue());

                binding.tvIdNCC.setText(idncc);
                binding.tvTenNCC.setText(ten);
                binding.tvDiaChiNCC.setText(dia_chi);
                binding.tvSoDienThoaiNCC.setText(sdt);
                if (trangThai) {
                    binding.tvTrangThai.setText("Hoạt động");
                    binding.tvTrangThai1.setTextColor(Color.GREEN);
                    binding.tvTrangThai.setTextColor(Color.GREEN);
                }else {
                    binding.tvTrangThai.setText("Không hoạt động");
                    binding.tvTrangThai.setTextColor(Color.RED);
                    binding.tvTrangThai1.setTextColor(Color.RED);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
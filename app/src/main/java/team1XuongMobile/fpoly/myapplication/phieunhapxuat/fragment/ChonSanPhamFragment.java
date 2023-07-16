package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonSanPhamAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;

public class ChonSanPhamFragment extends Fragment {
    private ArrayList<ChonSanPham> list;
    private ChonSanPhamAdapter adapter;
    private RecyclerView rcvChonSanPham;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chon_san_pham, container, false);
        rcvChonSanPham = view.findViewById(R.id.rcv_chonSanPham);
        list = new ArrayList<>();
        adapter = new ChonSanPhamAdapter(requireContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rcvChonSanPham.setLayoutManager(layoutManager);

        loadFirebase(); // Đổ dữ liệu lên recyclerView
        return view;
    }

    private void loadFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChonSanPham objChonSanPham = dataSnapshot.getValue(ChonSanPham.class);
                    list.add(objChonSanPham);
                }
                adapter.setData(list);
                rcvChonSanPham.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.PhieuNhapAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuNhap;

public class PhieuNhapFragment extends Fragment {
    private FloatingActionButton fab_themPhieuNhap;
    private RecyclerView rcvPhieuNhap;
    private PhieuNhapAdapter adapter;
    private ArrayList<PhieuNhap> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieu_nhap, container, false);
        fab_themPhieuNhap = view.findViewById(R.id.fab_themPhieuNhap);
        rcvPhieuNhap = view.findViewById(R.id.rcv_phieu_nhap);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        adapter = new PhieuNhapAdapter(requireContext());
        list = new ArrayList<>();
        rcvPhieuNhap.setLayoutManager(layoutManager);

        loadFirebasePhieuNhap();
        fab_themPhieuNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickThemPhieuNhap();
            }
        });
        return view;
    }

    public void onclickThemPhieuNhap() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, new ChonDanhSachNCCFragment()).
                addToBackStack(null).commit();
    }

    private void loadFirebasePhieuNhap() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PhieuNhap objPhieuNhap = dataSnapshot.getValue(PhieuNhap.class);
                    list.add(objPhieuNhap);
                }
                adapter.setData(list);
                rcvPhieuNhap.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonNCCAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonNhaCungCapListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonSanPhamListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.OnFragmentResultListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonNCC;

public class NhaCungCapSuaPhieuNhapFragment extends Fragment implements ChonNhaCungCapListener {
    private ChonNCCAdapter adapter;
    private RecyclerView chonNCCRecyclerView;
    private ArrayList<ChonNCC> chonNCCArrayList;
    private OnFragmentResultListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentResultListener) requireContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement FirstFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nha_cung_cap_sua_phieu_nhap, container, false);
        adapter = new ChonNCCAdapter(requireContext(), this);
        chonNCCArrayList = new ArrayList<>();

        chonNCCRecyclerView = view.findViewById(R.id.rcv_chonDanhSachNCC);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        chonNCCRecyclerView.setLayoutManager(layoutManager);

        loadDataFirebase();
        return view;
    }

    public void loadDataFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chonNCCArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChonNCC objChonNCC = dataSnapshot.getValue(ChonNCC.class);
                    chonNCCArrayList.add(objChonNCC);
                }
                adapter.setData(chonNCCArrayList);
                chonNCCRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClickChonNhaCungCap(ChonNCC objChonNCC) {
        String tenNhaCungCap = objChonNCC.getTen_nha_cc();
        listener.onFragmentResult(tenNhaCungCap);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
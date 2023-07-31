package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenModel;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonDonViVanChuyenAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonDonViVanChuyenListener;

public class ChonDonViVanChuyenFragment extends Fragment {
    private ArrayList<VanChuyenModel> list;
    private ChonDonViVanChuyenListener listener;
    private ChonDonViVanChuyenAdapter adapter;
    private RecyclerView rcvChonDonViVanChuyen;
    private LinearLayoutManager layoutManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChonDonViVanChuyenListener) requireContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement FirstFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chon_don_vi_van_chuyen, container, false);
        bindViews(view);
        initObjects();
        setupUI();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFirebase();
    }

    private void bindViews(View view) {
        rcvChonDonViVanChuyen = view.findViewById(R.id.rcvChonDonViVanChuyen);
    }

    private void initObjects() {
        list = new ArrayList<>();
        adapter = new ChonDonViVanChuyenAdapter(requireContext(), listener);
        layoutManager = new LinearLayoutManager(requireContext());
    }

    private void setupUI() {
        rcvChonDonViVanChuyen.setLayoutManager(layoutManager);
    }

    private void loadFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("don_vi_vc");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    VanChuyenModel objVanChuyenModel = dataSnapshot.getValue(VanChuyenModel.class);
                    list.add(objVanChuyenModel);
                }
                adapter.setData(list);
                rcvChonDonViVanChuyen.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
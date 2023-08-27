package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.content.Context;
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
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenModel;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonDonViVanChuyenAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonDonViVanChuyenListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.DonViVanChuyenSuaPhieuXuatListener;

public class DonViVanChuyenSuaPhieuXuatFragment extends Fragment implements ChonDonViVanChuyenListener {
    private ArrayList<VanChuyenModel> list;
    private DonViVanChuyenSuaPhieuXuatListener listener;
    private ChonDonViVanChuyenAdapter adapter;
    private RecyclerView rcvChonDonViVanChuyen;
    private LinearLayoutManager layoutManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DonViVanChuyenSuaPhieuXuatListener) requireActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement FirstFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_don_vi_van_chuyen_sua_phieu_xuat, container, false);
        rcvChonDonViVanChuyen = view.findViewById(R.id.rcvChonDonViVanChuyen);
        list = new ArrayList<>();
        adapter = new ChonDonViVanChuyenAdapter(requireContext(), this);
        layoutManager = new LinearLayoutManager(requireContext());
        rcvChonDonViVanChuyen.setLayoutManager(layoutManager);
        loadFirebase();
        return view;
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

    @Override
    public void onClickChonDonViVanChuyen(VanChuyenModel objVanChuyenModel) {
        String tenDonViVanChuyen = objVanChuyenModel.getTen();
        listener.onFragmentResultDonViVanChuyen(tenDonViVanChuyen);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
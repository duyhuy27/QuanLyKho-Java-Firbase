package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.content.Context;
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
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonSanPhamXAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonSanPhamXListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.OnFragmentResultSanPhamListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.SanPhamSuaPhieuXuatListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;

public class SanPhamSuaPhieuXuatFragment extends Fragment implements ChonSanPhamXListener {
    private ArrayList<ChonSanPham> list;
    private ChonSanPhamXAdapter adapter;
    private RecyclerView rcvChonSanPhamX;
    private LinearLayoutManager layoutManager;
    private SanPhamSuaPhieuXuatListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SanPhamSuaPhieuXuatListener) requireActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement FirstFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_san_pham_sua_phieu_xuat, container, false);
        rcvChonSanPhamX = view.findViewById(R.id.rcv_chonSanPhamX);
        list = new ArrayList<>();
        adapter = new ChonSanPhamXAdapter(requireContext(), this);
        layoutManager = new LinearLayoutManager(requireContext());
        rcvChonSanPhamX.setLayoutManager(layoutManager);
        loadFirebase();
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
                rcvChonSanPhamX.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClickChonSanPhamX(ChonSanPham objChonSanPham) {
        String tenSanPham = objChonSanPham.getTenSp();
        String maSanPham = objChonSanPham.getMaSp();
        String giaSanPham = objChonSanPham.getGiaNhap();
        listener.onFragmentResultSanPham(tenSanPham, maSanPham, giaSanPham);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
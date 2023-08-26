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

import team1XuongMobile.fpoly.myapplication.Model.KhachHang;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonKhachHangAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonKhachHangListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.KhachHangSuaPhieuXuatListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.SanPhamSuaPhieuXuatListener;

public class KhachHangSuaPhieuXuatFragment extends Fragment implements ChonKhachHangListener {
    private ArrayList<KhachHang> list;
    private ChonKhachHangAdapter adapter;
    private RecyclerView rcvChonKhachHang;
    private LinearLayoutManager layoutManager;
    private KhachHangSuaPhieuXuatListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (KhachHangSuaPhieuXuatListener) requireActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement FirstFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khach_hang_sua_phieu_xuat, container, false);
        rcvChonKhachHang = view.findViewById(R.id.rcv_chonKhachHang);
        list = new ArrayList<>();
        adapter = new ChonKhachHangAdapter(requireContext(), this);
        layoutManager = new LinearLayoutManager(requireContext());
        rcvChonKhachHang.setLayoutManager(layoutManager);
        loadFirebase();
        return view;
    }

    private void loadFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("khach_hang");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    KhachHang objKhachHang = dataSnapshot.getValue(KhachHang.class);
                    list.add(objKhachHang);
                }
                adapter.setData(list);
                rcvChonKhachHang.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClickChonKhachHang(KhachHang objKhachHang) {
        String tenKhachHang = objKhachHang.getTen_kh();
        listener.onFragmentResultKhachHang(tenKhachHang);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
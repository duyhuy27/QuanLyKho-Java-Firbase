package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ChonKhachHangFragment extends Fragment {
    private ArrayList<KhachHang> list;
    private ChonKhachHangAdapter adapter;
    private RecyclerView rcvChonKhachHang;
    private LinearLayoutManager layoutManager;
    private ChonKhachHangListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChonKhachHangListener) requireContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement FirstFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chon_khach_hang, container, false);
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
        rcvChonKhachHang = view.findViewById(R.id.rcv_chonKhachHang);
    }

    private void initObjects() {
        list = new ArrayList<>();
        adapter = new ChonKhachHangAdapter(requireContext(), listener);
        layoutManager = new LinearLayoutManager(requireContext());
    }

    private void setupUI() {
        rcvChonKhachHang.setLayoutManager(layoutManager);
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
}
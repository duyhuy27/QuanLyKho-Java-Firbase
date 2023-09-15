package team1XuongMobile.fpoly.myapplication.fragments;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.models.KhachHang;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.adapter.ChonKhachHangAdapter;
import team1XuongMobile.fpoly.myapplication.interfaced.ChonKhachHangListener;

public class ChonKhachHangFragment extends Fragment {
    private ArrayList<KhachHang> list;
    private ChonKhachHangAdapter adapter;
    private RecyclerView rcvChonKhachHang;
    private LinearLayoutManager layoutManager;
    private ChonKhachHangListener listener;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFirebaseChonSanPham();
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

    private void loadFirebaseChonSanPham() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kh = String.valueOf(snapshot.child("kh").getValue(String.class));

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("khach_hang");
                databaseReference.orderByChild("kh").equalTo(kh).addValueEventListener(new ValueEventListener() {
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
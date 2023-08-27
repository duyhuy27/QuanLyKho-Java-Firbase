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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonSanPhamXAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonSanPhamXListener;

public class ChonSanPhamXFragment extends Fragment {
    private ArrayList<ChonSanPham> list;
    private ChonSanPhamXAdapter adapter;
    private RecyclerView rcvChonSanPhamX;
    private ChonSanPhamXListener listener;
    private LinearLayoutManager layoutManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChonSanPhamXListener) requireContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement FirstFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chon_san_pham_x, container, false);
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
        loadFirebaseChonSanPhamX();
    }

    private void bindViews(View view) {
        rcvChonSanPhamX = view.findViewById(R.id.rcv_chonSanPhamX);
    }

    private void initObjects() {
        list = new ArrayList<>();
        adapter = new ChonSanPhamXAdapter(requireContext(), listener);
        layoutManager = new LinearLayoutManager(requireContext());
    }

    private void setupUI() {
        rcvChonSanPhamX.setLayoutManager(layoutManager);
    }

    private void loadFirebaseChonSanPhamX() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kh = String.valueOf(snapshot.child("kh").getValue(String.class));

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SanPham");
                databaseReference.orderByChild("kh").equalTo(kh).addValueEventListener(new ValueEventListener() {
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
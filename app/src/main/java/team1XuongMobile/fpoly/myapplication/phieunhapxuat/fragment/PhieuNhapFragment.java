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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieu_nhap, container, false);
        fab_themPhieuNhap = view.findViewById(R.id.fab_themPhieuNhap);
        rcvPhieuNhap = view.findViewById(R.id.rcv_phieu_nhap);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        adapter = new PhieuNhapAdapter(requireContext());
        list = new ArrayList<>();
        rcvPhieuNhap.setLayoutManager(layoutManager);

        fab_themPhieuNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickThemPhieuNhap();
            }
        });
        loadFirebasePhieuNhap();
        return view;
    }

    public void onclickThemPhieuNhap() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, new ChonDanhSachNCCFragment()).
                addToBackStack(null).commit();
    }

    private void loadFirebasePhieuNhap() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kh = String.valueOf(snapshot.child("kh").getValue(String.class));

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
                databaseReference.orderByChild("kh").equalTo(kh).addValueEventListener(new ValueEventListener() {
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

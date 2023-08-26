package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.PhieuNhapAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuNhap;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.phieunhap.ChiTietPNFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.phieunhap.LichSuPNFragment;

public class PhieuNhapFragment extends Fragment implements PhieuNhapAdapter.PhieuNhapInterface {
    private FloatingActionButton fab_themPhieuNhap;
    private RecyclerView rcvPhieuNhap;
    private PhieuNhapAdapter adapter;
    private PhieuNhapAdapter.PhieuNhapInterface listener;
    public static final String KEY_ID_PHIEU_NHAP = "idPN";
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
        listener = this;

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
                .replace(R.id.layout_content, new ChonDanhSachNCCFragment())
                .addToBackStack(null)
                .commit();
    }

    private void loadFirebasePhieuNhap() {
        list = new ArrayList<>();
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
                            Collections.reverse(list);
                        }
                        adapter = new PhieuNhapAdapter(getContext(), listener, list);
                        rcvPhieuNhap.setAdapter(adapter);
                        rcvPhieuNhap.setLayoutManager(new LinearLayoutManager(getContext()));
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

    @Override
    public void ChitietPN(String idPN) {
        Bundle bundlechitietPN = new Bundle();
        bundlechitietPN.putString(KEY_ID_PHIEU_NHAP, idPN);
        ChiTietPNFragment chiTietPNFragment = new ChiTietPNFragment();
        chiTietPNFragment.setArguments(bundlechitietPN);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, chiTietPNFragment).addToBackStack(null).commit();

    }

    @Override
    public void SuaPN(String idPN) {
    }

    @Override
    public void LichsuPN(String idPN) {
        Bundle bundlelsPN = new Bundle();
        bundlelsPN.putString(KEY_ID_PHIEU_NHAP, idPN);
        LichSuPNFragment lichSuPNFragment = new LichSuPNFragment();
        lichSuPNFragment.setArguments(bundlelsPN);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, lichSuPNFragment).addToBackStack(null).commit();

    }
}

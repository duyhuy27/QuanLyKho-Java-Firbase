package team1XuongMobile.fpoly.myapplication.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;

import team1XuongMobile.fpoly.myapplication.interfaced.ChonNhaCungCapListener;
import team1XuongMobile.fpoly.myapplication.adapter.ChonNCCAdapter;
import team1XuongMobile.fpoly.myapplication.models.ChonNCC;

public class ChonDanhSachNCCFragment extends Fragment {
    private ChonNCCAdapter adapter;
    private RecyclerView chonNCCRecyclerView;
    private ArrayList<ChonNCC> chonNCCArrayList;
    private ChonNhaCungCapListener listener;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChonNhaCungCapListener) requireContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement FirstFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chon_danh_sach_ncc, container, false);
        adapter = new ChonNCCAdapter(requireContext(), listener);
        chonNCCArrayList = new ArrayList<>();

        chonNCCRecyclerView = view.findViewById(R.id.rcv_chonDanhSachNCC);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        chonNCCRecyclerView.setLayoutManager(layoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        loadFirebaseChonNhaCungCap();

        return view;
    }

    private void loadFirebaseChonNhaCungCap() {
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kh = String.valueOf(snapshot.child("kh").getValue(String.class));

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
                databaseReference.orderByChild("kh").equalTo(kh).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chonNCCArrayList.clear();
                        progressDialog.dismiss();
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

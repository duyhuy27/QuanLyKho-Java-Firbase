package team1XuongMobile.fpoly.myapplication.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import team1XuongMobile.fpoly.myapplication.adapter.PhieuXuatAdapter;
import team1XuongMobile.fpoly.myapplication.models.PhieuXuat;

public class PhieuXuatFragment extends Fragment implements PhieuXuatAdapter.ViewHolder.PhieuXuatInterface {
    private RecyclerView rcvPhieuXuat;
    private FloatingActionButton fabPhieuXuat;
    private PhieuXuatAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<PhieuXuat> list;
    private LinearLayoutManager layoutManager;
    private TextView tv02;
    private ProgressDialog progressDialog;
    PhieuXuatAdapter.ViewHolder.PhieuXuatInterface phieuXuatInterface;
    public static final String KEY_ID_PHIEU_XUAT = "id_px_bd";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieu_xuat, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        phieuXuatInterface = this;

        // Ánh xạ view
        bindViews(view);
        // Khởi tạo dối tượng mới
        initObjects();
        // Các sự kiện click
        setupUI();
        return view;
    }

    private void bindViews(View view) {
        // RecyclerView
        rcvPhieuXuat = view.findViewById(R.id.rcv_phieu_xuat);
        // FloatingActionButton
        fabPhieuXuat = view.findViewById(R.id.fab_themPhieuXuat);
        tv02 = view.findViewById(R.id.tv02);
    }

    private void initObjects() {
        adapter = new PhieuXuatAdapter(requireContext());
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(requireContext());
    }

    private void setupUI() {
        fabPhieuXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, new TaoHDXFragment(), "TaoHDXFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        rcvPhieuXuat.setLayoutManager(layoutManager);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFirebasePhieuXuat();
    }

    private void loadFirebasePhieuXuat() {
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kh = String.valueOf(snapshot.child("kh").getValue(String.class));

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
                databaseReference.orderByChild("kh").equalTo(kh).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            PhieuXuat objPhieuXuat = dataSnapshot.getValue(PhieuXuat.class);
                            list.add(objPhieuXuat);
                            Collections.reverse(list);
                        }
                        progressDialog.dismiss();
                        if (list.size() == 0) {
                            tv02.setVisibility(View.VISIBLE);
                        } else {
                            tv02.setVisibility(View.GONE);
                        }
                        adapter.setData(list);
                        adapter = new PhieuXuatAdapter(getContext(), list, phieuXuatInterface);
                        rcvPhieuXuat.setAdapter(adapter);
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
    public void updatePXClick(String id) {

    }

    @Override
    public void LichSuPXClick(String id) {
        Bundle bundlechitietpx = new Bundle();
        bundlechitietpx.putString(KEY_ID_PHIEU_XUAT, id);
        LichSuPXFragment lichSuPXFragment = new LichSuPXFragment();
        lichSuPXFragment.setArguments(bundlechitietpx);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_phieu_xuat, lichSuPXFragment).addToBackStack(null).commit();
    }

    @Override
    public void chiTietPXClick(String id) {
        Bundle bundlechitietpx = new Bundle();
        bundlechitietpx.putString(KEY_ID_PHIEU_XUAT, id);
        ChiTietPXFragment chiTietPXFragment = new ChiTietPXFragment();
        chiTietPXFragment.setArguments(bundlechitietpx);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_phieu_xuat, chiTietPXFragment).addToBackStack(null).commit();
    }
}
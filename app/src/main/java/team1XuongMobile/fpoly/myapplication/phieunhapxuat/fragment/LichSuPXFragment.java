package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.LichSuPXAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.NotifyXuat;


public class LichSuPXFragment extends Fragment {
    String key_idpx = "";
    public static final String KEY_ID_PHIEU_XUAT = "id_px_bd";
    RecyclerView recyclerView;
    LichSuPXAdapter lichSuPXAdapter;
    ArrayList<NotifyXuat> lichSuPxArrayList;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    public LichSuPXFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su_p_x, container, false);
        recyclerView = view.findViewById(R.id.rcv_ls_px);
        firebaseAuth = FirebaseAuth.getInstance();
        loadDataPXChuyenSang();
        loadDuLieuNotifileFirebase();
        return view;
    }
    private void loadDataPXChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            key_idpx = bundle.getString(KEY_ID_PHIEU_XUAT);
            Log.e("zzzzzz", "id nhan duoc: " + key_idpx);
        }
    }
    private void loadDuLieuNotifileFirebase() {

        firebaseUser = firebaseAuth.getCurrentUser();
        lichSuPxArrayList = new ArrayList<>();
        if (firebaseUser == null) {
            return;
        }
        DatabaseReference useref1 = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        useref1.child(key_idpx).child("notify_xuat")

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        lichSuPxArrayList.clear();
                        for (DataSnapshot dsls : snapshot.getChildren()) {
                            NotifyXuat themntfPhieuXuat = dsls.getValue(NotifyXuat.class);
                            lichSuPxArrayList.add(themntfPhieuXuat);

                        }
                        lichSuPXAdapter = new LichSuPXAdapter(getContext(),lichSuPxArrayList);
                        recyclerView.setAdapter(lichSuPXAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
package team1XuongMobile.fpoly.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.models.LichSuPhieuNhan;
import team1XuongMobile.fpoly.myapplication.adapter.LichSuAdapter;


public class LichSuPNFragment extends Fragment {
    RecyclerView recyclerView;
    LichSuAdapter lichSuAdapter;
    EditText SearchLichSu;
    ArrayList<LichSuPhieuNhan> lichSuPhieuNhanArrayList;
    String  idPN = "";
    public static final String KEY_ID_PHIEU_NHAP = "idPN";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su_pn, container, false);
        recyclerView = view.findViewById(R.id.recyc_lichsupn);
        SearchLichSu = view.findViewById(R.id.edt_timkiemlichsupn_nhanvien);
        firebaseAuth = FirebaseAuth.getInstance();

        SearchLichSu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    lichSuAdapter.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loandatachuyensang();
        loadDuLieuNotifileFirebase();
        return view;
    }

    public void loandatachuyensang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idPN = bundle.getString(KEY_ID_PHIEU_NHAP);
            Log.d("lichsu", "loandatachuyensang: " + idPN);
        }
    }


    private void loadDuLieuNotifileFirebase() {

        firebaseUser = firebaseAuth.getCurrentUser();
        lichSuPhieuNhanArrayList = new ArrayList<>();
        if (firebaseUser == null) {
            return;
        }
        DatabaseReference useref1 = FirebaseDatabase.getInstance().getReference("notifile_phieunhap");
        useref1.orderByChild("id_phieu_nhap").equalTo(idPN)

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        lichSuPhieuNhanArrayList.clear();
                        for (DataSnapshot dspn : snapshot.getChildren()) {
                            LichSuPhieuNhan lichSuPhieuNhan = dspn.getValue(LichSuPhieuNhan.class);
                            lichSuPhieuNhanArrayList.add(lichSuPhieuNhan);
                        }
                        lichSuAdapter = new LichSuAdapter(getContext(), lichSuPhieuNhanArrayList);
                        recyclerView.setAdapter(lichSuAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
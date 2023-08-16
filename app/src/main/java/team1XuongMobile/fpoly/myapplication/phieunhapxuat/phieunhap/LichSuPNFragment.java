package team1XuongMobile.fpoly.myapplication.phieunhapxuat.phieunhap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Adapter.NhanVienAdapter;
import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.LichSuPhieuNhan;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.phieunhap.adapterlichsu.LichSuAdapter;


public class LichSuPNFragment extends Fragment {
    RecyclerView recyclerView;
    LichSuAdapter lichSuAdapter;
    ArrayList<LichSuPhieuNhan> lichSuPhieuNhanArrayList;
    String khstring = "", idPN = "";
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
        firebaseAuth = FirebaseAuth.getInstance();
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
                        for (DataSnapshot dsls : snapshot.getChildren()) {
                            LichSuPhieuNhan themlichSuPhieuNhan = dsls.getValue(LichSuPhieuNhan.class);
                            lichSuPhieuNhanArrayList.add(themlichSuPhieuNhan);
                            Log.d("quanquan", "list" + lichSuPhieuNhanArrayList);
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
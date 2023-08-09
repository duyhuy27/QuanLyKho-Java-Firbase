package team1XuongMobile.fpoly.myapplication.Fragment.KhachHang;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuXuat;


public class LichSuChiTietKhachHangVFragment extends Fragment {
    private TextView tv_kco_gd;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<PhieuXuat> list;
    private LichSuCtKhachHangAdapter lichSuCtKhachHangAdapter;
    public static final String KEY_ID_KHACH_HANG = "id_kh_bd";
    String id_khls;

    public LichSuChiTietKhachHangVFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su_chi_tiet_khach_hang_v, container, false);
        recyclerView = view.findViewById(R.id.rcv_lichsu_khachhang);
        tv_kco_gd = view.findViewById(R.id.tv_kcodongd_lskh);
        tv_kco_gd.setVisibility(View.GONE);
        loadFirebasePhieuXuat();
        return view;
    }

    //    private void loadFirebasePhieuXuat() {
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        list = new ArrayList<>();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    PhieuXuat objPhieuXuat = dataSnapshot.getValue(PhieuXuat.class);
//                    list.add(objPhieuXuat);
//                }
//                lichSuCtKhachHangAdapter= new LichSuCtKhachHangAdapter(getContext(),list);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                recyclerView.setAdapter(lichSuCtKhachHangAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    private void loadFirebasePhieuXuat() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        list = new ArrayList<>();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LuuIdKh", Context.MODE_PRIVATE);
        id_khls = sharedPreferences.getString("idkh", null);
        Log.e("idlskh","id nhận dc :"+id_khls);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        databaseReference.orderByChild("id_kh").equalTo(id_khls).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PhieuXuat objPhieuXuat = dataSnapshot.getValue(PhieuXuat.class);
                    list.add(objPhieuXuat);
                }
                if (list.size()==0){
                    tv_kco_gd.setVisibility(View.VISIBLE);
                }else {
                    lichSuCtKhachHangAdapter = new LichSuCtKhachHangAdapter(getContext(), list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(lichSuCtKhachHangAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
package team1XuongMobile.fpoly.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Adapter.NhanVienAdapter;
import team1XuongMobile.fpoly.myapplication.Adapter.QuanLyTaiKhoanAdapter;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.SuaNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan.SuaTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan.ThemTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.Model.QuanLyTaiKhoan;
import team1XuongMobile.fpoly.myapplication.R;


public class QuanLyTaiKhoanFragment extends Fragment {
    RecyclerView recyclerView_qltk;

    QuanLyTaiKhoanAdapter quanLyTaiKhoanAdapter;
    ArrayList<NhanVien> nhanVienArrayList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_tai_khoan, container, false);
        recyclerView_qltk = view.findViewById(R.id.recyc_quanlytaikhoan);

        loaddulieuQLTKFirebase();
        return view;

    }

    private void loaddulieuQLTKFirebase() {
        nhanVienArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Quan_Ly_Tai_Khoan");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nhanVienArrayList.clear();
                for (DataSnapshot dsqltk : snapshot.getChildren()) {
                    NhanVien themtk = dsqltk.getValue(NhanVien.class);
                    nhanVienArrayList.add(themtk);
                }
                quanLyTaiKhoanAdapter = new QuanLyTaiKhoanAdapter(getContext(), nhanVienArrayList);
                recyclerView_qltk.setAdapter(quanLyTaiKhoanAdapter);
                recyclerView_qltk.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Không thêm được dữ liệu lên Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
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


public class QuanLyTaiKhoanFragment extends Fragment implements QuanLyTaiKhoanAdapter.QltkInterface {
    RecyclerView recyclerView_qltk;
    FloatingActionButton floating_themtk;
    QuanLyTaiKhoanAdapter quanLyTaiKhoanAdapter;
    ArrayList<QuanLyTaiKhoan> quanLyTaiKhoanArrayList;
    private QuanLyTaiKhoanAdapter.QltkInterface qltkInterface;
    public static final String KEY_ID_QUAN_LY_TAI_KHOAN = "idQLTK";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_tai_khoan, container, false);
        recyclerView_qltk = view.findViewById(R.id.recyc_quanlytaikhoan);
        floating_themtk = view.findViewById(R.id.floatingbutton_quanlytaikhoan);
        floating_themtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemTaiKhoanFragment themTaiKhoanFragment = new ThemTaiKhoanFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_quanlytaikhoan, themTaiKhoanFragment).addToBackStack(null).commit();
            }
        });
        loaddulieuQLTKFirebase();
        return view;

    }

    private void loaddulieuQLTKFirebase() {
        quanLyTaiKhoanArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quanLyTaiKhoanArrayList.clear();
                for (DataSnapshot dsqltk : snapshot.getChildren()) {
                    QuanLyTaiKhoan themtk = dsqltk.getValue(QuanLyTaiKhoan.class);
                    quanLyTaiKhoanArrayList.add(themtk);
                }
                quanLyTaiKhoanAdapter = new QuanLyTaiKhoanAdapter(getContext(), quanLyTaiKhoanArrayList, qltkInterface);
                recyclerView_qltk.setAdapter(quanLyTaiKhoanAdapter);
                recyclerView_qltk.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Không thêm được dữ liệu lên Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void updateQLTKlick(String id) {
        Bundle bundlesuaQLTK = new Bundle();
        bundlesuaQLTK.putString(KEY_ID_QUAN_LY_TAI_KHOAN, id);
        SuaTaiKhoanFragment suaTaiKhoanFragment = new SuaTaiKhoanFragment();
        suaTaiKhoanFragment.setArguments(bundlesuaQLTK);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_quanlytaikhoan, suaTaiKhoanFragment).addToBackStack(null).commit();
    }

    @Override
    public void deleteQLTKlick(String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có muốn xóa hay không?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id != null && !id.isEmpty()) {
                    ref.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Xóa Thất Bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
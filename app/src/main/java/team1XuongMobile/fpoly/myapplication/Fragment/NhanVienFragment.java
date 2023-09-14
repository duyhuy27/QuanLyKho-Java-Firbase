package team1XuongMobile.fpoly.myapplication.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.ChitietNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.SuaNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.ThemNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan.CachChucTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan.ThemTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.R;


public class NhanVienFragment extends Fragment implements NhanVienAdapter.nhanvienInterface {
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    NhanVienAdapter nhanVienAdapter;
    ArrayList<NhanVien> nhanVienArrayList;
    private NhanVienAdapter.nhanvienInterface listener;
    FloatingActionButton themnhanvien;
    public static final String KEY_ID_NHAN_VIEN = "idNV";
    EditText inputsearchNV;
    TextView tv_none_nv;
    ImageView imgv_none_nv;
    String khstring = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhan_vien, container, false);
        recyclerView = view.findViewById(R.id.recyc_nhanvien);
        tv_none_nv = view.findViewById(R.id.tv_none_fragmentnv);
        imgv_none_nv = view.findViewById(R.id.imgv_none_fragmentnv);
        themnhanvien = view.findViewById(R.id.floatingbutton_themnhanvien);
        inputsearchNV = view.findViewById(R.id.edt_timkiem_nhanvien);
        firebaseAuth = FirebaseAuth.getInstance();



        loadDuLieuNhanVienFirebase();
        listener = this;
        inputsearchNV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    nhanVienAdapter.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        themnhanvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemNVFragment themNVFragment = new ThemNVFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, themNVFragment).addToBackStack(null).commit();
            }
        });


        return view;
    }

    private void loadDuLieuNhanVienFirebase() {

        firebaseUser = firebaseAuth.getCurrentUser();
        nhanVienArrayList = new ArrayList<>();
        if (firebaseUser == null) {
            return;
        }
        String uid = firebaseUser.getUid();
        DatabaseReference useref = FirebaseDatabase.getInstance().getReference("Accounts").child(uid);
        useref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                khstring = "" + snapshot.child("kh").getValue(String.class);
                if (khstring != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
                    Query query = ref.orderByChild("kh").equalTo(khstring);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            nhanVienArrayList.clear();
                            if(snapshot.getChildren() != null){
                                for (DataSnapshot dsnv : snapshot.getChildren()) {
                                    tv_none_nv.setVisibility(View.INVISIBLE);
                                    imgv_none_nv.setVisibility(View.INVISIBLE);
                                    NhanVien themnhanvien = dsnv.getValue(NhanVien.class);
                                    nhanVienArrayList.add(themnhanvien);
                                }
                            }
                            else {
                                tv_none_nv.setVisibility(View.VISIBLE);
                                imgv_none_nv.setVisibility(View.VISIBLE);
                            }


                            nhanVienAdapter = new NhanVienAdapter(getContext(), nhanVienArrayList, listener);
                            recyclerView.setAdapter(nhanVienAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Không lấy được dữ liệu lên Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void updateNVClick(String id) {
        Bundle bundlesuaNV = new Bundle();
        bundlesuaNV.putString(KEY_ID_NHAN_VIEN, id);
        SuaNVFragment suaNVFragment = new SuaNVFragment();
        suaNVFragment.setArguments(bundlesuaNV);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, suaNVFragment).addToBackStack(null).commit();
    }

    @Override
    public void deleteNVClick(String id) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Đang Thực Thi");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có muốn xóa hay không?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id != null && !id.isEmpty()) {
                    ref.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if(nhanVienArrayList.size() == 0){
                                        tv_none_nv.setVisibility(View.VISIBLE);
                                        imgv_none_nv.setVisibility(View.VISIBLE);
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                                    }


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
                progressDialog.dismiss();

                dialog.dismiss();
            }
        });
        builder.show();

    }

    @Override
    public void chiTietNVClick(String id) {
        Bundle bundlechitietNV = new Bundle();
        bundlechitietNV.putString(KEY_ID_NHAN_VIEN, id);
        ChitietNVFragment chitietNVFragment = new ChitietNVFragment();
        chitietNVFragment.setArguments(bundlechitietNV);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, chitietNVFragment).addToBackStack(null).commit();
    }

    @Override
    public void bonhiemNVClick(String id) {
        Bundle bundlebonhiemNV = new Bundle();
        bundlebonhiemNV.putString(KEY_ID_NHAN_VIEN, id);
        ThemTaiKhoanFragment themTaiKhoanFragment = new ThemTaiKhoanFragment();
        themTaiKhoanFragment.setArguments(bundlebonhiemNV);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, themTaiKhoanFragment).addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDuLieuNhanVienFirebase();
    }
}
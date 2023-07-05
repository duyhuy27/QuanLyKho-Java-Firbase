package team1XuongMobile.fpoly.myapplication.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.Filter;

import android.widget.ImageButton;
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
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.ChitietNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.SuaNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.ThemNVFragment;
import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.R;


public class NhanVienFragment extends Fragment implements NhanVienAdapter.nhanvienInterface {
    RecyclerView recyclerView;
    NhanVienAdapter nhanVienAdapter;
    ArrayList<NhanVien> nhanVienArrayList;
    private NhanVienAdapter.nhanvienInterface listener;
    FloatingActionButton themnhanvien;
    public static final String KEY_ID_NHAN_VIEN = "idNV";
    EditText inputsearchNV;
    ImageButton searchNV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhan_vien, container, false);
        recyclerView = view.findViewById(R.id.recyc_nhanvien);
        themnhanvien = view.findViewById(R.id.floatingbutton_themnhanvien);
        inputsearchNV = view.findViewById(R.id.edt_timkiem_nhanvien);

        listener =  this;
        inputsearchNV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        nhanVienAdapter.getFilter().filter(s);
                    }catch (Exception e){
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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_nhanvien, themNVFragment).addToBackStack(null).commit();
            }
        });
        loadDuLieuNhanVienFirebase();

        return view;
    }

    private void loadDuLieuNhanVienFirebase() {
        nhanVienArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nhanVienArrayList.clear();
                for (DataSnapshot dsnv : snapshot.getChildren()) {
                    NhanVien themnhanvien = dsnv.getValue(NhanVien.class);
                    nhanVienArrayList.add(themnhanvien);
                }
                nhanVienAdapter = new NhanVienAdapter(getContext(), nhanVienArrayList, listener);
                recyclerView.setAdapter(nhanVienAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Không thêm được dữ liệu lên Firebase", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void updateNVClick(String id) {
        Bundle bundlesuaNV = new Bundle();
        bundlesuaNV.putString(KEY_ID_NHAN_VIEN, id);
        Log.e("quanquan", "id chuyen sang: "+id );
        SuaNVFragment suaNVFragment = new SuaNVFragment();
        suaNVFragment.setArguments(bundlesuaNV);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_nhanvien, suaNVFragment).addToBackStack(null).commit();
    }

    @Override
    public void deleteNVClick(String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có muốn xóa hay không?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(id != null && !id.isEmpty()){
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

    @Override
    public void chiTietNVClick(String id) {
        Bundle bundlechitietNV = new Bundle();
        bundlechitietNV.putString(KEY_ID_NHAN_VIEN, id);
        ChitietNVFragment chitietNVFragment = new ChitietNVFragment();
        chitietNVFragment.setArguments(bundlechitietNV);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_nhanvien, chitietNVFragment).addToBackStack(null).commit();
    }



}
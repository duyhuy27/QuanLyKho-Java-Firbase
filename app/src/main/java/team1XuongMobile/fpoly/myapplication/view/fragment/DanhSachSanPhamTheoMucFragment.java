package team1XuongMobile.fpoly.myapplication.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.adapter.SanPhamAdapter;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentDanhSachSanPhamTheoMucBinding;
import team1XuongMobile.fpoly.myapplication.models.SanPhamModels;

public class DanhSachSanPhamTheoMucFragment extends Fragment implements SanPhamAdapter.sanPhamInterface{

    private ArrayList<SanPhamModels> sanPhamModelsArrayList;

    private SanPhamAdapter sanPhamAdapter;

    private SanPhamAdapter.sanPhamInterface listeners;

    private FragmentDanhSachSanPhamTheoMucBinding binding;

    private String idLoaiSp;

    private String tenSp;

    private String uid;


    public DanhSachSanPhamTheoMucFragment() {
        // Required empty public constructor
    }

    public static DanhSachSanPhamTheoMucFragment newInstance(String idLoaiSp, String tenSp, String uid) {
        DanhSachSanPhamTheoMucFragment fragment = new DanhSachSanPhamTheoMucFragment();
        Bundle args = new Bundle();
        args.putString("idLoaiSp", idLoaiSp);
        args.putString("tenSp", tenSp);
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idLoaiSp = getArguments().getString("idLoaiSp");
            tenSp = getArguments().getString("tenSp");
            uid = getArguments().getString("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDanhSachSanPhamTheoMucBinding.inflate(inflater, container, false);

        listeners = this;

        setupLoadSanPham();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void setupLoadSanPham() {
        if (tenSp.equals("Tên sản phẩm")){
            loadTatCaSanPham();
        }
        else {
            loadSanPhamTheoMuc();
        }
    }

    private void loadSanPhamTheoMuc() {
        sanPhamModelsArrayList = new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("SanPham");
        ref.orderByChild("id_loai").equalTo(idLoaiSp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sanPhamModelsArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            SanPhamModels sanPhamModels = ds.getValue(SanPhamModels.class);
                            sanPhamModelsArrayList.add(sanPhamModels);
                        }
                        sanPhamAdapter = new SanPhamAdapter(sanPhamModelsArrayList,getContext(), listeners);
                        binding.rcvSanPham.setAdapter(sanPhamAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadTatCaSanPham() {
        sanPhamModelsArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SanPham");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhamModelsArrayList.clear();

                for (DataSnapshot ds : snapshot.getChildren()){
                    SanPhamModels sanPhamModels = ds.getValue(SanPhamModels.class);
                    sanPhamModelsArrayList.add(sanPhamModels);
                }
                sanPhamAdapter = new SanPhamAdapter(sanPhamModelsArrayList,getContext(), listeners);
                binding.rcvSanPham.setAdapter(sanPhamAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void chiTietSpClick(String id) {

    }

    @Override
    public void xoaSpClick(String id) {

    }

    @Override
    public void suaSpClick(String id) {

    }
}
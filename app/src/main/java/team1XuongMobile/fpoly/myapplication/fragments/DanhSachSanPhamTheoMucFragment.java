package team1XuongMobile.fpoly.myapplication.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentDanhSachSanPhamTheoMucBinding;
import team1XuongMobile.fpoly.myapplication.adapter.SanPhamAdapter;
import team1XuongMobile.fpoly.myapplication.models.SanPhamModels;

public class DanhSachSanPhamTheoMucFragment extends Fragment implements SanPhamAdapter.sanPhamInterface {

    private ArrayList<SanPhamModels> sanPhamModelsArrayList;

    private SanPhamAdapter sanPhamAdapter;

    private SanPhamAdapter.sanPhamInterface listeners;

    private FragmentDanhSachSanPhamTheoMucBinding binding;

    private String idLoaiSp;

    private String tenSp;

    private String uid;

    public static final String TAG = "DanhSachSanPhamTheoMucFragment";

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


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
        binding = FragmentDanhSachSanPhamTheoMucBinding.inflate(LayoutInflater.from(getContext()), container, false);

        String idLoaiSp = getArguments().getString("idLoaiSp");
        Log.d("DSSPTM", "idLoaiSp: " + idLoaiSp);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        listeners = this;


        setupLoadSanPham();

        binding.edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    sanPhamAdapter.getFilter().filter(charSequence);
                } catch (Exception e) {
                    Log.d(TAG, "onTextChanged: " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void setupLoadSanPham() {
        if (tenSp.equals("Tất cả")) {
            loadTatCaSanPham();
        } else {
            loadSanPhamTheoMuc();
        }
    }

    private void loadSanPhamTheoMuc() {
        Log.d(TAG, "loadSanPhamTheoMuc: id loai " + idLoaiSp);
        sanPhamModelsArrayList = new ArrayList<>();

        binding.progressbar.setVisibility(View.VISIBLE)
        ;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SanPham");
        Query query = ref.orderByChild("id_loai").equalTo(idLoaiSp);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                sanPhamModelsArrayList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    SanPhamModels sanPhamModels = ds.getValue(SanPhamModels.class);
                    sanPhamModelsArrayList.add(sanPhamModels);
                }

                sanPhamAdapter = new SanPhamAdapter(sanPhamModelsArrayList, getContext(), listeners);
                binding.rcvSanPham.setAdapter(sanPhamAdapter);

                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }

    private void loadTatCaSanPham() {
        sanPhamModelsArrayList = new ArrayList<>();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(firebaseAuth.getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kh = "" + snapshot.child("kh").getValue(String.class);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SanPham");
                Query query = ref.orderByChild("kh").equalTo(kh);

                binding.progressbar.setVisibility(View.VISIBLE);
                query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                sanPhamModelsArrayList.clear();

                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    SanPhamModels sanPhamModels = ds.getValue(SanPhamModels.class);
                                    sanPhamModelsArrayList.add(sanPhamModels);
                                }

                                sanPhamAdapter = new SanPhamAdapter(sanPhamModelsArrayList, getContext(), listeners);
                                binding.rcvSanPham.setAdapter(sanPhamAdapter);

                                binding.progressbar.setVisibility(View.GONE);
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


    public static final String KEY_ID_SP = "id";

    @Override
    public void chiTietSpClick(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID_SP, id);
        Log.d(TAG, "chiTietSpClick: ID chuyển đi là " + id);

        ChiTietSPFragment fragment = new ChiTietSPFragment();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, fragment).addToBackStack(null).commit();
    }

    @Override
    public void xoaSpClick(String id) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bạn chắc chắn muốn xóa sản phẩm này ")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.show();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SanPham");
                        ref.child(id).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        dialogInterface.dismiss();
                                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        sanPhamAdapter.notifyDataSetChanged();
                                        loadSanPhamTheoMuc();
                                        loadTatCaSanPham();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        dialogInterface.dismiss();
                                        Toast.makeText(getContext(), "Lỗi " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onFailure: Xóa thất bại " + e.getMessage());
                                        sanPhamAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void suaSpClick(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID_SP, id);
        Log.d(TAG, "chiTietSpClick: ID chuyển đi là " + id);

        SuaSPFragment fragment = new SuaSPFragment();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupLoadSanPham();
    }
}
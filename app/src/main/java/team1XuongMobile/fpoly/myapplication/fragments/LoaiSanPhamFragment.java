package team1XuongMobile.fpoly.myapplication.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import team1XuongMobile.fpoly.myapplication.adapter.LoaiSanPhamAdapter;
import team1XuongMobile.fpoly.myapplication.models.LoaiSanPham;
import team1XuongMobile.fpoly.myapplication.R;


public class LoaiSanPhamFragment extends Fragment implements LoaiSanPhamAdapter.ViewHolder.LoaiSanPhamInterface {
    RecyclerView recyclerView;
    LinearLayout Khonglsp_linearLayout;
    LoaiSanPhamAdapter loaiSanPhamAdapter;
    ArrayList<LoaiSanPham> loaiSanPhamArrayList;
    private LoaiSanPhamAdapter.ViewHolder.LoaiSanPhamInterface loaiSanPhamInterface;
    FloatingActionButton fabThemLsp;
    EditText inputsearchLoaiSP;
    public static final String KEY_ID_LOAI_SAN_PHAM = "id_lsp_bd";
    FirebaseAuth firebaseAuth;
    String khString="";
    private ProgressDialog progressDialog;


    public LoaiSanPhamFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loai_san_pham, container, false);

        recyclerView = view.findViewById(R.id.rcv_loaisanpham);
        fabThemLsp = view.findViewById(R.id.fab_themloaisanpham);
        inputsearchLoaiSP = view.findViewById(R.id.edt_timkiem_loaisanpham);
        Khonglsp_linearLayout = view.findViewById(R.id.ll_khongdulieu_lsp);

        loaiSanPhamInterface = this;

        firebaseAuth = FirebaseAuth.getInstance();
        loadDuLieuLoaiSanPhamFirebase();


        inputsearchLoaiSP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    loaiSanPhamAdapter.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fabThemLsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemLoaiSanPhamFragment themLoaiSanPhamFragment = new ThemLoaiSanPhamFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_loaisanpham, themLoaiSanPhamFragment).addToBackStack(null).commit();
            }
        });



        return view;


    }

    private void loadDuLieuLoaiSanPhamFirebase() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        loaiSanPhamArrayList = new ArrayList<>();

        if (firebaseUser == null) {
            // User not logged in, handle the case as needed
            return;
        }

        progressDialog.show();
        String uid = firebaseUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kh = ""+snapshot.child("kh").getValue(String.class);
                if (kh != null) {
                    DatabaseReference loaiSpRef = FirebaseDatabase.getInstance().getReference("loai_sp");
                    Query query = loaiSpRef.orderByChild("kh").equalTo(kh);

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            loaiSanPhamArrayList.clear();
                            for (DataSnapshot dslsp : snapshot.getChildren()) {
                                LoaiSanPham themlsp = dslsp.getValue(LoaiSanPham.class);
                                if (themlsp != null) {
                                    loaiSanPhamArrayList.add(themlsp);
                                }
                            }
                            progressDialog.dismiss();
                            if (loaiSanPhamArrayList.size()==0){
                                Khonglsp_linearLayout.setVisibility(View.VISIBLE);
                            }else {
                                Khonglsp_linearLayout.setVisibility(View.GONE);

                                loaiSanPhamAdapter = new LoaiSanPhamAdapter(getContext(), loaiSanPhamArrayList, loaiSanPhamInterface);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(loaiSanPhamAdapter);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Không thêm được dữ liệu lên Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Không thêm được dữ liệu lên Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void updateLoaiSPClick(String id) {
        Bundle bundlechitietLSP = new Bundle();
        bundlechitietLSP.putString(KEY_ID_LOAI_SAN_PHAM, id);
        SuaLoaiSanPhamFragment suaLoaiSanPhamFragment = new SuaLoaiSanPhamFragment();
        suaLoaiSanPhamFragment.setArguments(bundlechitietLSP);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_loaisanpham, suaLoaiSanPhamFragment).addToBackStack(null).commit();
    }

    @Override
    public void deleteLoaiSPClick(String id) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loai_sp");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có muốn xóa hay không?");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Đang xóa vui lòng chờ trong giây lát...");
        progressDialog.setCanceledOnTouchOutside(false);


        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(id != null && !id.isEmpty()){
                    progressDialog.show();
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
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
                    };

                    // Gửi runnable với thời gian chờ là 500 miliseconds
                    handler.postDelayed(runnable, 500);
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
    public void chiTietLoaiSPClick(String id) {
        Bundle bundlechitietLSP = new Bundle();
        bundlechitietLSP.putString(KEY_ID_LOAI_SAN_PHAM, id);
        ChiTietLoaiSPFragment chiTietLoaiSPFragment = new ChiTietLoaiSPFragment();
        chiTietLoaiSPFragment.setArguments(bundlechitietLSP);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_loaisanpham, chiTietLoaiSPFragment).addToBackStack(null).commit();
    }

}
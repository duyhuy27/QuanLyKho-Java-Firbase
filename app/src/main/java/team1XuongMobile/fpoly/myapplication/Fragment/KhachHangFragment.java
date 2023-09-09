package team1XuongMobile.fpoly.myapplication.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
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

import team1XuongMobile.fpoly.myapplication.Adapter.KhachHangAdapter;
import team1XuongMobile.fpoly.myapplication.Adapter.LoaiSanPhamAdapter;
import team1XuongMobile.fpoly.myapplication.Fragment.KhachHang.ChiTietKhachHangFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.KhachHang.SuaKhachHangFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.KhachHang.ThemKhachHangFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPham.SuaLoaiSanPhamFragment;
import team1XuongMobile.fpoly.myapplication.Model.KhachHang;
import team1XuongMobile.fpoly.myapplication.Model.LoaiSanPham;
import team1XuongMobile.fpoly.myapplication.R;


public class KhachHangFragment extends Fragment implements KhachHangAdapter.ViewHolder.KhachHangInterface {
    RecyclerView recyclerView;
    LinearLayout Khongkh_linearLayout;
    KhachHangAdapter khachHangAdapter;
    ArrayList<KhachHang> khachHangArrayList;
    private KhachHangFragment khachHangInterface;
    FloatingActionButton fabThemkh;
    EditText inputsearchKhachHang;
    FirebaseAuth firebaseAuth;
    String khString="";
    private ProgressDialog progressDialog;
    public static final String KEY_ID_KHACH_HANG = "id_kh_bd";

    public KhachHangFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khach_hang, container, false);

        recyclerView = view.findViewById(R.id.rcv_khachhang);
        fabThemkh = view.findViewById(R.id.fab_themkhachhang);
        inputsearchKhachHang = view.findViewById(R.id.edt_timkiem_khachhang);
        Khongkh_linearLayout = view.findViewById(R.id.ll_khongdulieu_kh);

        khachHangInterface = this;
        firebaseAuth = FirebaseAuth.getInstance();

        inputsearchKhachHang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    khachHangAdapter.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fabThemkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemKhachHangFragment themKhachHangFragment = new ThemKhachHangFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,themKhachHangFragment).addToBackStack(null).commit();
            }
        });
        loadDuLieuKhachHangFirebase();
        return view;
    }

    private void loadDuLieuKhachHangFirebase() {
        khachHangArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
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
                    DatabaseReference loaiSpRef = FirebaseDatabase.getInstance().getReference("khach_hang");
                    Query query = loaiSpRef.orderByChild("kh").equalTo(kh);

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            khachHangArrayList.clear();
                            for (DataSnapshot dslsp : snapshot.getChildren()) {
                                KhachHang khachHang = dslsp.getValue(KhachHang.class);
                                if (khachHang != null) {
                                    khachHangArrayList.add(khachHang);
                                }
                            }
                            progressDialog.dismiss();
                            if (khachHangArrayList.size()==0){
                                Khongkh_linearLayout.setVisibility(View.VISIBLE);
                            }else {
                                Khongkh_linearLayout.setVisibility(View.GONE);

                                khachHangAdapter = new KhachHangAdapter(getContext(), khachHangArrayList, khachHangInterface);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(khachHangAdapter);
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
    public void updateKhachHangClick(String id) {
        Bundle bundleKH = new Bundle();
        bundleKH.putString(KEY_ID_KHACH_HANG, id);
        SuaKhachHangFragment suaKhachHangFragment = new SuaKhachHangFragment();
        suaKhachHangFragment.setArguments(bundleKH);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, suaKhachHangFragment).addToBackStack(null).commit();
    }

    @Override
    public void deleteKhachHangClick(String id) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("khach_hang");
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
    public void chiTietKhachHangClick(String id) {
        Bundle bundleKH = new Bundle();
        bundleKH.putString(KEY_ID_KHACH_HANG, id);
        ChiTietKhachHangFragment chiTietKhachHangFragment = new ChiTietKhachHangFragment();
        chiTietKhachHangFragment.setArguments(bundleKH);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, chiTietKhachHangFragment).addToBackStack(null).commit();
    }

}
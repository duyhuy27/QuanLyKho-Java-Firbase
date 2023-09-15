package team1XuongMobile.fpoly.myapplication.fragments;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.adapter.QuanLyTaiKhoanAdapter;
import team1XuongMobile.fpoly.myapplication.models.QuanLyTaiKhoan;
import team1XuongMobile.fpoly.myapplication.R;


public class QuanLyTaiKhoanFragment extends Fragment implements QuanLyTaiKhoanAdapter.TaikhoanInterface {
    RecyclerView recyclerView_qltk;
    EditText inputsearchTK;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String khstring = "";
    QuanLyTaiKhoanAdapter quanLyTaiKhoanAdapter;
    ArrayList<QuanLyTaiKhoan> quanLyTaiKhoanArrayList;

    public static final String KEY_ID_TAI_KHOAN = "id";


    QuanLyTaiKhoanAdapter.TaikhoanInterface taikhoanInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_tai_khoan, container, false);
        recyclerView_qltk = view.findViewById(R.id.recyc_quanlytaikhoan);
        inputsearchTK = view.findViewById(R.id.edt_quanlytaikhoan_timkiem);
        taikhoanInterface = this;

        firebaseAuth = FirebaseAuth.getInstance();


        inputsearchTK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    quanLyTaiKhoanAdapter.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        laydulieudangnhap();

        loaddulieuQLTKFirebase();
        return view;

    }

    private void loaddulieuQLTKFirebase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        quanLyTaiKhoanArrayList = new ArrayList<>();

        if (firebaseUser == null) {
            return;
        }
        DatabaseReference useref = FirebaseDatabase.getInstance().getReference("Accounts");
        useref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                useref.orderByChild("kh").equalTo(khstring).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        quanLyTaiKhoanArrayList.clear();
                        for (DataSnapshot dstk : snapshot.getChildren()) {
                            QuanLyTaiKhoan themtk = dstk.getValue(QuanLyTaiKhoan.class);
                            quanLyTaiKhoanArrayList.add(themtk);
                            Log.d("quanquan", "list" + quanLyTaiKhoanArrayList);
                        }
                        quanLyTaiKhoanAdapter = new QuanLyTaiKhoanAdapter(getContext(), quanLyTaiKhoanArrayList, taikhoanInterface);
                        recyclerView_qltk.setAdapter(quanLyTaiKhoanAdapter);
                        recyclerView_qltk.setLayoutManager(new LinearLayoutManager(getContext()));
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


    @Override
    public void CachChucTKClick(String id) {
        Bundle bundlecachchucTK = new Bundle();
        bundlecachchucTK.putString(KEY_ID_TAI_KHOAN, id);
        CachChucTaiKhoanFragment cachChucTaiKhoanFragment = new CachChucTaiKhoanFragment();
        cachChucTaiKhoanFragment.setArguments(bundlecachchucTK);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, cachChucTaiKhoanFragment).addToBackStack(null).commit();
    }

    @Override
    public void ChiTietTKClick(String id) {
        Bundle bundlechitietTK = new Bundle();
        bundlechitietTK.putString(KEY_ID_TAI_KHOAN, id);
        ChiTietTaiKhoanFragment chiTietTaiKhoanFragment = new ChiTietTaiKhoanFragment();
        chiTietTaiKhoanFragment.setArguments(bundlechitietTK);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, chiTietTaiKhoanFragment).addToBackStack(null).commit();
    }

    @Override
    public void DatLaiMatKhauTKClick(String id) {
        Bundle datlaimatkhauTK = new Bundle();
        datlaimatkhauTK.putString(KEY_ID_TAI_KHOAN, id);
        DatLaiMatKhauFragment datLaiMatKhauFragment = new DatLaiMatKhauFragment();
        datLaiMatKhauFragment.setArguments(datlaimatkhauTK);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, datLaiMatKhauFragment).addToBackStack(null).commit();

    }


    public void laydulieudangnhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                khstring = "" + snapshot.child("kh").getValue();
                Log.d("quanquan", "kh " + khstring);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
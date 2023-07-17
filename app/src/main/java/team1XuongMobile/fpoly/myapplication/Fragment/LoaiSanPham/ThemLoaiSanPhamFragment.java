package team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPham;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;


public class ThemLoaiSanPhamFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    ImageButton back;
    AppCompatButton hoantat;
    Switch swt_trangthai;
    TextInputEditText loaisp;
    String ten_lsp = "" ,khString="";
    boolean trangthai;
    FirebaseUser firebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        laydulieudangnhap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_loai_san_pham, container, false);
        back = view.findViewById(R.id.btn_quay_lai_them_lsp);
        hoantat = view.findViewById(R.id.btn_themloaisp_hoantat);
        swt_trangthai = view.findViewById(R.id.swt_them_trangthai_loaisp);

        loaisp = view.findViewById(R.id.edt_them_tenloaisp);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loaisp.length()==0){
                    loaisp.requestFocus();
                    loaisp.setError("Không để trống loại sản phẩm");
                }else {
                    LuuDuLieuLoaiSpLenFireBase();
                }

            }
        });
        return view;


    }

    private void LuuDuLieuLoaiSpLenFireBase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        ten_lsp = loaisp.getText().toString().trim();
        if (swt_trangthai.isChecked()){
            trangthai = true;
        }else {
            trangthai = false;
        }

        progressDialog.setTitle("Dang luu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id_loai_sp", "" +timestamp);
        hashMap.put("ten_loai_sp", "" + ten_lsp);
        hashMap.put("TrangThai",Boolean.parseBoolean(String.valueOf(trangthai)));
        hashMap.put("uid", firebaseUser.getUid());
        hashMap.put("timestamp",timestamp);
        hashMap.put("kh",khString);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loai_sp");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Thêm Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void laydulieudangnhap(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                khString = "" + snapshot.child("kh").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
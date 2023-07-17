package team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPham;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
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


public class SuaLoaiSanPhamFragment extends Fragment {

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ImageButton back;
    AppCompatButton hoantat;
    Switch swt_trangthai;
    TextInputEditText edt_loaisp;
    String sua_ten_lspString = "" , idlsp = "",idlspString ="";
    boolean trangthai , sua_trangthai;

    public static final String KEY_ID_LOAI_SAN_PHAM = "id_lsp_bd";

    public SuaLoaiSanPhamFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sua_loai_san_pham, container, false);

        back = view.findViewById(R.id.btn_quay_lai_lsp);
        edt_loaisp = view.findViewById(R.id.edt_sua_tenloaisp);
        swt_trangthai = view.findViewById(R.id.swt_sua_trangthai_loaisp);

        hoantat = view.findViewById(R.id.btn_sualoaisp_hoantat);
        firebaseAuth = FirebaseAuth.getInstance();

        loadDataLSPChuyenSang();
        setDataLoaiSpLenView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_loaisp.length()==0){
                    edt_loaisp.requestFocus();
                    edt_loaisp.setError("Không để trống loại sản phẩm");
                }else {
                    LuuDuLieuLoaiSpLenFireBase();
                }
            }
        });

        return view;
    }

    private void loadDataLSPChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idlsp = bundle.getString(KEY_ID_LOAI_SAN_PHAM);
            Log.e("zzzzzz", "id nhan duoc: "+idlsp );
        }
    }

    private void setDataLoaiSpLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loai_sp");
        ref.child(idlsp)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sua_ten_lspString = "" + snapshot.child("ten_loai_sp").getValue();
                        idlspString = "" + snapshot.child("id_loai_sp").getValue();
                        trangthai = Boolean.parseBoolean("" + snapshot.child("TrangThai").getValue());

                        if(trangthai){
                            swt_trangthai.setChecked(true);
                        }
                        else {
                            swt_trangthai.setChecked(false);
                        }

                        edt_loaisp.setText(sua_ten_lspString);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void LuuDuLieuLoaiSpLenFireBase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        sua_ten_lspString = edt_loaisp.getText().toString().trim();
        if (swt_trangthai.isChecked()){
            sua_trangthai = true;
        }else {
            sua_trangthai = false;
        }
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Dang luu...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("ten_loai_sp", "" + sua_ten_lspString);
        hashMap.put("TrangThai", Boolean.parseBoolean(String.valueOf(sua_trangthai)));
        hashMap.put("uid", firebaseUser.getUid());


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loai_sp");
        ref.child(idlsp)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Sửa Thành Công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Sửa Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
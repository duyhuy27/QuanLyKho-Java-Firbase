package team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPham;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    TextInputEditText ed_loaisp;
    String ten_lsp = "", khString = "";
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

        ed_loaisp = view.findViewById(R.id.edt_them_tenloaisp);


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
                if (ed_loaisp.length() == 0) {
                    ed_loaisp.requestFocus();
                    ed_loaisp.setError("Không để trống loại sản phẩm");
                } else {
                    LuuDuLieuLoaiSpLenFireBase();
                    anbanphim();
                }


            }
        });
        return view;


    }

    private void LuuDuLieuLoaiSpLenFireBase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        ten_lsp = ed_loaisp.getText().toString().trim();
        if (swt_trangthai.isChecked()) {
            trangthai = true;
        } else {
            trangthai = false;
        }

        progressDialog.setTitle("Dang luu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loai_sp");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean isDuplicate = false;
                //Duyệt qua tất cả các con của node "loai_sp"
                for (DataSnapshot child : snapshot.getChildren()) {
                    //Lấy giá trị của thuộc tính "ten_loai_sp" của mỗi con
                    String tenLoaiSP = child.child("ten_loai_sp").getValue(String.class);
                    //So sánh với tên loại sản phẩm bạn muốn thêm
                    if (tenLoaiSP.equals(ten_lsp)) {
                        //Nếu bằng nhau, đặt biến trùng lặp là true và thoát khỏi vòng lặp
                        isDuplicate = true;
                        break;
                    }
                }
                //Kiểm tra biến trùng lặp
                if (isDuplicate) {
                    //Nếu là true, thông báo cho người dùng biết và không thêm vào cơ sở dữ liệu
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Tên loại sản phẩm đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    //Nếu là false, thêm vào cơ sở dữ liệu bình thường
                    //Tạo một hashMap để lưu các thuộc tính của loại sản phẩm
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id_loai_sp", "" + timestamp);
                    hashMap.put("ten_loai_sp", ten_lsp);
                    hashMap.put("TrangThai", Boolean.parseBoolean(String.valueOf(trangthai)));
                    hashMap.put("uid", firebaseUser.getUid());
                    hashMap.put("timestamp", timestamp);
                    hashMap.put("kh", khString);

                    //Thêm vào node "loai_sp" với key là timestamp
                    ref.child("" + timestamp)
                            .setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    ed_loaisp.setText("");
                                    ed_loaisp.clearFocus();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void laydulieudangnhap() {
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
    private void anbanphim(){
        //Lấy đối tượng InputMethodManager từ hệ thống
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//Ẩn bàn phím ảo khi nhấn vào nút
        imm.hideSoftInputFromWindow(hoantat.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
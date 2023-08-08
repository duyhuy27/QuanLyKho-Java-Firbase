package team1XuongMobile.fpoly.myapplication.Fragment.KhachHang;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
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


public class SuaKhachHangFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageButton back;
    AppCompatButton hoantat;

    TextInputEditText ed_sua_ten_kh, ed_sua_sdt_kh, ed_sua_email_kh , ed_sua_diachi_kh;
    String sua_ten_khString = "", sua_sdt_khString = "", sua_email_khString = "", sua_diachi_khString = "" , id_kh;


    public static final String KEY_ID_KHACH_HANG = "id_kh_bd";

    public SuaKhachHangFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sua_khach_hang, container, false);

        back = view.findViewById(R.id.btn_quay_lai_kh);
        hoantat = view.findViewById(R.id.btn_suakhachhang_hoantat);

        ed_sua_ten_kh = view.findViewById(R.id.edt_sua_ten_kh);
        ed_sua_sdt_kh = view.findViewById(R.id.edt_sua_sdt_kh);
        ed_sua_email_kh = view.findViewById(R.id.edt_sua_email_kh);
        ed_sua_diachi_kh = view.findViewById(R.id.edt_sua_diachi_kh);

        firebaseAuth = FirebaseAuth.getInstance();

        loadDataNVChuyenSang();
        setDataKhachHangLenView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                anbanphim();
            }
        });

        hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_sua_ten_kh.length()==0){
                    ed_sua_ten_kh.requestFocus();
                    ed_sua_ten_kh.setError("không được để trống tên khách hàng");
                }else if(ed_sua_sdt_kh.length()==0) {
                    ed_sua_sdt_kh.requestFocus();
                    ed_sua_sdt_kh.setError("không được để trống số điện thoại khách hàng");
                }else if (!isValidPhoneNumber(ed_sua_sdt_kh.getText().toString())) {
                    ed_sua_sdt_kh.requestFocus();
                    ed_sua_sdt_kh.setError("không đúng định dạng số điện thoại !");
                } else if(ed_sua_email_kh.length()==0) {
                    ed_sua_email_kh.requestFocus();
                    ed_sua_email_kh.setError("không được để trống email khách hàng");
                }else if (!isValidEmail(ed_sua_email_kh.getText().toString())) {
                    ed_sua_email_kh.requestFocus();
                    ed_sua_email_kh.setError("không đúng định dạng email !");
                }else if(ed_sua_diachi_kh.length()==0) {
                    ed_sua_diachi_kh.requestFocus();
                    ed_sua_diachi_kh.setError("không được để trống số địa chỉ khách hàng");
                }
                else {
                    LuuDuLieuKhachHangLenFireBase();
                }
            }
        });

        return view;
    }

    private void loadDataNVChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_kh = bundle.getString(KEY_ID_KHACH_HANG);
            Log.e("suakhid", "id nhan duoc: " + id_kh);
        }
    }

    private void setDataKhachHangLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("khach_hang");
        ref.child(id_kh)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sua_ten_khString = "" + snapshot.child("ten_kh").getValue();
                        sua_sdt_khString = "" + snapshot.child("sdt_kh").getValue();
                        sua_email_khString = "" + snapshot.child("email_kh").getValue();
                        sua_diachi_khString = "" + snapshot.child("diachi_kh").getValue();

                        ed_sua_ten_kh.setText(sua_ten_khString);
                        ed_sua_sdt_kh.setText(sua_sdt_khString);
                        ed_sua_email_kh.setText(sua_email_khString);
                        ed_sua_diachi_kh.setText(sua_diachi_khString);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void LuuDuLieuKhachHangLenFireBase() {
        firebaseUser = firebaseAuth.getCurrentUser();

        sua_ten_khString = ed_sua_ten_kh.getText().toString().trim();
        sua_sdt_khString = ed_sua_sdt_kh.getText().toString().trim();
        sua_email_khString = ed_sua_email_kh.getText().toString().trim();
        sua_diachi_khString = ed_sua_diachi_kh.getText().toString().trim();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Dang luu...");
        progressDialog.show();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("khach_hang");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean checkemail = false , checksdt = false;
                for (DataSnapshot child : snapshot.getChildren()) {

                    String emai_lKhS = child.child("email_kh").getValue(String.class);
                    String sdt_lKhS = child.child("sdt_kh").getValue(String.class);
                    String id_khS = child.child("id_kh").getValue(String.class);

                    //Thêm điều kiện này để bỏ qua child có id_kh trùng với id_kh đang sửa
                    if (id_khS.equals(id_kh)) {
                        continue;
                    }

                    if (emai_lKhS.equals(sua_email_khString)) {
                        checkemail = true;
                        break;
                    }
                    if (sdt_lKhS.equals(sua_sdt_khString)) {
                        checksdt = true;
                        break;
                    }

                }
                if (checksdt){
                    //Nếu là true, thông báo cho người dùng biết và không thêm vào cơ sở dữ liệu
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                } else if (checkemail) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("ten_kh", "" +sua_ten_khString);
                    hashMap.put("sdt_kh", "" +sua_sdt_khString);
                    hashMap.put("email_kh", "" +sua_email_khString);
                    hashMap.put("diachi_kh", "" +sua_diachi_khString);

                    hashMap.put("uid", firebaseUser.getUid());
                    ref.child(id_kh)
                            .updateChildren(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Sửa Thành Công", Toast.LENGTH_SHORT).show();
                                    clearEdt();
                                    anbanphim();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Sửa Thất Bại", Toast.LENGTH_SHORT).show();
                                    anbanphim();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //check định dạng email
    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    //check định dạng sdt
    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber) && android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }
    private void anbanphim(){
        //Lấy đối tượng InputMethodManager từ hệ thống
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//Ẩn bàn phím ảo khi nhấn vào nút
        imm.hideSoftInputFromWindow(hoantat.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    private void clearEdt(){
        ed_sua_ten_kh.clearFocus();
        ed_sua_sdt_kh.clearFocus();
        ed_sua_email_kh.clearFocus();
        ed_sua_diachi_kh.clearFocus();
    }
}
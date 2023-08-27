package team1XuongMobile.fpoly.myapplication.Fragment.KhachHang;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
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


public class ThemKhachHangFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    ImageButton back;
    AppCompatButton hoantat;

    TextInputEditText ed_ten_kh, ed_sdt_kh, ed_email_kh , ed_diachi_kh;
    String ten_kh = "", sdt_kh = "", email_kh = "", diachi_kh = "" ,khString="";

    FirebaseUser firebaseUser;

    public ThemKhachHangFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        laydulieudangnhap();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_khach_hang, container, false);
        back = view.findViewById(R.id.btn_quay_lai_kh);
        hoantat = view.findViewById(R.id.btn_themkhachhang_hoantat);

        ed_ten_kh = view.findViewById(R.id.edt_them_ten_kh);
        ed_sdt_kh = view.findViewById(R.id.edt_them_sdt_kh);
        ed_email_kh = view.findViewById(R.id.edt_them_email_kh);
        ed_diachi_kh = view.findViewById(R.id.edt_them_diachi_kh);

        ed_diachi_kh.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    anbanphim();
                    ed_diachi_kh.setCursorVisible(false);
                    return true;
                }
                return false;
            }
        });
        ed_diachi_kh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_diachi_kh.setCursorVisible(true);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                anbanphim();
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_ten_kh.length()==0){
                    ed_ten_kh.requestFocus();
                    ed_ten_kh.setError("không được để trống tên khách hàng");
                }else if(ed_sdt_kh.length()==0) {
                    ed_sdt_kh.requestFocus();
                    ed_sdt_kh.setError("không được để trống số điện thoại khách hàng");
                }else if (!isValidPhoneNumber(ed_sdt_kh.getText().toString())) {
                    ed_sdt_kh.requestFocus();
                    ed_sdt_kh.setError("không đúng định dạng số điện thoại !");
                } else if(ed_email_kh.length()==0) {
                    ed_email_kh.requestFocus();
                    ed_email_kh.setError("không được để trống email khách hàng");
                }else if (!isValidEmail(ed_email_kh.getText().toString())) {
                    ed_email_kh.requestFocus();
                    ed_email_kh.setError("không đúng định dạng email !");
                }else if(ed_diachi_kh.length()==0) {
                    ed_diachi_kh.requestFocus();
                    ed_diachi_kh.setError("không được để trống số địa chỉ khách hàng");
                }
                else {
                    LuuDuLieuKhachHangLenFireBase();
                }
            }
        });

        return view;
    }

    private void LuuDuLieuKhachHangLenFireBase() {
        firebaseUser = firebaseAuth.getCurrentUser();

        ten_kh = ed_ten_kh.getText().toString().trim();
        sdt_kh = ed_sdt_kh.getText().toString().trim();
        email_kh = ed_email_kh.getText().toString().trim();
        diachi_kh = ed_diachi_kh.getText().toString().trim();

        progressDialog.setTitle("Dang luu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("khach_hang");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean checkemail = false , checksdt = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    //Lấy giá trị của thuộc tính "ten_loai_sp" của mỗi con
                    String emai_lKhS = child.child("email_kh").getValue(String.class);
                    String sdt_lKhS = child.child("sdt_kh").getValue(String.class);

                    //So sánh với tên loại sản phẩm bạn muốn thêm
                    if (emai_lKhS.equals(email_kh)) {
                        //Nếu bằng nhau, đặt biến trùng lặp là true và thoát khỏi vòng lặp
                        checkemail = true;
                        break;
                    }
                    if (sdt_lKhS.equals(sdt_kh)) {
                        //Nếu bằng nhau, đặt biến trùng lặp là true và thoát khỏi vòng lặp
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

                    hashMap.put("id_kh", "" +timestamp);
                    hashMap.put("ten_kh", "" +ten_kh);
                    hashMap.put("sdt_kh", "" +sdt_kh);
                    hashMap.put("email_kh", "" +email_kh);
                    hashMap.put("diachi_kh", "" +diachi_kh);
                    hashMap.put("uid", firebaseUser.getUid());
                    hashMap.put("timestamp",timestamp);

                    hashMap.put("kh",khString);
                    ref.child(""+ timestamp)
                            .setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    clearEdt();
                                    Toast.makeText(getContext(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                                    anbanphim();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Thêm Thất Bại", Toast.LENGTH_SHORT).show();
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

    public void laydulieudangnhap(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                khString = ""+snapshot.child("kh").getValue();
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
    private void clearEdt(){
        ed_ten_kh.setText("");
        ed_ten_kh.clearFocus();

        ed_sdt_kh.setText("");
        ed_sdt_kh.clearFocus();

        ed_email_kh.setText("");
        ed_email_kh.clearFocus();

        ed_diachi_kh.setText("");
        ed_diachi_kh.clearFocus();
    }
}
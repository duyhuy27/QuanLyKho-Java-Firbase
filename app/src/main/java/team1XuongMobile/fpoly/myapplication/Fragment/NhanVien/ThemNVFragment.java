package team1XuongMobile.fpoly.myapplication.Fragment.NhanVien;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;


public class ThemNVFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    AppCompatButton hoanttat;
    ImageButton back;
    EditText tennhanvien, email, sdt;
    Spinner vaitro;
    RadioButton danglam, danghi, tamnghi;
    String tenstring = "", emailstring = "", trangthai = "", vaitrostring = "", sdtstring = "", ngaystring = "", khstring = "";
    FirebaseUser firebaseUser;


    public ThemNVFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_n_v, container, false);
        tennhanvien = view.findViewById(R.id.ed_themnhanvien_tennhanvien);
        email = view.findViewById(R.id.ed_themnhanvien_email);
        sdt = view.findViewById(R.id.ed_themnhanvien_sdtnhanvien);
        vaitro = view.findViewById(R.id.spinner_themnhanvien_vaitronhanvien);
        danglam = view.findViewById(R.id.rabtn_themnhanvien_danglam);
        danghi = view.findViewById(R.id.rabtn_themnhanvien_danghi);
        tamnghi = view.findViewById(R.id.rabtn_themnhanvien_tamnghi);
        hoanttat = view.findViewById(R.id.btn_themnhanvien_hoantat);
        back = view.findViewById(R.id.imgbt_themnhanvien_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        ArrayList<String> dataListspinner = new ArrayList<String>();
        dataListspinner.add("Lao Công");
        dataListspinner.add("Bảo Vệ");
        dataListspinner.add("Nhân Viên Bốc Vác");
        dataListspinner.add("Nhân Viên Kiểm Kê");
        dataListspinner.add("Tổ Trưởng");
        dataListspinner.add("Tổ Phó");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, dataListspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaitro.setAdapter(adapter);
        danglam.setChecked(true);
        laydulieudangnhap();
        hoanttat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDataInput();
            }
        });

        return view;
    }

    private void validateDataInput() {
        if (tennhanvien.length() == 0) {
            tennhanvien.requestFocus();
            tennhanvien.setError("Không để trống phần tên nhân viên");
        } else if (email.length() == 0) {
            email.requestFocus();
            email.setError("Không để trống phần email");
        } else if (sdt.length() == 0) {
            sdt.requestFocus();
            sdt.setError("Không để trống phần số điện thoại");
        } else if (!email.getText().toString().matches("^[A-Za-z0-9_.]{6,32}@([a-zA-Z0-9]{2,12}+.)([a-zA-Z]{2,12})+$")) {
            email.requestFocus();
            email.setError("Sai định dang email");
        } else if (!sdt.getText().toString().matches("^[0-9]{9,10}")) {
            sdt.requestFocus();
            sdt.setError("Số điện thoại không hợp lệ");
        } else if (!TextUtils.isDigitsOnly(sdt.getText().toString())) {
            sdt.requestFocus();
            sdt.setError("Số điện thoại phải là số");
        } else {
            luuDuLieuNhanVienLenFirebase();
            tennhanvien.setText("");
            email.setText("");
            sdt.setText("");

        }
    }

    private void luuDuLieuNhanVienLenFirebase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        tenstring = tennhanvien.getText().toString().trim();
        emailstring = email.getText().toString().trim();
        sdtstring = sdt.getText().toString().trim();
        vaitrostring = (String) vaitro.getSelectedItem();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng ngày tháng
        ngaystring = sdf.format(calendar.getTime());
        if (danglam.isChecked()) {
            trangthai = "Đang Làm";
        } else if (danghi.isChecked()) {
            trangthai = "Đã Nghỉ";
        } else if (tamnghi.isChecked()) {
            trangthai = "Tạm Nghỉ";
        }
        progressDialog.setTitle("Dang lưu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("username", "" + tenstring);
        hashMap.put("ngay_vaolam", "" + ngaystring);
        hashMap.put("vaiTro", "" + vaitrostring);
        hashMap.put("email", "" + emailstring);
        hashMap.put("sdt", "" + sdtstring);
        hashMap.put("trang_thai", "" + trangthai);
        hashMap.put("uid", firebaseUser.getUid());
        hashMap.put("timestamp", timestamp);
        hashMap.put("kh", khstring);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.child("" + timestamp)
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

    public void laydulieudangnhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                khstring = "" + snapshot.child("kh").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
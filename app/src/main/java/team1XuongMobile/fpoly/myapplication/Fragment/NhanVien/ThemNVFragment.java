package team1XuongMobile.fpoly.myapplication.Fragment.NhanVien;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import team1XuongMobile.fpoly.myapplication.Adapter.NhanVienAdapter;
import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.R;


public class ThemNVFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    Button hoanttat;
    ImageButton back;
    EditText tennhanvien, email, sdt;
    Spinner vaitro;
    RadioButton danglam, danghi, tamnghi;
    String ten = "", emailstring = "", trangthai = "", vaitrostring = "", sdtstring = "";


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
        dataListspinner.add("Nhân Viên Kho");
        dataListspinner.add("Nhân Viên Chi Nhánh");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dataListspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaitro.setAdapter(adapter);
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
        } else if (!TextUtils.isDigitsOnly(sdt.getText().toString())) {
            sdt.requestFocus();
            sdt.setError("Số điện thoại phải là số");
        } else {
            luuDuLieuNhanVienLenFirebase();
        }
    }

    private void luuDuLieuNhanVienLenFirebase() {
        ten = tennhanvien.getText().toString().trim();
        emailstring = email.getText().toString().trim();
        sdtstring = sdt.getText().toString().trim();
        vaitrostring = (String) vaitro.getSelectedItem();
        if (danglam.isChecked()) {
            trangthai = "Đang Làm";
        } else if (danghi.isChecked()) {
            trangthai = "Đã Nghỉ";
        } else if (tamnghi.isChecked()) {
            trangthai = "Tạm Nghỉ";
        }
        progressDialog.setTitle("Dang luu...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id_nv", "" + timestamp);
        hashMap.put("ten", "" + ten);
        hashMap.put("vai_tro", "" + vaitrostring);
        hashMap.put("email", "" + emailstring);
        hashMap.put("sdt", "" + sdtstring);
        hashMap.put("trang_thai", "" + trangthai);
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


}
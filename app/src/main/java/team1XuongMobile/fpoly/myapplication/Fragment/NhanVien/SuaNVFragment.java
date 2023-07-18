package team1XuongMobile.fpoly.myapplication.Fragment.NhanVien;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.R;


public class SuaNVFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String idNV = "";

    public static final String KEY_ID_NHAN_VIEN = "idNV";


    AppCompatButton suahoantat;
    ImageButton backsua;
    EditText suatennhanvien;
    EditText suaemail;
    EditText suasdt;
    Spinner suavaitro;
    RadioButton suadanglam, suadanghi, suatamnghi;

    String suaten = "", suaemailstring = "", suavaitrostring = "", suasdtstring = "", trangthai = "",khstring = "";


    public SuaNVFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sua_n_v, container, false);
        suatennhanvien = view.findViewById(R.id.ed_suanhanvien_tennhanvien);
        suaemail = view.findViewById(R.id.ed_suanhanvien_email);
        suasdt = view.findViewById(R.id.ed_suanhanvien_sdtnhanvien);
        suavaitro = view.findViewById(R.id.spinner_suanhanvien_vaitronhanvien);
        suadanglam = view.findViewById(R.id.rabtn_suanhanvien_danglam);
        suadanghi = view.findViewById(R.id.rabtn_suanhanvien_danghi);
        suatamnghi = view.findViewById(R.id.rabtn_suanhanvien_tamnghi);
        suahoantat = view.findViewById(R.id.btn_suanhanvien_hoantat);
        backsua = view.findViewById(R.id.imgbt_suanhanvien_back);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        ArrayList<String> dataListspinner = new ArrayList<>();
        dataListspinner.add("Lao Công");
        dataListspinner.add("Bảo Vệ");
        dataListspinner.add("Nhân Viên Bốc Vác");
        dataListspinner.add("Nhân Viên Kiểm Kê");
        dataListspinner.add("Tổ Trưởng");
        dataListspinner.add("Tổ Phó");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dataListspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suavaitro.setAdapter(adapter);
        laydulieudangnhap();
        loadDataNVChuyenSang();
        setDataNVLenView();
        backsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        suahoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateNVDataInput();
            }
        });
        return view;
    }

    private void validateNVDataInput() {
        if (suatennhanvien.length() == 0) {
            suatennhanvien.requestFocus();
            suatennhanvien.setError("Không để trống phần tên nhân viên");
        } else if (suaemail.length() == 0) {
            suaemail.requestFocus();
            suaemail.setError("Không để trống phần email");
        } else if (suasdt.length() == 0) {
            suasdt.requestFocus();
            suasdt.setError("Không để trống phần số điện thoại");
        } else if (!suaemail.getText().toString().matches("^[A-Za-z0-9_.]{6,32}@([a-zA-Z0-9]{2,12}+.)([a-zA-Z]{2,12})+$")) {
            suaemail.requestFocus();
            suaemail.setError("Sai định dang email");
        } else if (!suasdt.getText().toString().matches("^[0-9]{9,10}")) {
            suasdt.requestFocus();
            suasdt.setError("Số điện thoại không hợp lệ");

        } else if (!TextUtils.isDigitsOnly(suasdt.getText().toString())) {
            suasdt.requestFocus();
            suasdt.setError("Số điện thoại phải là số");
        } else {
            luuDuLieuNhanVienLenFirebase();
        }
    }

    private void luuDuLieuNhanVienLenFirebase() {
        suaten = suatennhanvien.getText().toString().trim();
        suaemailstring = suaemail.getText().toString().trim();
        suasdtstring = suasdt.getText().toString().trim();
        suavaitrostring = (String) suavaitro.getSelectedItem();

        if (suadanglam.isChecked()) {
            trangthai = "Đang Làm";
        } else if (suadanghi.isChecked()) {
            trangthai = "Đã Nghỉ";
        } else if (suatamnghi.isChecked()) {
            trangthai = "Tạm Nghỉ";
        }
        progressDialog.setTitle("Dang lưu...");
        progressDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", "" + suaten);
        hashMap.put("vaiTro", "" + suavaitrostring);
        hashMap.put("email", "" + suaemailstring);
        hashMap.put("sdt", "" + suasdtstring);
        hashMap.put("trang_thai", "" + trangthai);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.child("" + idNV)
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void loadDataNVChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idNV = bundle.getString(KEY_ID_NHAN_VIEN);
        }
    }
    public void laydulieudangnhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                khstring = ""+snapshot.child("kh").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setDataNVLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.child(idNV)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        suaten = "" + snapshot.child("username").getValue();
                        suaemailstring = "" + snapshot.child("email").getValue();
                        suasdtstring = "" + snapshot.child("sdt").getValue();
                        suavaitrostring = "" + snapshot.child("vaiTro").getValue();
                        trangthai = "" + snapshot.child("trang_thai").getValue();

                        suatennhanvien.setText(suaten);
                        suaemail.setText(suaemailstring);
                        suasdt.setText(suasdtstring);


                        if (suavaitrostring.equals("Lao Công")) {
                            suavaitro.setSelection(0);
                        } else if (suavaitrostring.equals("Bảo Vệ")) {
                            suavaitro.setSelection(1);
                        } else if (suavaitrostring.equals("Nhân Viên Bốc Vác")) {
                            suavaitro.setSelection(2);
                        } else if (suavaitrostring.equals("Nhân Viên Kiểm Kê")) {
                            suavaitro.setSelection(3);
                        } else if (suavaitrostring.equals("Tổ Trưởng")) {
                            suavaitro.setSelection(4);
                        } else if (suavaitrostring.equals("Tổ Phó")) {
                            suavaitro.setSelection(5);
                        }

                        if (trangthai.equals("Đang Làm")) {
                            suadanglam.setChecked(true);
                        } else if (trangthai.equals("Đã Nghỉ")) {
                            suadanghi.setChecked(true);
                        } else if (trangthai.equals("Tạm Nghỉ")) {
                            suatamnghi.setChecked(true);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
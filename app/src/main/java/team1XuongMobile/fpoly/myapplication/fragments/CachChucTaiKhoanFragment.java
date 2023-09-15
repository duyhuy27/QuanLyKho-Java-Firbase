package team1XuongMobile.fpoly.myapplication.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

import team1XuongMobile.fpoly.myapplication.R;


public class CachChucTaiKhoanFragment extends Fragment {
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    String id = "";


    public static final String KEY_ID_TAI_KHOAN = "id";

    AppCompatButton cachchuc;
    ImageButton quaylai;
    TextView tentk, emailtk, sdttk;
    String tentkstring = "", emailtkstring = "", sdttkstring = "", vaitrostring = "", idNVstring = "";
    ;
    Spinner suavaitro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cach_chuc_tai_khoan, container, false);
        tentk = view.findViewById(R.id.tv_cachchucfragment_tentaikhoan);
        emailtk = view.findViewById(R.id.tv_cachchucfragment_emailtaikhoan);
        sdttk = view.findViewById(R.id.tv_cachchucfragment_sdttaikhoan);
        suavaitro = view.findViewById(R.id.spinner_cachchucfragment_vaitro);
        cachchuc = view.findViewById(R.id.btn_cachchucfragment_cachchuc);
        quaylai = view.findViewById(R.id.imgbt_cachchucfragment_back);
        firebaseAuth = FirebaseAuth.getInstance();
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
        loadDataTKChuyenSang();
        setDataNVLenView();
        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        cachchuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luuDuLieuNVLenFirebase();
                luuDuLieuTKLenFirebase();
            }
        });

        return view;

    }

    private void luuDuLieuTKLenFirebase() {
        vaitrostring = (String) suavaitro.getSelectedItem();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Đang Thực Thi");
        progressDialog.show();


        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("vaiTro", "" + vaitrostring);
        hashMap.put("trangThai", "Ngưng Hoạt Động");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child("" + id)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();

                        Toast.makeText(getContext(), "Cách Chức Thành Công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Cách Chức Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void luuDuLieuNVLenFirebase() {
        vaitrostring = (String) suavaitro.getSelectedItem();


        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("vaiTro", "" + vaitrostring);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.child("" + idNVstring)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void loadDataTKChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString(KEY_ID_TAI_KHOAN);
            Log.d("quanquan", "id_tk " + id);

        }
    }

    private void setDataNVLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(id)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("quanquan", "id_tk1 " + id);
                        tentkstring = "" + snapshot.child("username").getValue();
                        emailtkstring = "" + snapshot.child("email").getValue();
                        sdttkstring = "" + snapshot.child("sdt").getValue();
                        idNVstring = "" + snapshot.child("idNV").getValue();

                        tentk.setText(tentkstring);
                        emailtk.setText(emailtkstring);
                        sdttk.setText(sdttkstring);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
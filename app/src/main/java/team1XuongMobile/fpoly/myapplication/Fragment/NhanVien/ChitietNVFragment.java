package team1XuongMobile.fpoly.myapplication.Fragment.NhanVien;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import team1XuongMobile.fpoly.myapplication.R;


public class ChitietNVFragment extends Fragment {
    TextView ten, email, sdt, vaitro, trangthai;
    Button quaylai;
    String tenstring = "", emailstring = "", sdtstring = "", vaitrostring = "", trangthaistring = "", idNV = "";
    public static final String KEY_ID_NHAN_VIEN = "idNV";


    public ChitietNVFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chitiet_n_v, container, false);
        ten = view.findViewById(R.id.tv_chitietnhanvien_tennhanvien);
        email = view.findViewById(R.id.tv_chitietnhanvien_emailnhanvien);
        sdt = view.findViewById(R.id.tv_chitietnhanvien_sdtnhanvien);
        vaitro = view.findViewById(R.id.tv_chitietnhanvien_vaitronhanvien);
        trangthai = view.findViewById(R.id.tv_chitietnhanvien_trangthainhanvien);
        quaylai = view.findViewById(R.id.btn_chitietnhanvien_back);
        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        loadDataNVChuyenSang();
        setDataNVLenView();

        return view;
    }

    private void loadDataNVChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idNV = bundle.getString(KEY_ID_NHAN_VIEN);
            Log.e("quanquan", "id nhan duoc: " + idNV);
        }
    }

    private void setDataNVLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhan_vien");
        ref.child(idNV)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tenstring = "" + snapshot.child("ten").getValue();
                        emailstring = "" + snapshot.child("email").getValue();
                        sdtstring = "" + snapshot.child("sdt").getValue();
                        vaitrostring = "" + snapshot.child("vai_tro").getValue();
                        trangthaistring = "" + snapshot.child("trang_thai").getValue();
                        if(trangthaistring.equalsIgnoreCase("Đang Làm")){
                            trangthai.setTextColor(Color.GREEN);
                            trangthai.setText(trangthaistring);
                        }
                        else if(trangthaistring.equalsIgnoreCase("Đã Nghỉ")){
                            trangthai.setTextColor(Color.RED);
                            trangthai.setText(trangthaistring);
                        }
                        else if(trangthaistring.equalsIgnoreCase("Tạm Nghỉ")){
                            trangthai.setTextColor(Color.YELLOW);
                            trangthai.setText(trangthaistring);
                        }


                        ten.setText(tenstring);
                        email.setText(emailstring);
                        sdt.setText(sdtstring);
                        vaitro.setText(vaitrostring);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
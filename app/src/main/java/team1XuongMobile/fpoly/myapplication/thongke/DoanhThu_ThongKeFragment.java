package team1XuongMobile.fpoly.myapplication.thongke;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import team1XuongMobile.fpoly.myapplication.R;


public class DoanhThu_ThongKeFragment extends Fragment {
    AppCompatButton btn_tungay, btn_denngay, btn_doangthu;
    TextView tv_tungay, tv_denngay, tv_tiennhap, tv_tienxuat;
    long tiennhap = 0;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String khstring = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doanh_thu__thong_ke, container, false);
        btn_tungay = view.findViewById(R.id.btn_doanhthufragment_tungay);
        btn_denngay = view.findViewById(R.id.btn_doanhthufragment_denngay);
        btn_doangthu = view.findViewById(R.id.btn_doanhthufragment_doanhthu);
        tv_tungay = view.findViewById(R.id.tv_doanhthufragment_tungay);
        tv_denngay = view.findViewById(R.id.tv_doanhthufragment_denngay);
        tv_tiennhap = view.findViewById(R.id.tv_doanhthufragment_tiennhap);
        tv_tienxuat = view.findViewById(R.id.tv_doanhthufragment_tienxuat);
        firebaseAuth = FirebaseAuth.getInstance();
        laydulieudangnhap();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        final int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        btn_tungay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        tv_tungay.setText(date);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btn_denngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        tv_denngay.setText(date);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btn_doangthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tiennhap.setText((int) SoTienNhap(tv_tungay.getText().toString(), tv_denngay.getText().toString()));

            }
        });
    }

    public long SoTienNhap(String tungay, String denngay) {

        firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference refnhap = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        refnhap.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refnhap.orderByChild("kh").equalTo(khstring).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        refnhap.orderByChild("ngayNhap").startAt(tungay)
                                .endAt(denngay).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getValue() != null) {
                                            for (DataSnapshot childnhap : snapshot.getChildren()) {
                                                tiennhap += snapshot.child("tong_tien").getValue(long.class);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return tiennhap;

    }

    public void SoTienXuat() {

    }

    public void laydulieudangnhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                khstring = "" + snapshot.child("kh").getValue();
                Log.d("quanquan", "kh " + khstring);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
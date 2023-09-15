package team1XuongMobile.fpoly.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;


public class ThongTinChiTietKhachHangVFragment extends Fragment {
    private TextView tv_tongsodon , tv_sanpham;
    private String id_khls;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    public ThongTinChiTietKhachHangVFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_tin_chi_tiet_khach_hang_v, container, false);

        tv_tongsodon = view.findViewById(R.id.tv_tongsodon);
        tv_sanpham = view.findViewById(R.id.tv_sphaymua_ls);
        demlsdon();

        return view;
    }
    private void demlsdon(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        ArrayList<String> tenSpList = new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LuuIdKh", Context.MODE_PRIVATE);
        id_khls = sharedPreferences.getString("idkh", null);
        Log.e("idlskh","id nhận dc :"+id_khls);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        databaseReference.orderByChild("id_kh").equalTo(id_khls).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Lấy số lượng các tài liệu con trong snapshot
                long count = snapshot.getChildrenCount();
                tv_tongsodon.setText(String.valueOf(count));


                //Duyệt qua các tài liệu con trong snapshot
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    //Lấy giá trị của trường "tenSp" từ mỗi tài liệu
                    String tenSp = dataSnapshot1.child("tenSp").getValue(String.class);
                    //Thêm giá trị này vào danh sách tenSpList
                    if (!tenSpList.contains(tenSp)) {
                        //Nếu chưa có, thêm giá trị này vào danh sách tenSpList
                        tenSpList.add(tenSp);
                    }
                }
                if(tenSpList.size()==0){
                    return;
                }else {
                    //In ra danh sách tenSpList để kiểm tra kết quả
                    Log.d("tenSpList", tenSpList.toString());

                    //Tạo một biến String để lưu trữ các tenSp
                    String tenSpText = "";

                    //Duyệt qua danh sách tenSpList
                    for (String tenSp : tenSpList) {
                        //Nối các tenSp với nhau bằng dấu phẩy và dấu cách
                        tenSpText += "- " +  tenSp + "\n";
                    }

                    //Loại bỏ dấu phẩy và dấu cách cuối cùng
                    tenSpText = tenSpText.substring(0, tenSpText.length() - 1);

                    //Đặt giá trị của biến String cho TextView
                    tv_sanpham.setText(tenSpText);
                    tv_sanpham.setLineSpacing(0f, 1.2f);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
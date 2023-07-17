package team1XuongMobile.fpoly.myapplication.Fragment.KhachHang;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.R;


public class ChiTietKhachHangFragment extends Fragment {
    private TabLayout tabLayout;

    private ViewPager2 viewPager;
    private ChiTietKhachHangViewPageAdapter chiTietKhachHangViewPageAdapter;
    TextView ten_kh ,sdt_kh , email_kh,diachi_kh;
    ImageView img_sdt , img_email , img_diachi;
    String id_kh ="", tenkh_String ="",sdtkh_String ="",diachikh_String ="",emailkh_String ="";

    public static final String KEY_ID_KHACH_HANG = "id_kh_bd";


    public ChiTietKhachHangFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_khach_hang, container, false);

        tabLayout = view.findViewById(R.id.tab_layout_chitietkh);
        viewPager = view.findViewById(R.id.view_pager2_chitietkh);

        chiTietKhachHangViewPageAdapter = new ChiTietKhachHangViewPageAdapter(ChiTietKhachHangFragment.this);
        viewPager.setAdapter(chiTietKhachHangViewPageAdapter);

        ten_kh = view.findViewById(R.id.tv_tenkh_chitietkh);
        sdt_kh = view.findViewById(R.id.tv_sdtkh__chitietkh);
        email_kh = view.findViewById(R.id.tv_emailkh_chitietkh);
        diachi_kh = view.findViewById(R.id.tv_diachikh_chitietkh);

        img_diachi = view.findViewById(R.id.img_diachikh_chitietkh);
        img_email = view.findViewById(R.id.img_emailkh_chitietkh);
        img_sdt = view.findViewById(R.id.img_phone_in_talk_chitietkh);

        img_sdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("số điện thoại : "+sdtkh_String );
                builder.setTitle("Bạn có muốn gọi đến " + tenkh_String);
                builder.setCancelable(false);
                builder.setPositiveButton("gọi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri uri = Uri.parse("tel:" + sdtkh_String);
                        intent.setData(uri);
                        v.getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        loadDataNVChuyenSang();
        setDataKhachHangLenView();

        new TabLayoutMediator(tabLayout,viewPager,(tab, position) ->{
            switch (position){
                case 0:
                    tab.setText("Thông Tin");
                    break;
                case 1:
                    tab.setText("Lịch Sử");
                    break;

            }
        }).attach();

        return view;
    }
    private void loadDataNVChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_kh = bundle.getString(KEY_ID_KHACH_HANG);
            Log.e("zzzzzz", "id nhan duoc: " + id_kh);
        }
    }

    private void setDataKhachHangLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("khach_hang");
        ref.child(id_kh)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tenkh_String = "" + snapshot.child("ten_kh").getValue();
                        sdtkh_String = "" + snapshot.child("sdt_kh").getValue();
                        emailkh_String = "" + snapshot.child("email_kh").getValue();
                        diachikh_String = "" + snapshot.child("diachi_kh").getValue();

                        ten_kh.setText(tenkh_String);
                        sdt_kh.setText(sdtkh_String);
                        email_kh.setText(emailkh_String);
                        diachi_kh.setText(diachikh_String);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
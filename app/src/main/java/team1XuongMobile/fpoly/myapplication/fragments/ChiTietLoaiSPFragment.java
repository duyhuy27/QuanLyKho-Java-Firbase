package team1XuongMobile.fpoly.myapplication.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.R;


public class ChiTietLoaiSPFragment extends Fragment {
    TextView id_lsp ,ten_lsp , trangthai_lsp;
    AppCompatButton quaylai2;
    ImageButton quaylai1;
    String tenlspString = "", trangthaistring = "", idlsp = "", idlspString ="";
    boolean trangthai;
    public static final String KEY_ID_LOAI_SAN_PHAM = "id_lsp_bd";

    public ChiTietLoaiSPFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_loai_s_p, container, false);

        id_lsp = view.findViewById(R.id.tv_chitietloaisp_idloaisp);
        ten_lsp = view.findViewById(R.id.tv_sdtkh__chitietkh);

        trangthai_lsp = view.findViewById(R.id.tv_emailkh_chitietkh);
        quaylai1 = view.findViewById(R.id.btn_quay_lai_chitiet_lsp);
        quaylai2 = view.findViewById(R.id.btn_chitietloaisp_quaylai);

        quaylai1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        quaylai2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        loadDataNVChuyenSang();
        setDataLoaiSPLenView();
        return view;
    }

    private void loadDataNVChuyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idlsp = bundle.getString(KEY_ID_LOAI_SAN_PHAM);
            Log.e("zzzzzz", "id nhan duoc: " + idlsp);
        }
    }

    private void setDataLoaiSPLenView() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loai_sp");
        ref.child(idlsp)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tenlspString = "" + snapshot.child("ten_loai_sp").getValue();
                        idlspString = "" + snapshot.child("id_loai_sp").getValue();
                        trangthai = Boolean.parseBoolean("" + snapshot.child("TrangThai").getValue());
                        if(trangthai == true){
                            trangthai_lsp.setTextColor(Color.GREEN);
                            trangthai_lsp.setText("đang hoạt động");
                        }
                        else {
                            trangthai_lsp.setTextColor(Color.RED);
                            trangthai_lsp.setText("dừng hoạt động");
                        }

                        ten_lsp.setText(tenlspString);
                        id_lsp.setText(idlspString);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
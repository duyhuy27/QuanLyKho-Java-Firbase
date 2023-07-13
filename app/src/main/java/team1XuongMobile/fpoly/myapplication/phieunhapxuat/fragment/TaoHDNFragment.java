package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonSanPhamAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;

public class TaoHDNFragment extends Fragment {
    public TextView chonSanPham, chonNgayNhap, nhaCungCap;
    protected RecyclerView rcvDialog;
    private ChonSanPhamAdapter adapter;
    private ArrayList<ChonSanPham> list;
    private LinearLayout linerChonSp, linearChonSpThanhCong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_h_d_n, container, false);

        chonSanPham = view.findViewById(R.id.tv_chonSanPhamNhap);
        chonNgayNhap = view.findViewById(R.id.tvChonNgayNhap);
        nhaCungCap = view.findViewById(R.id.tvNhaCungCap);
        linerChonSp = view.findViewById(R.id.linearChonSP);
        linearChonSpThanhCong = view.findViewById(R.id.linearChonSpThanhCong);

        adapter = new ChonSanPhamAdapter(requireContext(), linerChonSp, linearChonSpThanhCong);
        list = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            nhaCungCap.setText(bundle.getString("title"));
        }

        chonSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                View viewDialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_chon_san_pham, null);
                builder.setView(viewDialog);

                rcvDialog = viewDialog.findViewById(R.id.rcv_chonSanPham);
                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                rcvDialog.setLayoutManager(layoutManager);

                loadDataFirebase();

                AlertDialog dialog = builder.create();
                dialog.show();
               
            }
        });
        chonNgayNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickChonNgayNhap();
            }
        });
        return view;
    }

    public void onclickChonNgayNhap() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                chonNgayNhap.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void loadDataFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChonSanPham objChonSanPham = dataSnapshot.getValue(ChonSanPham.class);
                    list.add(objChonSanPham);
                }
                adapter.setData(list);
                rcvDialog.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
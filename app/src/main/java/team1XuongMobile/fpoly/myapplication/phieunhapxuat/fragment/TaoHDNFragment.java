package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.ChonSanPhamAdapter;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;

public class TaoHDNFragment extends Fragment implements ChonSanPhamAdapter.ClickChonSp {
    public TextView chonSanPham, chonNgayNhap, nhaCungCap, tvTenSpHDN, tvMaSpHDN, tvSoTienSpHDN;
    protected RecyclerView rcvDialog;
    private ChonSanPhamAdapter adapter;
    private ArrayList<ChonSanPham> list;
    private LinearLayout linerChonSp, linearChonSpThanhCong;
    private AlertDialog dialog;
    private String idNCC, tenNCC;
    private ChonSanPhamAdapter.ClickChonSp listener;

    private String idSanPha, tenSanPham, giaSanPham;
    private ArrayList<String> idSpArray, tenSpArray, giaSpArray;
    private String chonIdSanPham, chonTenSanPham, chonGiaSanPham;

    public static final String TAG = "TaoHDNFRAGMENTTAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_h_d_n, container, false);

        chonSanPham = view.findViewById(R.id.tv_chonSanPhamNhap);
        chonNgayNhap = view.findViewById(R.id.tvChonNgayNhap);
        nhaCungCap = view.findViewById(R.id.tvNhaCungCap);
        linerChonSp = view.findViewById(R.id.linearChonSP);
        linearChonSpThanhCong = view.findViewById(R.id.linearChonSpThanhCong);
        tvTenSpHDN = view.findViewById(R.id.tvTenspHDN);
        tvMaSpHDN = view.findViewById(R.id.tvMaSpHDN);
        tvSoTienSpHDN = view.findViewById(R.id.tvSoTienSpHDN);

        listener = this;

        adapter = new ChonSanPhamAdapter(requireContext(), linerChonSp, linearChonSpThanhCong, this, listener);
        list = new ArrayList<>();

        bundleChoNCCAdapter();
//        bundleChonSpAdapter();
        loadDataFirebase();

        chonSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//                View viewDialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_chon_san_pham, null);
//                builder.setView(viewDialog);
//
//                rcvDialog = viewDialog.findViewById(R.id.rcv_chonSanPham);
//                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
//                rcvDialog.setLayoutManager(layoutManager);
//
//                loadDataFirebase();
//
//                dialog = builder.create();
//                dialog.show();
                showDialogChonSp();
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
        idSpArray = new ArrayList<>();
        tenSpArray = new ArrayList<>();
        giaSpArray = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idSpArray.clear();
                tenSpArray.clear();
                giaSpArray.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    ChonSanPham objChonSanPham = dataSnapshot.getValue(ChonSanPham.class);
//                    list.add(objChonSanPham);
                    idSanPha = "" + dataSnapshot.child("idSanPham").getValue();
                    tenSanPham = "" + dataSnapshot.child("tenSp").getValue();
                    giaSanPham = "" + dataSnapshot.child("giaNhap").getValue();

                    idSpArray.add(idSanPha);
                    tenSpArray.add(tenSanPham);
                    giaSpArray.add(giaSanPham);

                }
//                adapter.setData(list);
//                rcvDialog.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialogChonSp() {
        String[] tenSpArr = new String[tenSpArray.size()];
        for (int i = 0; i < tenSpArray.size(); i++) {
            tenSpArr[i] = tenSpArray.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(tenSpArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chonIdSanPham = idSpArray.get(which);
                chonGiaSanPham = giaSpArray.get(which);
                chonTenSanPham = tenSpArray.get(which);

                Log.d(TAG, "onClick: id gia ten " + chonTenSanPham + chonGiaSanPham + chonIdSanPham);
            }
        }).show();
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void bundleChoNCCAdapter() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tenNCC = bundle.getString("title");
            idNCC = bundle.getString("idNCC");
            nhaCungCap.setText(tenNCC);
        }
    }

    public void bundleChonSpAdapter() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tvTenSpHDN.setText(bundle.getString("tenSpHDN"));
            Log.d("haihuy262", "test " + bundle.getString("tenSpHDN"));
        }
    }

    @Override
    public void clickChon(String tenSp, String maSp, String giaSp, TaoHDNFragment fragment, LinearLayout linearChonSp, LinearLayout linearChonSpThanhCong) {
        if (fragment != null) {
            fragment.dismissDialog();
        }
        int visibility = linearChonSp.getVisibility();
        if (visibility == View.VISIBLE) {
            linearChonSp.setVisibility(View.GONE);
            linearChonSpThanhCong.setVisibility(View.VISIBLE);
        }
        Bundle bundle = new Bundle();
        bundle.putString("tenSpHDN", tenSp);
        bundle.putString("maSpHDN", maSp);
        bundle.putString("soTienHDN", giaSp);
        TaoHDNFragment taoHDNFragment = new TaoHDNFragment();
        taoHDNFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, new TaoHDNFragment())
                .addToBackStack(null)
                .commit();
    }
}
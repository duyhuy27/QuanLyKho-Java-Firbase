package team1XuongMobile.fpoly.myapplication.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentChiTietSPBinding;
import team1XuongMobile.fpoly.myapplication.adapter.ThuocTinhAdapter;
import team1XuongMobile.fpoly.myapplication.models.ThuocTinhModels;


public class ChiTietSPFragment extends Fragment implements ThuocTinhAdapter.thuocTinhInterface {

    private FragmentChiTietSPBinding binding;

    private String idSp = "";

    public static final String TAG = "ChiTietSPFragment";

    private ArrayList<ThuocTinhModels> thuocTinhModelsArrayList;

    private ThuocTinhAdapter thuocTinhAdapter;

    private ThuocTinhAdapter.thuocTinhInterface listener;

    private ArrayList<String> skuArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChiTietSPBinding.inflate(inflater, container, false);
        listener = this;

        layDuLieuTruyenSang();

        loadDuLieuDuaTrenId();

        loadDuLieuThuocTinh();

        binding.buttonHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

    public static final String KEY_ID_SP = "id";

    private void layDuLieuTruyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idSp = bundle.getString(KEY_ID_SP);
            Log.d(TAG, "layDuLieuTruyenSang: id truyền sang " + idSp);
        }

    }

    private void loadDuLieuDuaTrenId() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SanPham");
        databaseReference.child(idSp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String giaBan = "" + snapshot.child("giaBan").getValue();
                        String giaNhap = "" + snapshot.child("giaNhap").getValue();
                        String id_loai = "" + snapshot.child("id_loai").getValue();
                        String id_nha_cc = "" + snapshot.child("id_nha_cc").getValue();
                        String img = "" + snapshot.child("img").getValue();
                        String khoiLuong = "" + snapshot.child("khoiLuong").getValue();
                        String maSp = "" + snapshot.child("maSp").getValue();
                        String tenSp = "" + snapshot.child("tenSp").getValue();
                        String ten_loai = "" + snapshot.child("ten_loai").getValue();
                        String ten_nha_cc = "" + snapshot.child("ten_nha_cc").getValue();
                        String thueAvailable = "" + snapshot.child("thueAvailable").getValue();
                        String thueDauRa = "" + snapshot.child("thueDauRa").getValue();
                        String thueDauVao = "" + snapshot.child("thueDauVao").getValue();
                        String trangThaiAvailable = "" + snapshot.child("trangThaiAvailable").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String mota = "" + snapshot.child("mota").getValue();

                        try {
                            Picasso.get().load(img).placeholder(R.drawable.ic_add).error(R.drawable.ic_add)
                                    .into(binding.cardPickerCamera);
                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: Không thể load ảnh " + e.getMessage());
                        }

                        //set datta to UI
                        binding.tvTenSp.setText(tenSp);
                        binding.tvMaSp.setText(maSp);
                        binding.tvKhoiLuong.setText(khoiLuong + " g");

                        if (thueAvailable.equals("true")) {
                            binding.tvThueDaura.setText(thueDauRa + " %");
                            binding.tvThueDauvao.setText(thueDauVao + " %");
                        } else {
                            binding.tvThueDaura.setVisibility(View.GONE);
                            binding.tvThueDauvao.setVisibility(View.GONE);
                        }

                        binding.tvGiaban.setText(giaBan + " VND");
                        binding.tvGiagoc.setText(giaNhap + " VND");

                        binding.tvNcc.setText("NCC : " + ten_nha_cc);
                        binding.tvLsp.setText("Loại : " + ten_loai);

                        binding.tvMota.setText("" + mota);

                        if (trangThaiAvailable.equals("true")) {
                            binding.buttonSwitchTrangthai.setImageResource(R.drawable.ic_status);
                        } else {
                            binding.buttonSwitchTrangthai.setImageResource(R.drawable.ic_status_red);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadDuLieuThuocTinh() {
        thuocTinhModelsArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("attributes");
        databaseReference.orderByChild("product_id").equalTo(idSp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        thuocTinhModelsArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ThuocTinhModels thuocTinhModels = ds.getValue(ThuocTinhModels.class);
                            thuocTinhModelsArrayList.add(thuocTinhModels);
                        }
                        thuocTinhAdapter = new ThuocTinhAdapter(getContext(), thuocTinhModelsArrayList, listener);
                        binding.rcvThuoctinhSp.setAdapter(thuocTinhAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void xoaThuocTinh(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bạn không thể xóa sản phẩm ở đây, hãy chỉnh sửa ở trong màn hình sửa sản phẩm ");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
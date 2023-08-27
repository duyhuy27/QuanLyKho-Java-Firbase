package team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.ChiTietHDNFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuNhap;

public class PhieuNhapAdapter extends RecyclerView.Adapter<PhieuNhapAdapter.ViewHolder> {
    private final Context context;
    private PhieuNhapInterface listener;
    public ArrayList<PhieuNhap> phieuNhapArrayList, list;
    String uid, kh, tennhanvientao = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public PhieuNhapAdapter(Context context, PhieuNhapInterface listener, ArrayList<PhieuNhap> phieuNhapArrayList) {
        this.context = context;
        this.listener = listener;
        this.phieuNhapArrayList = phieuNhapArrayList;
        this.list = phieuNhapArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phieu_nhap, parent, false);
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    public interface PhieuNhapInterface {
        void ChitietPN(String idPN);

        void SuaPN(String idPN);

        void LichsuPN(String idPN);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        layDuLieuDangNhap();
        PhieuNhap objPhieuNhap = phieuNhapArrayList.get(position);
        if (objPhieuNhap == null) {
            return;
        }
        holder.tvMaHoaDon.setText(objPhieuNhap.getId_phieu_nhap());
        holder.tvNhanVienTao.setText(objPhieuNhap.getTen_nhan_vien());
        holder.tvLoaiHoaDon.setText("Nhập");
        holder.tvNgay.setText(objPhieuNhap.getNgayNhap());
        holder.tvTongGia.setText(objPhieuNhap.getTong_tien_hang());

        if (objPhieuNhap.isTrangThai()) {
            holder.tvTrangThai.setText("Đã thanh toán");
            holder.tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.tvTrangThai.setText("Chưa thanh toán");
            holder.tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.mau_do));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("idPhieuNhap", objPhieuNhap.getId_phieu_nhap());
                ChiTietHDNFragment fragment = new ChiTietHDNFragment();
                fragment.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.imgLuaChon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.menu_phieunhap, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionPN = new MenuPopupHelper(context, menuBuilder, v);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
//                        if (item.getItemId() == R.id.popup_menuPN_chitiet) {
//                            listener.ChitietPN(objPhieuNhap.getId_phieu_nhap());
//                            return true;
//                        } 
                        if (item.getItemId() == R.id.popup_menuPN_lichsu) {
                            listener.LichsuPN(objPhieuNhap.getId_phieu_nhap());
                            return true;
                        } else {
                            return false;
                        }

                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                optionPN.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        if (phieuNhapArrayList != null) {
            return phieuNhapArrayList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHoaDon, tvLoaiHoaDon, tvNhanVienTao, tvNgay, tvTongGia, tvTrangThai;
        ImageView imgLuaChon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHoaDon = itemView.findViewById(R.id.tvMaHoaDon);
            tvLoaiHoaDon = itemView.findViewById(R.id.tvLoaiHoaDon);
            tvNhanVienTao = itemView.findViewById(R.id.tvNhanVienTao);
            tvNgay = itemView.findViewById(R.id.tvNgay);
            tvTongGia = itemView.findViewById(R.id.tvTongGia);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThaiPhieuNhap);
            imgLuaChon = itemView.findViewById(R.id.imgLuaChon);
        }
    }

    private void layDuLieuDangNhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kh = String.valueOf(snapshot.child("kh").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

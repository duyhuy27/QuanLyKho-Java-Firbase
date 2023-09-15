package team1XuongMobile.fpoly.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;

import team1XuongMobile.fpoly.myapplication.models.PhieuXuat;

public class LichSuCtKhachHangAdapter extends RecyclerView.Adapter<LichSuCtKhachHangAdapter.ViewHolder>{
     Context context;
     ArrayList<PhieuXuat> list;

    public LichSuCtKhachHangAdapter(Context context, ArrayList<PhieuXuat> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lichsu_khachhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhieuXuat objPhieuXuat = list.get(position);
        if (objPhieuXuat == null) {
            return;
        }
        holder.tvMaHoaDonXuat.setText(objPhieuXuat.getId_phieu_xuat());
        holder.tvNgayXuat.setText(objPhieuXuat.getNgay_xuat());
        holder.tvTenKhachHang.setText(objPhieuXuat.getTen_kh());
        holder.tvTongGia.setText(objPhieuXuat.getTong_tien_hang());
    }



    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHoaDonXuat, tvTenKhachHang, tvNhanVienTao, tvNgayXuat, tvTongGia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHoaDonXuat = itemView.findViewById(R.id.tvMaHoaDonX_ls);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang_ls);
            tvNhanVienTao = itemView.findViewById(R.id.tvNhanVienTaoX_ls);
            tvNgayXuat = itemView.findViewById(R.id.tvNgayX_ls);
            tvTongGia = itemView.findViewById(R.id.tvTongGiaX_ls);
        }
    }
}

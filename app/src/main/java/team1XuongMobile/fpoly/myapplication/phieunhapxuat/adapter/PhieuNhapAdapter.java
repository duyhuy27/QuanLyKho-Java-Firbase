package team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuNhap;

public class PhieuNhapAdapter extends RecyclerView.Adapter<PhieuNhapAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<PhieuNhap> list;

    public PhieuNhapAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<PhieuNhap> list) {
        this.list = list;
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phieu_nhap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhieuNhap objPhieuNhap = list.get(position);
        if (objPhieuNhap == null) {
            return;
        }
        holder.tvMaHoaDon.setText(objPhieuNhap.getId_phieu_nhap());
        holder.tvLoaiHoaDon.setText("Nhập");
        holder.tvNgay.setText(objPhieuNhap.getNgayNhap());
        holder.tvTongGia.setText(objPhieuNhap.getTong_tien_hang());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
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
}

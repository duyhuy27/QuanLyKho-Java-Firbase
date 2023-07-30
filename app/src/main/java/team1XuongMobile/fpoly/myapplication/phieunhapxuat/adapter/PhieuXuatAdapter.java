package team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuXuat;

public class PhieuXuatAdapter extends RecyclerView.Adapter<PhieuXuatAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<PhieuXuat> list;

    public PhieuXuatAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<PhieuXuat> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phieu_xuat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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
            tvMaHoaDonXuat = itemView.findViewById(R.id.tvMaHoaDonX);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvNhanVienTao = itemView.findViewById(R.id.tvNhanVienTaoX);
            tvNgayXuat = itemView.findViewById(R.id.tvNgayX);
            tvTongGia = itemView.findViewById(R.id.tvTongGiaX);
        }
    }
}

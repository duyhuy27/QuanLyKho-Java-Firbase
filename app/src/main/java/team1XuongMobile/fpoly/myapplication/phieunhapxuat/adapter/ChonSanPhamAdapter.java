package team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.TaoHDNFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;

public class ChonSanPhamAdapter extends RecyclerView.Adapter<ChonSanPhamAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<ChonSanPham> list;
    private LinearLayout linearChonSp;
    private LinearLayout linearChonSpThanhCong;
    private TaoHDNFragment fragment;
    private ClickChonSp clickChonSp;

    public ChonSanPhamAdapter(Context context, LinearLayout linearChonSp, LinearLayout linearChonSpThanhCong, TaoHDNFragment fragment, ClickChonSp clickChonSp) {
        this.context = context;
        this.linearChonSp = linearChonSp;
        this.linearChonSpThanhCong = linearChonSpThanhCong;
        this.fragment = fragment;
        this.clickChonSp = clickChonSp;
    }

    public void setData(ArrayList<ChonSanPham> list) {
        this.list = list;
        notifyItemInserted(0);
    }

    public interface ClickChonSp {
        void clickChon(String tenSp, String maSp, String giaSp, TaoHDNFragment fragment, LinearLayout linearChonSp, LinearLayout linearChonSpThanhCong);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chon_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChonSanPham objChonSanPham = list.get(position);
        if (objChonSanPham == null) {
            return;
        }
        holder.tvChonSanPham.setText(objChonSanPham.getTenSp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChonSp.clickChon(objChonSanPham.getTenSp(), objChonSanPham.getMaSp(), objChonSanPham.getGiaNhap(), fragment, linearChonSp, linearChonSpThanhCong);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvChonSanPham;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChonSanPham = itemView.findViewById(R.id.tv_chonSanPham);
        }
    }
}

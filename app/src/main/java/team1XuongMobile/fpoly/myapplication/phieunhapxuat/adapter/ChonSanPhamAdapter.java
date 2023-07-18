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
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPhamListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;

public class ChonSanPhamAdapter extends RecyclerView.Adapter<ChonSanPhamAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<ChonSanPham> list;
    private ChonSanPhamListener listener;

    public ChonSanPhamAdapter(Context context, ChonSanPhamListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(ArrayList<ChonSanPham> list) {
        this.list = list;
        notifyItemInserted(0);
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
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ChonSanPham objChonSanPham = list.get(position);
                    listener.onClickChonSanPham(objChonSanPham);
                }
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

package team1XuongMobile.fpoly.myapplication.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.interfaced.ChonNhaCungCapListener;
import team1XuongMobile.fpoly.myapplication.models.ChonNCC;

public class ChonNCCAdapter extends RecyclerView.Adapter<ChonNCCAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<ChonNCC> chonNCCArrayList;
    private ChonNhaCungCapListener listener;

    public ChonNCCAdapter(Context context, ChonNhaCungCapListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(ArrayList<ChonNCC> chonNCCArrayList) {
        this.chonNCCArrayList = chonNCCArrayList;
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chon_nha_cung_cap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChonNCC objChonNCC = chonNCCArrayList.get(position);
        if (objChonNCC == null) {
            return;
        }
        holder.tvChonTenNcc.setText(objChonNCC.getTen_nha_cc());
        holder.tvChonSDTNCC.setText(objChonNCC.getSo_dien_dienthoai());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ChonNCC objChonNCC = chonNCCArrayList.get(position);
                    listener.onClickChonNhaCungCap(objChonNCC);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (chonNCCArrayList != null) {
            return chonNCCArrayList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageButton imgBtnLuaChon;
        protected TextView tvChonTenNcc, tvChonSDTNCC;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtnLuaChon = itemView.findViewById(R.id.imgButton_luaChon);
            tvChonTenNcc = itemView.findViewById(R.id.tv_chonTenNCC);
            tvChonSDTNCC = itemView.findViewById(R.id.tv_chonSdtNCC);
        }
    }
}

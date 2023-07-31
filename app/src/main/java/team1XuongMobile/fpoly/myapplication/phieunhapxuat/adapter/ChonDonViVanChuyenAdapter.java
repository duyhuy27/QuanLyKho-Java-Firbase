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
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenModel;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonDonViVanChuyenListener;

public class ChonDonViVanChuyenAdapter extends RecyclerView.Adapter<ChonDonViVanChuyenAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<VanChuyenModel> list;
    private ChonDonViVanChuyenListener listener;

    public ChonDonViVanChuyenAdapter(Context context, ChonDonViVanChuyenListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(ArrayList<VanChuyenModel> list) {
        this.list = list;
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chon_don_vi_van_chuyen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VanChuyenModel objVanChuyenModel = list.get(position);
        if (objVanChuyenModel == null) {
            return;
        }
        holder.tvTenDonViVanChuyen.setText(objVanChuyenModel.getTen());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickChonDonViVanChuyen(objVanChuyenModel);
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
        TextView tvTenDonViVanChuyen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDonViVanChuyen = itemView.findViewById(R.id.tvChonDonViVanChuyen);
        }
    }
}

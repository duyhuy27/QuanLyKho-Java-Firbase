package team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Model.KhachHang;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonKhachHangListener;

public class ChonKhachHangAdapter extends RecyclerView.Adapter<ChonKhachHangAdapter.ViewHolder> {
    private ArrayList<KhachHang> list;
    private final Context context;
    private ChonKhachHangListener listener;

    public ChonKhachHangAdapter(Context context, ChonKhachHangListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(ArrayList<KhachHang> list) {
        this.list = list;
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chon_khach_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhachHang objKhachHang = list.get(position);
        if (objKhachHang == null) {
            return;
        }
        holder.tvKhachHang.setText(objKhachHang.getTen_kh());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickChonKhachHang(objKhachHang);
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
        TextView tvKhachHang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKhachHang = itemView.findViewById(R.id.tv_chonKhachHang);
        }
    }
}

package team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.ChonSanPhamFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.TaoHDNFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;

public class ChonSanPhamAdapter extends RecyclerView.Adapter<ChonSanPhamAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<ChonSanPham> list;

    public ChonSanPhamAdapter(Context context) {
        this.context = context;
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
                Bundle bundleChonSp = new Bundle();
                bundleChonSp.putString("tenSp", objChonSanPham.getTenSp());
                bundleChonSp.putString("maSp", objChonSanPham.getMaSp());
                bundleChonSp.putString("soTienSp", objChonSanPham.getGiaNhap());

                TaoHDNFragment fragment = new TaoHDNFragment();
                fragment.setArguments(bundleChonSp);

                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

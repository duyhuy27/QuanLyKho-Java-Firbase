package team1XuongMobile.fpoly.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.Model.QuanLyTaiKhoan;
import team1XuongMobile.fpoly.myapplication.R;

public class QuanLyTaiKhoanAdapter extends RecyclerView.Adapter<QuanLyTaiKhoanAdapter.ViewHolder> {
    private Context context;
    public ArrayList<NhanVien> nhanVienArrayList;


    public QuanLyTaiKhoanAdapter(Context context, ArrayList<NhanVien> nhanVienArrayList) {
        this.context = context;
        this.nhanVienArrayList = nhanVienArrayList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenNhanVien, email, sdt;
        ImageView showluuchon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenNhanVien = itemView.findViewById(R.id.tv_itemquanlytaikhoan_tennhanvien);
            email = itemView.findViewById(R.id.tv_itemquanlytaikhoan_email);
            sdt = itemView.findViewById(R.id.tv_itemquanlytaikhoan_sdt);
            showluuchon = itemView.findViewById(R.id.imgv_itemquanlytaikhoan_showluuchon);

        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quanlytaikhoan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tenNhanVien.setText(nhanVienArrayList.get(position).getTen());
        holder.email.setText(nhanVienArrayList.get(position).getEmail());
        Log.d("adapte", "onBindViewHolder: "+nhanVienArrayList.get(position).getEmail());
        holder.sdt.setText(nhanVienArrayList.get(position).getSdt());

    }

    @Override
    public int getItemCount() {
        return nhanVienArrayList.size();
    }


}

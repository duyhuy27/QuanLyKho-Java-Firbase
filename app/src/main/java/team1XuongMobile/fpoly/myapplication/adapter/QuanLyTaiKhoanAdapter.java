package team1XuongMobile.fpoly.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.filter.FilterSearchTaiKhoan;
import team1XuongMobile.fpoly.myapplication.models.QuanLyTaiKhoan;
import team1XuongMobile.fpoly.myapplication.R;

public class QuanLyTaiKhoanAdapter extends RecyclerView.Adapter<QuanLyTaiKhoanAdapter.ViewHolder> implements Filterable {
    private Context context;
    public ArrayList<QuanLyTaiKhoan> quanLyTaiKhoanArrayList, list;
    FilterSearchTaiKhoan filterSearchTaiKhoan;
    TaikhoanInterface taikhoanInterface;
    FirebaseAuth firebaseAuth;



    public QuanLyTaiKhoanAdapter(Context context, ArrayList<QuanLyTaiKhoan> quanLyTaiKhoanArrayList, TaikhoanInterface taikhoanInterface) {
        this.context = context;
        this.quanLyTaiKhoanArrayList = quanLyTaiKhoanArrayList;
        this.taikhoanInterface = taikhoanInterface;
        this.list = quanLyTaiKhoanArrayList;
    }

    public interface TaikhoanInterface {
        void CachChucTKClick(String id);

        void ChiTietTKClick(String id);

        void DatLaiMatKhauTKClick(String id);


    }

    @Override
    public Filter getFilter() {

        if (filterSearchTaiKhoan == null) {
            filterSearchTaiKhoan = new FilterSearchTaiKhoan(list, this);
        }
        return filterSearchTaiKhoan;
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
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        QuanLyTaiKhoan quanLyTaiKhoan = quanLyTaiKhoanArrayList.get(position);

        holder.tenNhanVien.setText(quanLyTaiKhoanArrayList.get(position).getUsername());
        holder.email.setText(quanLyTaiKhoanArrayList.get(position).getEmail());
        holder.sdt.setText("**********");

        holder.showluuchon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.popup_menu_quanlytaikhoan, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionTK = new MenuPopupHelper(context, menuBuilder, v);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        if (item.getItemId() == R.id.popup_menuQTLK_cachchuc) {
                            taikhoanInterface.CachChucTKClick(quanLyTaiKhoan.getUid());
                            return true;
                        } else if (item.getItemId() == R.id.popup_menuQTLK_datlaimatkhau) {
                            taikhoanInterface.DatLaiMatKhauTKClick(quanLyTaiKhoan.getUid());
                            return true;
                        } else if (item.getItemId() == R.id.popup_menuQTLK_chitiet) {
                            taikhoanInterface.ChiTietTKClick(quanLyTaiKhoan.getUid());
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                optionTK.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return quanLyTaiKhoanArrayList.size();
    }



}

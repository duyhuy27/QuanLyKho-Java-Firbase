package team1XuongMobile.fpoly.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
    public ArrayList<QuanLyTaiKhoan> quanLyTaiKhoanArrayList;
    private QltkInterface qltkInterface;

    public QuanLyTaiKhoanAdapter(Context context, ArrayList<QuanLyTaiKhoan> quanLyTaiKhoanArrayList,
                                 QltkInterface qltkInterface) {
        this.context = context;
        this.quanLyTaiKhoanArrayList = quanLyTaiKhoanArrayList;
        this.qltkInterface = qltkInterface;
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

    public interface QltkInterface {
        void updateQLTKlick(String id);

        void deleteQLTKlick(String id);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quanlytaikhoan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuanLyTaiKhoan quanLyTaiKhoan = quanLyTaiKhoanArrayList.get(position);
        holder.tenNhanVien.setText(quanLyTaiKhoanArrayList.get(position).getTennhanvien());
        holder.email.setText(quanLyTaiKhoanArrayList.get(position).getEmail());
        holder.sdt.setText(quanLyTaiKhoanArrayList.get(position).getSdt());
        holder.showluuchon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.popup_menu_quanlytaikhoan, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionQLTK = new MenuPopupHelper(context, menuBuilder, v);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        if (item.getItemId() == R.id.popup_menuQLTK_sua) {
                            qltkInterface.updateQLTKlick(quanLyTaiKhoan.getId_qltk());
                            return true;
                        } else if (item.getItemId() == R.id.popup_menuQTLK_xoa) {
                            qltkInterface.deleteQLTKlick(quanLyTaiKhoan.getId_qltk());
                            return true;
                        } else {
                            return false;
                        }

                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                optionQLTK.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return quanLyTaiKhoanArrayList.size();
    }


}

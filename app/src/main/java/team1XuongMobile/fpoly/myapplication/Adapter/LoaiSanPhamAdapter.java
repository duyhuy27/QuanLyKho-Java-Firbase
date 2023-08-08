package team1XuongMobile.fpoly.myapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPham.ChiTietLoaiSPFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPham.FilterSearchLoaiSanPham;
import team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPham.ThemLoaiSanPhamFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.ChitietNVFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVien.FilterSearchNhanVien;
import team1XuongMobile.fpoly.myapplication.Model.LoaiSanPham;
import team1XuongMobile.fpoly.myapplication.R;

public class LoaiSanPhamAdapter extends RecyclerView.Adapter<LoaiSanPhamAdapter.ViewHolder> implements Filterable {
    private Context context;
    public ArrayList<LoaiSanPham> loaiSanPhamArrayList, filterlist;
    private ViewHolder.LoaiSanPhamInterface loaiSanPhamInterface;
    FilterSearchLoaiSanPham filterSearchLoaiSanPham;

    public static final String KEY_ID_LOAI_SAN_PHAM = "id_lsp_bd";


//    public LoaiSanPhamAdapter(Context context, ArrayList<LoaiSanPham> loaiSanPhamArrayList, ViewHolder.LoaiSanPhamInterface loaiSanPhamInterface) {
//        this.context = context;
//        this.loaiSanPhamArrayList = loaiSanPhamArrayList;
//        this.loaiSanPhamInterface = loaiSanPhamInterface;
//    }

    public LoaiSanPhamAdapter(Context context, ArrayList<LoaiSanPham> loaiSanPhamArrayList, ViewHolder.LoaiSanPhamInterface loaiSanPhamInterface) {
        this.context = context;
        this.loaiSanPhamArrayList = loaiSanPhamArrayList;
        this.filterlist = loaiSanPhamArrayList;
        this.loaiSanPhamInterface = loaiSanPhamInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loaisanpham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiSanPham loaiSanPham = loaiSanPhamArrayList.get(position);
        holder.tenloaisp.setText(loaiSanPham.getTen_loai_sp());

        holder.chitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();

                View view1 = inflater.inflate(R.layout.dialog_chuc_nang, null);
                builder.setView(view1);

                Button sua = view1.findViewById(R.id.button_sua);
                Button xoa = view1.findViewById(R.id.button_xoa);
                Button xemchitiet = view1.findViewById(R.id.button_xem_chi_tiet);

                AlertDialog alertDialog = builder.create();
//                alertDialog.setCancelable(false);
                alertDialog.show();

                sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        loaiSanPhamInterface.updateLoaiSPClick(loaiSanPham.getId_loai_sp());
                    }
                });

                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        loaiSanPhamInterface.deleteLoaiSPClick(loaiSanPham.getId_loai_sp());

                    }
                });

                xemchitiet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        loaiSanPhamInterface.chiTietLoaiSPClick(loaiSanPham.getId_loai_sp());

                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return loaiSanPhamArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterSearchLoaiSanPham == null){
            filterSearchLoaiSanPham = new FilterSearchLoaiSanPham(filterlist,this);
        }
        return filterSearchLoaiSanPham;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenloaisp;
        ImageView chitiet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenloaisp = itemView.findViewById(R.id.tv_loaisanpham_item_loaisanpham);
            chitiet = itemView.findViewById(R.id.imgv_chitiet_itemnhanvien);

        }

        public interface LoaiSanPhamInterface {
            void updateLoaiSPClick(String id);

            void deleteLoaiSPClick(String id);

            void chiTietLoaiSPClick(String id);
        }
    }
}



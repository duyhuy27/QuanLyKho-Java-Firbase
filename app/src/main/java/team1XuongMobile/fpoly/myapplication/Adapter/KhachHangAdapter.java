package team1XuongMobile.fpoly.myapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Fragment.KhachHang.ChiTietKhachHangFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.KhachHang.FilterSearchKhachHang;
import team1XuongMobile.fpoly.myapplication.Fragment.KhachHang.LichSuChiTietKhachHangVFragment;
import team1XuongMobile.fpoly.myapplication.Model.KhachHang;
import team1XuongMobile.fpoly.myapplication.R;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> implements Filterable {
    private Context context;
    public ArrayList<KhachHang> khachHangArrayList, filterList;
    private ViewHolder.KhachHangInterface khachHangInterface;
    FilterSearchKhachHang filterSearchKhachHang;


    public KhachHangAdapter(Context context, ArrayList<KhachHang> khachHangArrayList, ViewHolder.KhachHangInterface khachHangInterface) {
        this.context = context;
        this.khachHangArrayList = khachHangArrayList;
        this.filterList = khachHangArrayList;
        this.khachHangInterface = khachHangInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khachhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhachHang khachHang = khachHangArrayList.get(position);
        holder.tv_ten_kh.setText(khachHang.getTen_kh());
        holder.tv_sdt_kh.setText(khachHang.getSdt_kh());
        holder.tv_diachi_kh.setText(khachHang.getDiachi_kh());

        holder.goi_kh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("số điện thoại : "+khachHang.getSdt_kh() );
                builder.setTitle("Bạn có muốn gọi đến " + khachHang.getTen_kh());
                builder.setCancelable(false);
                builder.setPositiveButton("gọi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri uri = Uri.parse("tel:" + khachHang.getSdt_kh());
                        intent.setData(uri);
                        v.getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.chitiet_kh.setOnClickListener(new View.OnClickListener() {
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
                alertDialog.show();

                sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        khachHangInterface.updateKhachHangClick(khachHang.getId_kh());
                    }
                });

                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        khachHangInterface.deleteKhachHangClick(khachHang.getId_kh());
                    }
                });

                xemchitiet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        khachHangInterface.chiTietKhachHangClick(khachHang.getId_kh());
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return khachHangArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filterSearchKhachHang ==null){
            filterSearchKhachHang = new FilterSearchKhachHang(filterList,this);
        }
        return filterSearchKhachHang;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ten_kh, tv_sdt_kh , tv_diachi_kh;
        ImageView chitiet_kh, goi_kh;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten_kh = itemView.findViewById(R.id.tv_tenkhachhang_item_khachhang);
            tv_sdt_kh = itemView.findViewById(R.id.tv_sdt_item_khachhang);
            tv_diachi_kh = itemView.findViewById(R.id.tv_diachi_item_khachhang);

            chitiet_kh = itemView.findViewById(R.id.imgv_chitiet_item_khachhang);
            goi_kh = itemView.findViewById(R.id.a2);
        }

        public interface KhachHangInterface {
            void updateKhachHangClick(String id);

            void deleteKhachHangClick(String id);

            void chiTietKhachHangClick(String id);
        }
    }
}

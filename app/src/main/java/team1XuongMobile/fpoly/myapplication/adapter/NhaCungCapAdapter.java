package team1XuongMobile.fpoly.myapplication.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.databinding.DialogChucNangBinding;
import team1XuongMobile.fpoly.myapplication.databinding.ItemNhacungcapBinding;
import team1XuongMobile.fpoly.myapplication.filter.FilterSearchNCC;
import team1XuongMobile.fpoly.myapplication.models.NhaCungCapModel;


public class NhaCungCapAdapter extends RecyclerView.Adapter<NhaCungCapAdapter.ViewHolder> implements Filterable {
    public ArrayList<NhaCungCapModel> nhaCungCapModelArrayList, list;
    private Context context;
    private chucNangInterfaceNhaCungCap chucNangInterfaceNhaCungCap;
    private FilterSearchNCC filterSearchNCC;


    public NhaCungCapAdapter(ArrayList<NhaCungCapModel> nhaCungCapModelArrayList, Context context, chucNangInterfaceNhaCungCap chucNangInterfaceNhaCungCap) {
        this.nhaCungCapModelArrayList = nhaCungCapModelArrayList;
        this.context = context;
        this.chucNangInterfaceNhaCungCap = chucNangInterfaceNhaCungCap;
        this.list = nhaCungCapModelArrayList;
    }

    @Override
    public Filter getFilter() {
        if (filterSearchNCC == null){

            filterSearchNCC = new FilterSearchNCC(list,this);
        }

        return filterSearchNCC;
    }

    public interface chucNangInterfaceNhaCungCap {
        void update(String id);
        void delete(String id);
        void xemChiTiet(String id);
        void goiClick(String sdt);
    }


    @NonNull
    @Override
    public NhaCungCapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNhacungcapBinding binding = ItemNhacungcapBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NhaCungCapAdapter.ViewHolder holder, int position) {
        NhaCungCapModel object = nhaCungCapModelArrayList.get(position);
        holder.binding.tvTenNCC.setText(object.getTen_nha_cc());
        holder.binding.tvSdtNCC.setText(object.getSo_dien_dienthoai());
        holder.binding.buttonLuaChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogChucNang(object,holder);
            }
        });
        holder.binding.tvSdtNCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chucNangInterfaceNhaCungCap.goiClick(object.getSo_dien_dienthoai());
            }
        });

    }
    private void openDialogChucNang(NhaCungCapModel object, NhaCungCapAdapter.ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        DialogChucNangBinding binding = DialogChucNangBinding.inflate(inflater);
        View view = binding.getRoot();
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        binding.buttonSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chucNangInterfaceNhaCungCap.update(object.getId_nha_cc());
                dialog.dismiss();

            }
        });
        binding.buttonXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chucNangInterfaceNhaCungCap.delete(object.getId_nha_cc());
                dialog.dismiss();
            }
        });
        binding.buttonXemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chucNangInterfaceNhaCungCap.xemChiTiet(object.getId_nha_cc());
                dialog.dismiss();

            }
        });
    }



    @Override
    public int getItemCount() {
        return nhaCungCapModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNhacungcapBinding binding;
        public ViewHolder(@NonNull ItemNhacungcapBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

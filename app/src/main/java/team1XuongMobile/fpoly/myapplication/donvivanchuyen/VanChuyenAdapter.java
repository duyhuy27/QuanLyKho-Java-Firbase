package team1XuongMobile.fpoly.myapplication.donvivanchuyen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.DialogChucNangBinding;
import team1XuongMobile.fpoly.myapplication.databinding.ItemVanchuyenBinding;

public class VanChuyenAdapter extends RecyclerView.Adapter<VanChuyenAdapter.ViewHolder> {
    private ArrayList<VanChuyenModel> vanChuyenModelArrayList;
    private Context context;
    private chucNangInterfaceVanChuyen chucNangInterfaceVanChuyen;

    public VanChuyenAdapter(ArrayList<VanChuyenModel> vanChuyenModelArrayList, Context context, VanChuyenAdapter.chucNangInterfaceVanChuyen chucNangInterfaceVanChuyen) {
        this.vanChuyenModelArrayList = vanChuyenModelArrayList;
        this.context = context;
        this.chucNangInterfaceVanChuyen = chucNangInterfaceVanChuyen;
    }

    public interface chucNangInterfaceVanChuyen {
        void update(String id);
        void delete(String id);
        void xemChiTiet(String id);
    }

    @NonNull
    @Override
    public VanChuyenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVanchuyenBinding binding = ItemVanchuyenBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VanChuyenAdapter.ViewHolder holder, int position) {
        VanChuyenModel object = vanChuyenModelArrayList.get(position);
        holder.binding.tvTenDVC.setText(object.getTen());
        holder.binding.tvSdtDVC.setText(object.getHotline());
        holder.binding.buttonLuaChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogChucNang(object,holder);
            }
        });

    }


    private  void  openDialogChucNang(VanChuyenModel object, ViewHolder holder) {
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
                chucNangInterfaceVanChuyen.update(object.getId_don_vi_vc());
                dialog.dismiss();

            }
        });
        binding.buttonXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chucNangInterfaceVanChuyen.delete(object.getId_don_vi_vc());
                dialog.dismiss();
            }
        });
        binding.buttonXemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chucNangInterfaceVanChuyen.xemChiTiet(object.getId_don_vi_vc());
                dialog.dismiss();

            }
        });
    }

    @Override
    public int getItemCount() {
        return vanChuyenModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         ItemVanchuyenBinding binding;

        public ViewHolder(@NonNull ItemVanchuyenBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

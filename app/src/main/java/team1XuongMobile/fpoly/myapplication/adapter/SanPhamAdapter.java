package team1XuongMobile.fpoly.myapplication.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.ItemSanPhamBinding;
import team1XuongMobile.fpoly.myapplication.filter.FilterSearchSanPham;
import team1XuongMobile.fpoly.myapplication.models.SanPhamModels;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> implements Filterable {

    public ArrayList<SanPhamModels> sanPhamModelsArrayList, list;

    private Context context;

    private sanPhamInterface listener;

    private FilterSearchSanPham filterSearchSanPham;

    public SanPhamAdapter(ArrayList<SanPhamModels> sanPhamModelsArrayList, Context context, sanPhamInterface listener) {
        this.sanPhamModelsArrayList = sanPhamModelsArrayList;
        this.list = sanPhamModelsArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SanPhamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSanPhamBinding binding = ItemSanPhamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public Filter getFilter() {
        if (filterSearchSanPham != null) {
            filterSearchSanPham = new FilterSearchSanPham(list, this);
        }
        return filterSearchSanPham;
    }

    public interface sanPhamInterface {
        void chiTietSpClick(String id);

        void xoaSpClick(String id);

        void suaSpClick(String id);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamAdapter.ViewHolder holder, int position) {
        SanPhamModels sanPhamModels = sanPhamModelsArrayList.get(position);
        String tenSp = sanPhamModels.getTenSp();
        String giaSp = sanPhamModels.getGiaBan();
        String npp = sanPhamModels.getTen_nha_cc();
        String img = sanPhamModels.getImg();

        try {
            Picasso.get().load(img).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera)
                    .into(holder.binding.imgSp);

        } catch (Exception e) {
            Toast.makeText(context, "Không thể load ảnh", Toast.LENGTH_SHORT).show();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String giaSpFormatted = decimalFormat.format(Double.parseDouble(giaSp));

        holder.binding.tvGiaSp.setText("Giá: " + giaSpFormatted + "VND");


        holder.binding.tvTenSp.setText(tenSp);
        holder.binding.tvCc.setText(npp);


        holder.binding.buttonChucNang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChucNang(holder, sanPhamModels);
            }
        });
    }

    private void showDialogChucNang(ViewHolder holder, SanPhamModels sanPhamModels) {
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_chuc_nang);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.AndroidAnimation;

        AppCompatButton buttonSua = dialog.findViewById(R.id.button_sua);
        AppCompatButton buttonXoa = dialog.findViewById(R.id.button_xoa);
        AppCompatButton buttonXemCt = dialog.findViewById(R.id.button_xem_chi_tiet);

        buttonSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.suaSpClick(sanPhamModels.getIdSanPham());
            }
        });

        buttonXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.xoaSpClick(sanPhamModels.getIdSanPham());
            }
        });

        buttonXemCt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.chiTietSpClick(sanPhamModels.getIdSanPham());
            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return sanPhamModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSanPhamBinding binding;

        public ViewHolder(@NonNull ItemSanPhamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

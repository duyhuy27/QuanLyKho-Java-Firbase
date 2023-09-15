package team1XuongMobile.fpoly.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.databinding.ItemThuocTinhSpBinding;
import team1XuongMobile.fpoly.myapplication.models.ThuocTinhModels;

public class ThuocTinhAdapter extends RecyclerView.Adapter<ThuocTinhAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ThuocTinhModels> thuocTinhModelsArrayList;

    private thuocTinhInterface thuocTinhInterface;

    public ThuocTinhAdapter(Context context, ArrayList<ThuocTinhModels> thuocTinhModelsArrayList, ThuocTinhAdapter.thuocTinhInterface thuocTinhInterface) {
        this.context = context;
        this.thuocTinhModelsArrayList = thuocTinhModelsArrayList;
        this.thuocTinhInterface = thuocTinhInterface;
    }

    public interface thuocTinhInterface{
        void xoaThuocTinh(String id);
    }

    @NonNull
    @Override
    public ThuocTinhAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemThuocTinhSpBinding binding = ItemThuocTinhSpBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuocTinhAdapter.ViewHolder holder, int position) {
        ThuocTinhModels thuocTinhModels = thuocTinhModelsArrayList.get(position);
        holder.binding.tvTenThuocTinh.setText(thuocTinhModels.getTen_tt());
        holder.binding.tvGiaTriThuocTinh.setText(thuocTinhModels.getGia_tri_tt());
        holder.binding.buttonXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thuocTinhInterface.xoaThuocTinh(thuocTinhModels.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return thuocTinhModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemThuocTinhSpBinding binding;

        public ViewHolder(@NonNull ItemThuocTinhSpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

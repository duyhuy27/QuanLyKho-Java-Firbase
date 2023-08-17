package team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuXuat;

public class PhieuXuatAdapter extends RecyclerView.Adapter<PhieuXuatAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<PhieuXuat> list;
    private ViewHolder.PhieuXuatInterface phieuXuatInterface;

    public PhieuXuatAdapter(Context context) {
        this.context = context;
    }

    public PhieuXuatAdapter(Context context, ArrayList<PhieuXuat> list, ViewHolder.PhieuXuatInterface phieuXuatInterface) {
        this.context = context;
        this.list = list;
        this.phieuXuatInterface = phieuXuatInterface;
    }

    public void setData(ArrayList<PhieuXuat> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phieu_xuat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhieuXuat objPhieuXuat = list.get(position);
        if (objPhieuXuat == null) {
            return;
        }
        holder.tvMaHoaDonXuat.setText(objPhieuXuat.getId_phieu_xuat());
        holder.tvNgayXuat.setText(objPhieuXuat.getNgay_xuat());
        holder.tvTenKhachHang.setText(objPhieuXuat.getTen_kh());
        holder.tvTongGia.setText(objPhieuXuat.getTong_tien_hang());

        holder.imgLuachon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();

                View view1 = inflater.inflate(R.layout.dialog_chuc_nang, null);
                builder.setView(view1);

                Button sua = view1.findViewById(R.id.button_sua);
                Button lichsu = view1.findViewById(R.id.button_xoa);
                Button xemchitiet = view1.findViewById(R.id.button_xem_chi_tiet);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        phieuXuatInterface.updatePXClick(objPhieuXuat.getId_phieu_xuat());
                    }
                });
                lichsu.setText("lịch sử");
                lichsu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        phieuXuatInterface.LichSuPXClick(objPhieuXuat.getId_phieu_xuat());
                    }
                });
                xemchitiet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        phieuXuatInterface.chiTietPXClick(objPhieuXuat.getId_phieu_xuat());
                    }
                });
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
        TextView tvMaHoaDonXuat, tvTenKhachHang, tvNhanVienTao, tvNgayXuat, tvTongGia;
        ImageView imgLuachon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHoaDonXuat = itemView.findViewById(R.id.tvMaHoaDonX);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvNhanVienTao = itemView.findViewById(R.id.tvNhanVienTaoX);
            tvNgayXuat = itemView.findViewById(R.id.tvNgayX);
            tvTongGia = itemView.findViewById(R.id.tvTongGiaX);

            imgLuachon = itemView.findViewById(R.id.imgLuaChonX);
        }

        public interface PhieuXuatInterface {
            void updatePXClick(String id);

            void LichSuPXClick(String id);

            void chiTietPXClick(String id);
        }

    }

}

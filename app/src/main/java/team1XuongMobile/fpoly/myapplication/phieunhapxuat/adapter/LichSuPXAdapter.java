package team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.NotifyXuat;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuXuat;

public class LichSuPXAdapter extends RecyclerView.Adapter<LichSuPXAdapter.ViewHolder>{
    Context context;
    private ArrayList<NotifyXuat> list;

    public LichSuPXAdapter(Context context, ArrayList<NotifyXuat> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ls_phieu_xuat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotifyXuat notifyXuat = list.get(position);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.orderByChild("kh").equalTo(notifyXuat.getKh()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy ra tên của các phieu_xuat trùng kh
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String nvtaoString = "" + child.child("username").getValue();
                        holder.tenNhanVien.setText(nvtaoString);

                        // Làm gì đó với tên
                    }
                } else {
                    // Không có phieu_xuat nào trùng kh
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
        holder.ngay.setText(notifyXuat.getNgay_xuat());
        holder.kieuthongbao.setText(notifyXuat.getHinhthuc());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenNhanVien, ngay, kieuthongbao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenNhanVien = itemView.findViewById(R.id.tv_nhanvantao);
            ngay = itemView.findViewById(R.id.tv_ngay_lspx);
            kieuthongbao = itemView.findViewById(R.id.tv_hinhthuc);
        }



    }
}

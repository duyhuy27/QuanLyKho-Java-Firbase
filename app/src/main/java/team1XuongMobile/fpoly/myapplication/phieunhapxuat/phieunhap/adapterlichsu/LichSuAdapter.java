package team1XuongMobile.fpoly.myapplication.phieunhapxuat.phieunhap.adapterlichsu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.LichSuPhieuNhan;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.phieunhap.FilterSearchLichSu;

public class LichSuAdapter extends RecyclerView.Adapter<LichSuAdapter.ViewHolder> implements Filterable {
    Context context;
    public ArrayList<LichSuPhieuNhan> lichSuPhieuNhanArrayList, list;
    FilterSearchLichSu filterSearchLichSu;


    public LichSuAdapter(Context context, ArrayList<LichSuPhieuNhan> lichSuPhieuNhanArrayList) {
        this.context = context;
        this.lichSuPhieuNhanArrayList = lichSuPhieuNhanArrayList;
        this.list = lichSuPhieuNhanArrayList;
    }

    @Override
    public Filter getFilter() {
        if (filterSearchLichSu == null) {
            filterSearchLichSu = new FilterSearchLichSu(list, this);
        }
        return filterSearchLichSu;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenNhanVien, ngay, kieuthongbao;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenNhanVien = itemView.findViewById(R.id.tv_lichsupn_itemtennhanvien);
            ngay = itemView.findViewById(R.id.tv_lichsupn_itemngay);
            kieuthongbao = itemView.findViewById(R.id.tv_lichsupn_itemloaithongbao);


        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lich_su_pn, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichSuPhieuNhan lichSuPhieuNhan = lichSuPhieuNhanArrayList.get(position);
        holder.tenNhanVien.setText(lichSuPhieuNhan.getTen_nhan_vien());
        holder.ngay.setText(lichSuPhieuNhan.getNgay_them_sua());
        holder.kieuthongbao.setText(lichSuPhieuNhan.getLoai_thong_bao());


    }

    @Override
    public int getItemCount() {
        if (lichSuPhieuNhanArrayList != null) {
            return lichSuPhieuNhanArrayList.size();
        }
        return 0;
    }


}

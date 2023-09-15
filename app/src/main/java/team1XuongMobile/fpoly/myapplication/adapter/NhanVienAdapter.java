package team1XuongMobile.fpoly.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.filter.FilterSearchNhanVien;
import team1XuongMobile.fpoly.myapplication.models.NhanVien;
import team1XuongMobile.fpoly.myapplication.R;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> implements Filterable {
    private Context context;
    public ArrayList<NhanVien> nhanVienArrayList, list;
    private nhanvienInterface listener;
    FilterSearchNhanVien filterSearchNhanVien;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String vaitrostring;

    public NhanVienAdapter(Context context, ArrayList<NhanVien> nhanVienArrayList, nhanvienInterface listener) {
        this.context = context;
        this.nhanVienArrayList = nhanVienArrayList;
        this.listener = listener;
        this.list = nhanVienArrayList;
    }

    @Override
    public Filter getFilter() {
        if (filterSearchNhanVien == null) {
            filterSearchNhanVien = new FilterSearchNhanVien(list, this);
        }
        return filterSearchNhanVien;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenNhanVien, sdt;
        ImageView chitiet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenNhanVien = itemView.findViewById(R.id.tv_tennhanvien_itemnhanvien);
            sdt = itemView.findViewById(R.id.tv_sdt_itemnhanvien);
            chitiet = itemView.findViewById(R.id.imgv_chitiet_itemnhanvien);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, parent, false);
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    public interface nhanvienInterface {
        void updateNVClick(String id);

        void deleteNVClick(String id);

        void chiTietNVClick(String id);

        void bonhiemNVClick(String id);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NhanVien nhanVien = nhanVienArrayList.get(position);
        holder.tenNhanVien.setText(nhanVienArrayList.get(position).getUsername());
        holder.sdt.setText(nhanVienArrayList.get(position).getSdt());
        holder.sdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + list.get(position).getSdt()));
                v.getContext().startActivity(intent);
            }
        });
        holder.chitiet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.popup_menu_nhanvien, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionNV = new MenuPopupHelper(context, menuBuilder, v);
                firebaseUser = firebaseAuth.getCurrentUser();
                String uid = firebaseUser.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
                ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        vaitrostring = "" + snapshot.child("vaiTro").getValue();
                        if (vaitrostring.equals("nhanVien") == true) {
                            menuBuilder.setCallback(new MenuBuilder.Callback() {
                                @Override
                                public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                                    if (item.getItemId() == R.id.popup_menuNV_chitiet) {
                                        listener.chiTietNVClick(nhanVien.getId());
                                        return true;
                                    } else if (item.getItemId() == R.id.popup_menuNV_sua) {
                                        listener.updateNVClick(nhanVien.getId());
                                        return true;
                                    } else if (item.getItemId() == R.id.popup_menuNV_xoa) {
                                        Toast.makeText(context, "Bạn Không Đủ Thẩm Quyền Để Xóa", Toast.LENGTH_SHORT).show();
                                        return true;
                                    } else if (item.getItemId() == R.id.popup_menuNV_bonhiem) {
                                        Toast.makeText(context, "Bạn Không Đủ Thẩm Quyền Để Bổ Nhiệm", Toast.LENGTH_SHORT).show();
                                        return true;
                                    } else {
                                        return false;
                                    }

                                }

                                @Override
                                public void onMenuModeChange(@NonNull MenuBuilder menu) {

                                }
                            });
                            optionNV.show();
                        } else {
                            menuBuilder.setCallback(new MenuBuilder.Callback() {
                                @Override
                                public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                                    if (item.getItemId() == R.id.popup_menuNV_chitiet) {
                                        listener.chiTietNVClick(nhanVien.getId());
                                        return true;
                                    } else if (item.getItemId() == R.id.popup_menuNV_sua) {
                                        listener.updateNVClick(nhanVien.getId());
                                        return true;
                                    } else if (item.getItemId() == R.id.popup_menuNV_xoa) {
                                        listener.deleteNVClick(nhanVien.getId());
                                        return true;
                                    } else if (item.getItemId() == R.id.popup_menuNV_bonhiem) {
                                        listener.bonhiemNVClick(nhanVien.getId());
                                        return true;
                                    } else {
                                        return false;
                                    }

                                }

                                @Override
                                public void onMenuModeChange(@NonNull MenuBuilder menu) {

                                }
                            });
                            optionNV.show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return nhanVienArrayList.size();
    }




}

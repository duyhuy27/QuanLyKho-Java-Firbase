package team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPham;

import android.widget.Filter;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Adapter.LoaiSanPhamAdapter;
import team1XuongMobile.fpoly.myapplication.Model.LoaiSanPham;

public class FilterSearchLoaiSanPham extends Filter {
    ArrayList<LoaiSanPham> loaiSanPhamArrayList;
    LoaiSanPhamAdapter loaiSanPhamAdapter;

    public FilterSearchLoaiSanPham(ArrayList<LoaiSanPham> loaiSanPhamArrayList, LoaiSanPhamAdapter loaiSanPhamAdapter) {
        this.loaiSanPhamArrayList = loaiSanPhamArrayList;
        this.loaiSanPhamAdapter = loaiSanPhamAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if(constraint != null && constraint.length()>0){
            constraint = constraint.toString().toUpperCase().trim();
            ArrayList<LoaiSanPham> establishes = new ArrayList<>();
            for (int i = 0 ; i<loaiSanPhamArrayList.size(); i++){
                if(loaiSanPhamArrayList.get(i).getTen_loai_sp().toUpperCase().contains(constraint)){
                    establishes.add(loaiSanPhamArrayList.get(i));
                }
            }
            filterResults.count = establishes.size();
            filterResults.values = establishes;
        }else {
            filterResults.count = loaiSanPhamArrayList.size();
            filterResults.values = loaiSanPhamArrayList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        loaiSanPhamAdapter.loaiSanPhamArrayList = (ArrayList<LoaiSanPham>) results.values;
        loaiSanPhamAdapter.notifyDataSetChanged();
    }
}

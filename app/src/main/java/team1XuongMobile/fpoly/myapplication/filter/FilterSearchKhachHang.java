package team1XuongMobile.fpoly.myapplication.filter;

import android.widget.Filter;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.adapter.KhachHangAdapter;
import team1XuongMobile.fpoly.myapplication.models.KhachHang;

public class FilterSearchKhachHang extends Filter {
    ArrayList<KhachHang> khachHangArrayList;
    KhachHangAdapter khachHangAdapter;

    public FilterSearchKhachHang(ArrayList<KhachHang> khachHangArrayList, KhachHangAdapter khachHangAdapter) {
        this.khachHangArrayList = khachHangArrayList;
        this.khachHangAdapter = khachHangAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if(constraint != null && constraint.length()>0){
            constraint = constraint.toString().toUpperCase().trim();
            ArrayList<KhachHang> establishes = new ArrayList<>();
            for (int i = 0 ; i<khachHangArrayList.size(); i++){
                if(khachHangArrayList.get(i).getTen_kh().toUpperCase().contains(constraint)){
                    establishes.add(khachHangArrayList.get(i));
                }
            }
            filterResults.count = establishes.size();
            filterResults.values = establishes;
        }else {
            filterResults.count = khachHangArrayList.size();
            filterResults.values = khachHangArrayList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        khachHangAdapter.khachHangArrayList = (ArrayList<KhachHang>) results.values;
        khachHangAdapter.notifyDataSetChanged();
    }
}

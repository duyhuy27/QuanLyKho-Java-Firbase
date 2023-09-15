package team1XuongMobile.fpoly.myapplication.filter;

import android.widget.Filter;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.adapter.QuanLyTaiKhoanAdapter;
import team1XuongMobile.fpoly.myapplication.models.QuanLyTaiKhoan;

public class FilterSearchTaiKhoan extends Filter {
    ArrayList<QuanLyTaiKhoan> list;
    QuanLyTaiKhoanAdapter quanLyTaiKhoanAdapter;

    public FilterSearchTaiKhoan(ArrayList<QuanLyTaiKhoan> list, QuanLyTaiKhoanAdapter quanLyTaiKhoanAdapter) {
        this.list = list;
        this.quanLyTaiKhoanAdapter = quanLyTaiKhoanAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase().trim();
            ArrayList<QuanLyTaiKhoan> establishes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().toUpperCase().contains(constraint)) {
                    establishes.add(list.get(i));
                }
            }

            filterResults.count = establishes.size();
            filterResults.values = establishes;
        } else {
            filterResults.count = list.size();
            filterResults.values = list;
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        quanLyTaiKhoanAdapter.quanLyTaiKhoanArrayList = (ArrayList<QuanLyTaiKhoan>) results.values;
        quanLyTaiKhoanAdapter.notifyDataSetChanged();


    }
}

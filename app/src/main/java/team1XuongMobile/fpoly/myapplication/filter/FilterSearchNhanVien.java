package team1XuongMobile.fpoly.myapplication.filter;

import android.widget.Filter;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.adapter.NhanVienAdapter;
import team1XuongMobile.fpoly.myapplication.models.NhanVien;

public class FilterSearchNhanVien extends Filter {

    ArrayList<NhanVien> list;
    NhanVienAdapter nhanVienAdapter;

    public FilterSearchNhanVien(ArrayList<NhanVien> list, NhanVienAdapter adapter) {
        this.list = list;
        this.nhanVienAdapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if (charSequence != null && charSequence.length() > 0) {
            charSequence = charSequence.toString().toUpperCase().trim();
            ArrayList<NhanVien> establishes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().toUpperCase().contains(charSequence)) {
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
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        nhanVienAdapter.nhanVienArrayList = (ArrayList<NhanVien>) filterResults.values;
        nhanVienAdapter.notifyDataSetChanged();
    }

}

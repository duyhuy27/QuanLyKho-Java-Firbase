package team1XuongMobile.fpoly.myapplication.sanpham;

import android.widget.Filter;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.nhacungcap.NhaCungCapModel;

public class FilterSearchSanPham extends Filter {
    ArrayList<SanPhamModels> list;
    SanPhamAdapter adapter;

    public FilterSearchSanPham(ArrayList<SanPhamModels> list, SanPhamAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if (charSequence != null && charSequence.length() > 0) {
            charSequence = charSequence.toString().toUpperCase().trim();
            ArrayList<SanPhamModels> establishes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTenSp().toUpperCase().contains(charSequence)) {
                    establishes.add(list.get(i));
                } else if (list.get(i).getMaSp().toUpperCase().contains(charSequence)) {
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
        adapter.sanPhamModelsArrayList = (ArrayList<SanPhamModels>) filterResults.values;
        adapter.notifyDataSetChanged();
    }
}

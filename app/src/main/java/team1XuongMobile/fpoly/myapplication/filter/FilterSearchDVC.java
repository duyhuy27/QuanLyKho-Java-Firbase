package team1XuongMobile.fpoly.myapplication.filter;

import android.widget.Filter;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.adapter.VanChuyenAdapter;
import team1XuongMobile.fpoly.myapplication.models.VanChuyenModel;


public class FilterSearchDVC extends Filter {
    private ArrayList<VanChuyenModel> list;
    VanChuyenAdapter adapter;

    public FilterSearchDVC(ArrayList<VanChuyenModel> list, VanChuyenAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if (charSequence != null && charSequence.length() > 0) {
            charSequence = charSequence.toString().toUpperCase().trim();
            ArrayList<VanChuyenModel> establishes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTen().toUpperCase().contains(charSequence)) {
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
        adapter.vanChuyenModelArrayList = (ArrayList<VanChuyenModel>) filterResults.values;
        adapter.notifyDataSetChanged();

    }
}

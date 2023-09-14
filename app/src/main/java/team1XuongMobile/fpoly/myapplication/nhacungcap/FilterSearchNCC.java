package team1XuongMobile.fpoly.myapplication.nhacungcap;

import android.widget.Filter;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Model.NhanVien;


public class FilterSearchNCC extends Filter {
    private ArrayList<NhaCungCapModel> list;

    public NhaCungCapAdapter adapter;

    public FilterSearchNCC(ArrayList<NhaCungCapModel> list, NhaCungCapAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if (charSequence != null && charSequence.length() > 0) {
            charSequence = charSequence.toString().toUpperCase().trim();
            ArrayList<NhaCungCapModel> establishes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTen_nha_cc().toUpperCase().contains(charSequence)) {
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
        adapter.nhaCungCapModelArrayList = (ArrayList<NhaCungCapModel>) filterResults.values;
        adapter.notifyDataSetChanged();

    }
}

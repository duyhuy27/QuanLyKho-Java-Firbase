package team1XuongMobile.fpoly.myapplication.phieunhapxuat.phieunhap;

import android.widget.Filter;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.LichSuPhieuNhan;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.phieunhap.adapterlichsu.LichSuAdapter;

public class FilterSearchLichSu extends Filter {
    ArrayList<LichSuPhieuNhan> list;
    LichSuAdapter lichSuAdapter;

    public FilterSearchLichSu(ArrayList<LichSuPhieuNhan> list, LichSuAdapter lichSuAdapter) {
        this.list = list;
        this.lichSuAdapter = lichSuAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase().trim();
            ArrayList<LichSuPhieuNhan> establishes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTen_nhan_vien().toUpperCase().contains(constraint)) {
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
        lichSuAdapter.lichSuPhieuNhanArrayList = (ArrayList<LichSuPhieuNhan>) results.values;
        lichSuAdapter.notifyDataSetChanged();

    }
}

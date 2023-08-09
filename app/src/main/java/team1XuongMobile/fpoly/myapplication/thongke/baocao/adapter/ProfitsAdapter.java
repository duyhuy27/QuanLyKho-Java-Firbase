package team1XuongMobile.fpoly.myapplication.thongke.baocao.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.databinding.ItemLoiNhuanBinding;
import team1XuongMobile.fpoly.myapplication.thongke.baocao.model.DateRangeProfit;

public class ProfitsAdapter extends RecyclerView.Adapter<ProfitsAdapter.ViewHolder> {
    private List<DateRangeProfit> dateRangeProfitsList;

    public ProfitsAdapter(List<DateRangeProfit> dateRangeProfitsList) {
        this.dateRangeProfitsList = dateRangeProfitsList;
    }

    @NonNull
    @Override
    public ProfitsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLoiNhuanBinding binding = ItemLoiNhuanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfitsAdapter.ViewHolder holder, int position) {
        DateRangeProfit dateRangeStats = dateRangeProfitsList.get(position);
        holder.binding.tvDate.setText(dateRangeStats.getDate());
        // Format profit in Vietnamese currency format
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedProfit = currencyFormatter.format(dateRangeStats.getProfit());

        holder.binding.tvMoney.setText(formattedProfit);

    }

    @Override
    public int getItemCount() {
        return dateRangeProfitsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLoiNhuanBinding binding;

        public ViewHolder(@NonNull ItemLoiNhuanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

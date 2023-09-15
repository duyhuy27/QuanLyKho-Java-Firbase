package team1XuongMobile.fpoly.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.databinding.ItemDoanhThuBinding;
import team1XuongMobile.fpoly.myapplication.models.DateRangeStats;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

    private List<DateRangeStats> dayStatsList;

    public StatsAdapter(List<DateRangeStats> dayStatsList) {
        this.dayStatsList = dayStatsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDoanhThuBinding binding = ItemDoanhThuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateRangeStats dayStats = dayStatsList.get(position);

        double totalValue = dayStats.getTotalValue();

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalValue = currencyFormatter.format(totalValue);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateStr = dayStats.getDate();
        String invoiceCount = String.valueOf(dayStats.getTotalInvoiceCount());

        holder.binding.tvMoney.setText(formattedTotalValue);

        holder.binding.tvDate.setText(dateStr);

        holder.binding.tvSoLuongDonHang.setText(invoiceCount);
    }

    @Override
    public int getItemCount() {
        return dayStatsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDoanhThuBinding binding;

        public ViewHolder(@NonNull ItemDoanhThuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

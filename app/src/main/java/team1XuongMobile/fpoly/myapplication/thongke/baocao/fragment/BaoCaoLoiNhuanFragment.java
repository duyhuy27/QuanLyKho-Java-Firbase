package team1XuongMobile.fpoly.myapplication.thongke.baocao.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentBaoCaoLoiNhuanBinding;
import team1XuongMobile.fpoly.myapplication.thongke.baocao.adapter.ProfitsAdapter;
import team1XuongMobile.fpoly.myapplication.thongke.baocao.fragment.huongdan.HuongDanBaoCaoLoiNhuanFragment;
import team1XuongMobile.fpoly.myapplication.thongke.baocao.model.DateRangeProfit;


public class BaoCaoLoiNhuanFragment extends Fragment {

    private FragmentBaoCaoLoiNhuanBinding binding;

    private ProfitsAdapter adapter;

    public static final String TAG = "BaoCaoLoiNhuanFragment";

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBaoCaoLoiNhuanBinding.inflate(inflater, container, false);


        userClick();

        return binding.getRoot();
    }

    private void userClick() {
        binding.tvBatDauThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateStatistics();
            }
        });

        binding.tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        binding.tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });

        binding.buttonInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new HuongDanBaoCaoLoiNhuanFragment()).addToBackStack(null).commit();
            }
        });

        binding.tvPickMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectedYear = year;
                                selectedMonth = month;

                                // Update the TextView with the selected month
                                binding.tvPickMonth.setText(
                                        String.format(Locale.US, "%d-%02d", selectedYear, selectedMonth + 1)
                                );
                            }
                        },
                        currentYear, currentMonth, 1 // Initialize with the first day of the month
                );

                datePickerDialog.show();
            }
        });

        binding.buttonReloadMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateStatisticsMonth();
            }
        });

    }

    private void generateStatisticsMonth() {
        String selectedMonthStr = binding.tvPickMonth.getText().toString();

        if (selectedMonthStr.isEmpty()) {
            Toast.makeText(getContext(), "Please select a month", Toast.LENGTH_SHORT).show();
            return;
        }

        String startDateStr = selectedYear + "-" + (selectedMonth + 1) + "-01"; // First day of the selected month
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, 1);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String endDateStr = selectedYear + "-" + (selectedMonth + 1) + "-" + lastDay; // Last day of the selected month

        binding.progressCircular.setVisibility(View.VISIBLE); // Show ProgressBar

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(startDate);
            calendarStart.set(Calendar.HOUR_OF_DAY, 0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            long startTimeMillis = calendarStart.getTimeInMillis();

            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(endDate);
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            long endTimeMillis = calendarEnd.getTimeInMillis();

            Query inputQuery = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_nhap")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            Query outputQuery = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_xuat")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            inputQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot inputSnapshot) {
                    outputQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot outputSnapshot) {
                            List<DateRangeProfit> dateRangeStatsList = calculateProfitStatisticsMonth(inputSnapshot, outputSnapshot);
                            updateRecyclerView(dateRangeStatsList);
                            binding.progressCircular.setVisibility(View.GONE); // Hide ProgressBar
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled if needed
                            binding.progressCircular.setVisibility(View.GONE); // Hide ProgressBar
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled if needed
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRecyclerView(List<DateRangeProfit> dateRangeStatsList) {
        adapter = new ProfitsAdapter(dateRangeStatsList);
        binding.rcvLoiNhuan.setAdapter(adapter);
    }

    private void generateStatistics() {
        String startDateStr = binding.tvDateStart.getText().toString();
        String endDateStr = binding.tvDateEnd.getText().toString();

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(getContext(), "Please select both start and end dates", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressCircular.setVisibility(View.VISIBLE); // Hide ProgressBar


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(startDate);
            calendarStart.set(Calendar.HOUR_OF_DAY, 0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            long startTimeMillis = calendarStart.getTimeInMillis();

            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(endDate);
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            long endTimeMillis = calendarEnd.getTimeInMillis();

            Query inputQuery = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_nhap")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            Query outputQuery = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_xuat")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            inputQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot inputSnapshot) {
                    outputQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot outputSnapshot) {
                            List<DateRangeProfit> dateRangeStatsList = calculateProfitStatistics(inputSnapshot, outputSnapshot);
                            updateRecyclerView(dateRangeStatsList);
                            binding.progressCircular.setVisibility(View.GONE); // Hide ProgressBar

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled if needed
                            binding.progressCircular.setVisibility(View.GONE); // Hide ProgressBar

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled if needed
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }

    private List<DateRangeProfit> calculateProfitStatistics(DataSnapshot inputSnapshot, DataSnapshot outputSnapshot) {
        List<DateRangeProfit> dateRangeProfitList = new ArrayList<>();

        // Get the start and end dates from the TextViews
        String startDateStr = binding.tvDateStart.getText().toString();
        String endDateStr = binding.tvDateEnd.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            double totalProfit = 0;
            double totalSales = 0;

            while (!calendar.getTime().after(endDate)) {
                String date = dateFormat.format(calendar.getTime());

                double inputAmount = calculateTotalInputAmount(inputSnapshot, date);
                double outputAmount = calculateTotalOutputAmount(outputSnapshot, date);
                double profit = outputAmount - inputAmount;

                totalProfit += profit;

                totalSales += outputAmount;


                dateRangeProfitList.add(new DateRangeProfit(date, profit));

                calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
            }
            double totalProfitMargin = (totalProfit / totalSales) * 100;

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTotalValue = currencyFormatter.format(totalProfit);

            String formattedTotalProfitMargin = String.format("%.2f%%", totalProfitMargin);

            binding.tvTiXuatLoiNhuan.setText("Tỷ suất LN: " + formattedTotalProfitMargin);

            binding.tvTotalLoiNhuan.setText(formattedTotalValue);


        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception
        }

        return dateRangeProfitList;
    }

    private List<DateRangeProfit> calculateProfitStatisticsMonth(DataSnapshot inputSnapshot, DataSnapshot outputSnapshot) {
        List<DateRangeProfit> dateRangeProfitList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Calculate the first and last day of the selected month
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, 1);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        Date endDate = calendar.getTime();

        calendar.setTime(startDate);

        double totalProfit = 0;
        double totalSales = 0;

        while (!calendar.getTime().after(endDate)) {
            String date = dateFormat.format(calendar.getTime());

            double inputAmount = calculateTotalInputAmount(inputSnapshot, date);
            double outputAmount = calculateTotalOutputAmount(outputSnapshot, date);
            double profit = outputAmount - inputAmount;

            totalProfit += profit;
            totalSales += outputAmount;

            dateRangeProfitList.add(new DateRangeProfit(date, profit));

            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        }

        double totalProfitMargin = (totalProfit / totalSales) * 100;

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalValue = currencyFormatter.format(totalProfit);
        String formattedTotalProfitMargin = String.format("%.2f%%", totalProfitMargin);

        binding.tvTiXuatLoiNhuan.setText("Tỷ suất LN: " + formattedTotalProfitMargin);
        binding.tvTotalLoiNhuan.setText(formattedTotalValue);

        return dateRangeProfitList;
    }


    private double calculateTotalInputAmount(DataSnapshot inputSnapshot, String date) {
        double totalInputAmount = 0;

        for (DataSnapshot input : inputSnapshot.getChildren()) {
            if (date.equals(input.child("formattedDate").getValue(String.class))) {
                double inputAmount = Double.parseDouble(input.child("tong_tien_hang").getValue(String.class));
                totalInputAmount += inputAmount;
            }
        }

        return totalInputAmount;
    }

    private double calculateTotalOutputAmount(DataSnapshot outputSnapshot, String date) {
        double totalOutputAmount = 0;

        for (DataSnapshot output : outputSnapshot.getChildren()) {
            if (date.equals(output.child("formattedDate").getValue(String.class))) {
                double outputAmount = Double.parseDouble(output.child("tong_tien_hang").getValue(String.class));
                totalOutputAmount += outputAmount;
            }
        }

        return totalOutputAmount;
    }

    private void showDatePickerDialog(final boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        if (isStartDate) {
                            binding.tvDateStart.setText(selectedDate);
                        } else {
                            binding.tvDateEnd.setText(selectedDate);
                        }
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

}
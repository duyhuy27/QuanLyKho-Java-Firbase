package team1XuongMobile.fpoly.myapplication.fragments;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentBaoCaoDoanhThuBinding;
import team1XuongMobile.fpoly.myapplication.models.DateRangeStats;
import team1XuongMobile.fpoly.myapplication.adapter.StatsAdapter;


public class BaoCaoDoanhThuFragment extends Fragment {

    FragmentBaoCaoDoanhThuBinding binding;

    public static final String TAG = "BaoCaoDoanhThuFragment";

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBaoCaoDoanhThuBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        userClick();


        return binding.getRoot();
    }

    private void userClick() {
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

        binding.tvBatDauThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateStatistics();
            }
        });

        binding.buttonInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new HuongDanBaoCaoDoanhThuFragment()).addToBackStack(null).commit();
            }
        });

        // Inside onCreate or onViewCreated
        binding.tvPickMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectedYear = year;
                                selectedMonth = month;
                                selectedDay = dayOfMonth;

                                // Update the TextView with the selected month
                                binding.tvPickMonth.setText(
                                        String.format(Locale.US, "%d-%02d", selectedYear, selectedMonth + 1)
                                );
                            }
                        },
                        currentYear, currentMonth, currentDay
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

    private void updateRecyclerView(List<DateRangeStats> dayStatsList) {
        StatsAdapter statsAdapter = new StatsAdapter(dayStatsList);
        binding.rcvDonHang.setAdapter(statsAdapter);
    }


    private void generateStatistics() {

        String startDateStr = binding.tvDateStart.getText().toString();
        String endDateStr = binding.tvDateEnd.getText().toString();

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(getContext(), "Please select both start and end dates", Toast.LENGTH_SHORT).show();
            return;
        }

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

            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_xuat")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<DateRangeStats> dateRangeStatsList = calculateDateRangeStatistics(dataSnapshot, startDateStr, endDateStr);
                    updateRecyclerView(dateRangeStatsList);
                    binding.progressCircular.setVisibility(View.GONE); // Hide ProgressBar
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled if needed
                    binding.progressCircular.setVisibility(View.GONE); // Hide ProgressBar
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }

    private List<DateRangeStats> calculateDateRangeStatistics(DataSnapshot dataSnapshot, String startDate, String endDate) {
        TreeMap<String, DateRangeStats> statsMap = new TreeMap<>();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        double totalValue = 0;
        int totalCount = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);

            while (!calendar.getTime().after(end)) {
                String formattedDate = dateFormat.format(calendar.getTime());
                statsMap.put(formattedDate, new DateRangeStats(formattedDate, 0, 0));
                calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
            }

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String formattedDate = snapshot.child("formattedDate").getValue(String.class);
                if (formattedDate != null && !formattedDate.isEmpty()) { // Add null and empty check
                    int invoiceCount = 1; // Count each invoice
                    double value = Double.parseDouble(snapshot.child("tong_tien_hang").getValue(String.class));

                    if (statsMap.containsKey(formattedDate)) {
                        DateRangeStats existingStats = statsMap.get(formattedDate);
                        existingStats.addToInvoiceCount(invoiceCount);
                        existingStats.addToTotalValue(value);
                    }

                    totalValue += value;
                    totalCount += invoiceCount;
                }
            }

            String formattedTotalValue = currencyFormatter.format(totalValue);
            binding.tvSoDonHang.setText("Số đơn hàng " + totalCount);
            binding.tvTotalDoanhThu.setText(formattedTotalValue);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception
        }

        List<DateRangeStats> dateRangeStatsList = new ArrayList<>(statsMap.values());
        return dateRangeStatsList;
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

            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_xuat")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<DateRangeStats> dateRangeStatsList = calculateDateRangeStatistics(dataSnapshot, startDateStr, endDateStr);
                    updateRecyclerView(dateRangeStatsList);
                    binding.progressCircular.setVisibility(View.GONE); // Hide ProgressBar
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled if needed
                    binding.progressCircular.setVisibility(View.GONE); // Hide ProgressBar
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }

}
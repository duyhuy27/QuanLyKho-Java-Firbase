package team1XuongMobile.fpoly.myapplication.thongke.baocao.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentBaoCaoLaiLoBinding;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuNhap;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuXuat;
import team1XuongMobile.fpoly.myapplication.thongke.baocao.fragment.huongdan.HuongDanBaoCaoLaiLoFragment;


public class BaoCaoLaiLoFragment extends Fragment {

    private FragmentBaoCaoLaiLoBinding binding;

    public static final String TAG = "FragmentBaoCaoLaiLoFragment";

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBaoCaoLaiLoBinding.inflate(inflater, container, false);

        userClick();

        return binding.getRoot();
    }

    private void userClick() {

        binding.tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(true);
            }
        });

        binding.tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new HuongDanBaoCaoLaiLoFragment()).addToBackStack(null).commit();
            }
        });

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



            Query nhapQuery = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_nhap")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            Query xuatQuery = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_xuat")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            nhapQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot inputSnapshot) {
                    xuatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot xuatSnapshot) {

                            //caclucatethe profite, VAT, ...

                            calculateDateRangeStatistics(inputSnapshot, xuatSnapshot);


                            binding.progressCircular.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
        }

    }

    private void generateStatistics() {
        String starDateStr = binding.tvDateStart.getText().toString();
        String endDateStr = binding.tvDateEnd.getText().toString();

        if (starDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(getContext(), "Chọn ngày bắt đầu và ngày kết thúc để thống kê số liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressCircular.setVisibility(View.VISIBLE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            Date startDate = dateFormat.parse(starDateStr);
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

            long endTimeInMillis = calendarEnd.getTimeInMillis();


            Query nhapQuery = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_nhap")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeInMillis);

            Query xuatQuery = FirebaseDatabase.getInstance().getReference()
                    .child("phieu_xuat")
                    .orderByChild("timestamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeInMillis);

            nhapQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot inputSnapshot) {
                    xuatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot xuatSnapshot) {

                            //caclucatethe profite, VAT, ...

                            calculateDateRangeStatistics(inputSnapshot, xuatSnapshot);


                            binding.progressCircular.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
        }

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

    private void calculateDateRangeStatistics(DataSnapshot inputSnapshot, DataSnapshot xuatSnapshot) {


        double totalProfit = 0;

        double totalRevenue = 0;

        double totalCost = 0;

        double totalVAT = 0;

        double totalCostXuat = 0;

        double totalLNKhac = 0;


        for (DataSnapshot xuat : xuatSnapshot.getChildren()) {
            // Extract properties and perform calculations
            double tongTienHang = Double.parseDouble(xuat.child("tong_tien_hang").getValue(String.class));

            double costXuat = Double.parseDouble(xuat.child("tong_tien").getValue(String.class));

            String vatStr = xuat.child("thue_xuat").getValue(String.class);

            if (!vatStr.isEmpty()) {
                double vat = tongTienHang - costXuat;
                totalVAT += vat;
            }

            totalCostXuat += costXuat;

            totalRevenue += tongTienHang;

            Log.d(TAG, "calculateDateRangeStatistics: totalRevenue: " + totalRevenue);
        }

        for (DataSnapshot nhap : inputSnapshot.getChildren()) {

            double cost = Double.parseDouble(nhap.child("tong_tien").getValue(String.class));

            totalCost += cost;

            Log.d(TAG, "calculateDateRangeStatistics: cost " + cost);

        }


        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        String formattedTotalRevenue = currencyFormatter.format(totalRevenue);

        String formatTotalCost = currencyFormatter.format(totalCost);

        String formatTotalVAT = currencyFormatter.format(totalVAT);

        String formatTotalCostXuat = currencyFormatter.format(totalCostXuat);

        String formatTotalLN = currencyFormatter.format(totalLNKhac);


        //1

        binding.tvDoanhThuOvv.setText(formattedTotalRevenue); //1

        binding.tvDoanhThu1.setText(formattedTotalRevenue);

        binding.tvTienThucBan1.setText(formatTotalCostXuat);

        binding.tvThueVAT.setText(formatTotalVAT);

        //2


        binding.tvChiPhiKho.setText(formatTotalCost);

        binding.tvCost.setText(formatTotalCost); // 2

        binding.tvGiaVonHh.setText(formatTotalCost);

        // 3 + 4

        binding.tvTotalLn34.setText(formatTotalLN);

        totalProfit = totalRevenue - totalCost;

        String formatTotalProfit = currencyFormatter.format(totalProfit);

        if (totalProfit > 0) {
            binding.tvLoiNhau4.setText(formatTotalProfit);
            binding.tvTotalLoiNhuan.setText(formatTotalProfit);
            binding.tvTotalLoiNhuan.setTextColor(getResources().getColor(R.color.black));
            binding.tvLoiNhau4.setTextColor(getResources().getColor(R.color.black));
        } else if (totalProfit < 0) {
            binding.tvLoiNhau4.setText(formatTotalProfit);
            binding.tvTotalLoiNhuan.setText(formatTotalProfit);
            binding.tvTotalLoiNhuan.setTextColor(getResources().getColor(R.color.black));
            binding.tvLoiNhau4.setTextColor(getResources().getColor(R.color.mau_do));
        }

    }
}
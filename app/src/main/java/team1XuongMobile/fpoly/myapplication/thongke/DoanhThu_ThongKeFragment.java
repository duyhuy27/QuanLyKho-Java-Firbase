package team1XuongMobile.fpoly.myapplication.thongke;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.databinding.FragmentDoanhThuThongKeBinding;


public class DoanhThu_ThongKeFragment extends Fragment {
    private FragmentDoanhThuThongKeBinding binding;

    public static final String TAG = "DoanhThu_ThongKeFragment";
    private List<String> datesList = new ArrayList<>();
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    String endDateStr = "";
    private String startDateStr = "";
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoanhThuThongKeBinding.inflate(inflater, container, false);

        initFirebaseAuth();

        initCalendar();

        setDefaultDateRange();

//        processDataByDateRange();

        userClick();

        return binding.getRoot();
    }

    private void initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initCalendar() {
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
    }


    private void userClick() {
        binding.tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(requireContext(), startDateCalendar, binding.tvDateStart);
            }
        });

        binding.tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(requireContext(), endDateCalendar, binding.tvDateEnd);

            }
        });

        binding.buttonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataByDateRange(startDateStr, endDateStr);
            }
        });
    }

    private void showDatePickerDialog(Context context, Calendar calendar, TextView textView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = simpleDateFormat.format(calendar.getTime());
                textView.setText(formattedDate);
            }
        }, year, month, day);


        datePickerDialog.show();
    }

//    private void setTextViewsToDateRange(TextView textView1, TextView textView2) {
//        // Get the current time
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        String formattedCurrentDate = simpleDateFormat.format(calendar.getTime());
//
//        // Set TextView2 to the current time
//        textView2.setText(formattedCurrentDate);
//
//        // Set TextView1 to 7 days ago from the current time
//        calendar.add(Calendar.DAY_OF_MONTH, -7);
//        String formattedDate7DaysAgo = simpleDateFormat.format(calendar.getTime());
//        textView1.setText(formattedDate7DaysAgo);
//
//        loadDataByDateRange(startDateStr, endDateStr);
//
//    }

    private void setDefaultDateRange() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Get the end date (current date)
        endDateStr = formatDate(calendar.getTime());

        // Get the start date (7 days ago)
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        startDateStr = formatDate(calendar.getTime());

        // Update the date range UI
        updateDateRangeUI(startDateStr, endDateStr);

        // Load data for the default date range
        loadDataByDateRange(startDateStr, endDateStr);
    }

    private void updateDateRangeUI(String startDateStr, String endDateStr) {
        // Update your TextViews with the selected date range
        // Replace the following lines with your actual TextView references
        binding.tvDateStart.setText(startDateStr);
        binding.tvDateEnd.setText(endDateStr);
    }

    private String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    private void loadDataByDateRange(String startDateStr, String endDateStr) {
        // Parse the date strings to Date objects
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date startDate = simpleDateFormat.parse(startDateStr);
            Date endDate = simpleDateFormat.parse(endDateStr);

            // Query Firebase using the date range
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("phieu_xuat");
            Query query = reference.orderByChild("timestamp").startAt(startDate.getTime()).endAt(endDate.getTime() + 86399999);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<BarEntry> barEntries = new ArrayList<>();

                    // Process the retrieved data and aggregate by date
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String date = snapshot.child("ngay_xuat").getValue(String.class);
                        String tongTienStr = snapshot.child("tong_tien_hang").getValue(String.class);
                        float tongTien = 0;
                        try {
                            // Convert the money data (String) to float
                            tongTien = Float.parseFloat(tongTienStr);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        // Aggregate data by date
                        boolean found = false;
                        for (BarEntry entry : barEntries) {
                            if (entry.getX() == datesList.indexOf(date)) {
                                entry.setY(entry.getY() + tongTien);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            datesList.add(date);
                            barEntries.add(new BarEntry(datesList.size() - 1, tongTien));
                        }
                    }

                    // Create a BarDataSet with the BarEntry list
                    BarDataSet dataSet = new BarDataSet(barEntries, "Doanh số");

                    // Set custom colors for the bars
                    dataSet.setColor(Color.rgb(0, 128, 255)); // Blue
                    // ... Add more colors as needed

                    // Create a BarData object using the BarDataSet
                    BarData barData = new BarData(dataSet);

                    // Set data to the BarChart and invalidate to refresh the view
                    binding.barchar.setData(barData);
                    binding.barchar.invalidate();

                    // Customize the appearance of the chart
                    XAxis xAxis = binding.barchar.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(datesList));
                    xAxis.setLabelCount(datesList.size(), false); // Show all date labels
                    xAxis.setLabelRotationAngle(45f);

                    // Apply a scaling animation to the bar chart
                    binding.barchar.animateY(1000);

                    // Finally, notify the chart that data has changed
                    binding.barchar.notifyDataSetChanged();
                    binding.barchar.invalidate();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error, if any
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid date format!", Toast.LENGTH_SHORT).show();
        }
    }

    private void processDataByDateRange() {
        String startDateStr = binding.tvDateStart.getText().toString().trim();
        String endDateStr = binding.tvDateEnd.getText().toString().trim();

        // Parse the date strings to Date objects
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date startDate = simpleDateFormat.parse(startDateStr);
            Date endDate = simpleDateFormat.parse(endDateStr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            // Create a list to hold the aggregated data for each day
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            ArrayList<String> datesList = new ArrayList<>(); // List to store date labels

            // Loop through each day within the date range
            while (!calendar.getTime().after(endDate)) {
                Date currentDate = calendar.getTime();

                // Query Firebase for the current day's data
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("phieu_xuat");
                Query query = reference.orderByChild("timestamp").startAt(currentDate.getTime()).endAt(currentDate.getTime() + 86399999);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        float totalTongTien = 0;

                        // Process the retrieved data and calculate total tongTien for the day
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String tongTienStr = snapshot.child("tong_tien_hang").getValue(String.class);
                            try {
                                float tongTien = Float.parseFloat(tongTienStr);
                                totalTongTien += tongTien;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                        // Add the aggregated data to the list
                        barEntries.add(new BarEntry(barEntries.size(), totalTongTien));
                        datesList.add(simpleDateFormat.format(currentDate)); // Format the current date and add to the list

                        // Create a BarDataSet with the BarEntry list
                        BarDataSet dataSet = new BarDataSet(barEntries, "Doanh số");

                        // Set custom colors or other styling for the bars if needed
                        dataSet.setColor(Color.BLUE);

                        // Create a BarData object using the BarDataSet
                        BarData barData = new BarData(dataSet);

                        // Set data to the BarChart and invalidate to refresh the view
                        binding.barchar.setData(barData);
                        binding.barchar.notifyDataSetChanged();
                        binding.barchar.invalidate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error, if any
                    }
                });

                // Move to the next day
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid date format!", Toast.LENGTH_SHORT).show();
        }
    }

}
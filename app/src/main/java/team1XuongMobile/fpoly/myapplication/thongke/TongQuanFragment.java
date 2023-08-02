package team1XuongMobile.fpoly.myapplication.thongke;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentTongQuanBinding;


public class TongQuanFragment extends Fragment {

    private FragmentTongQuanBinding binding;

    public static final String TAG = "TongQuanFragment";

    private FirebaseAuth firebaseAuth;

    private Calendar calendar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTongQuanBinding.inflate(inflater, container, false);

        initFirebaseAuth();

        initCalendar();

        setTimeDefault();

        userClick();


        return binding.getRoot();
    }

    private void initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initCalendar() {
        calendar = Calendar.getInstance();

    }

    private void setTimeDefault() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedCurrentDate = simpleDateFormat.format(calendar.getTime());

        // Set TextView2 to the current time
        binding.tvDate.setText("Hôm nay, " + formattedCurrentDate);
    }

    private void userClick(){
        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(getContext(), calendar, binding.tvDate);
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
}
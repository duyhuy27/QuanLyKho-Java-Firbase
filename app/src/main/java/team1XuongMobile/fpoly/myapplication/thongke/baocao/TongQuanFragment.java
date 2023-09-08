package team1XuongMobile.fpoly.myapplication.thongke.baocao;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Locale;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentTongQuanBinding;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuNhap;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.PhieuXuat;


public class TongQuanFragment extends Fragment {

    private FragmentTongQuanBinding binding;

    public static final String TAG = "TongQuanFragment";

    private FirebaseAuth firebaseAuth;

    private Calendar calendar;

    private ArrayList<PhieuNhap> phieuNhapArrayList;

    private ArrayList<PhieuXuat> phieuXuatArrayList;

    private float totalMoneyNhap = 0;

    private float totalMoneyXuat = 0;

    private float totalDoanhSo = 0;

    private int sllPhieuXuat = 0;

    private int sllPhieuNhap = 0;

    private int totalSlSPNhap = 0;

    private int totalSLSPXuat = 0;

    private float totalTienHangNhap = 0;

    private float totalTienHangXuat = 0;

    private boolean showDoanhSoAvailability = false;

    private String selectedDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTongQuanBinding.inflate(inflater, container, false);

        initFirebaseAuth();

        initCalendar();

        setTimeDefault();

        hideShowMoneyLoiNhuanFunction();

        loadDataHangTon();

        userClick();


//        caculateHangTon();

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
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String dateDefault = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
        binding.tvDate.setText("Hôm nay, " + dateDefault);

        loadDataPhieuXuat(dateDefault);

    }

    private void userClick() {
        // Add a click listener to the TextView to open a DatePickerDialog
        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        if (binding.tvLoinhuanMoney.getText().toString().trim().equals("**************")) {
            showDoanhSoAvailability = true;
        }


    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        binding.tvDate.setText("Ngày được chọn: " + selectedDate);

                        loadDataPhieuXuat(selectedDate);
                        loadDataPhieuNhap(selectedDate);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void loadDataPhieuXuat(String selectedDate) {
        phieuXuatArrayList = new ArrayList<>();

        // Parse the selected date to a Calendar object
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = simpleDateFormat.parse(this.selectedDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        // Calculate the start timestamp for the selected date (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTimestamp = calendar.getTimeInMillis();

        // Calculate the end timestamp for the selected date (23:59:59)
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endTimestamp = calendar.getTimeInMillis();

        // Query the Firebase database for data within the date range
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phieu_xuat");
        binding.progressCircularNhapXuat.setVisibility(View.VISIBLE);
        databaseReference.orderByChild("timestamp")
                .startAt(startTimestamp)
                .endAt(endTimestamp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        phieuXuatArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            PhieuXuat object = ds.getValue(PhieuXuat.class);
                            phieuXuatArrayList.add(object);
                        }


                        for (PhieuXuat object : phieuXuatArrayList) {
                            try {
                                float moneyTotalPx = Float.parseFloat(object.getTong_tien_hang());
                                totalMoneyXuat += moneyTotalPx;
                            } catch (Exception e) {
                                Log.d(TAG, "onDataChange: can not parse " + e.getMessage());
                            }
                        }

                        binding.progressCircularNhapXuat.setVisibility(View.GONE);

                        sllPhieuXuat = phieuXuatArrayList.size();

                        if (phieuXuatArrayList.isEmpty()) {
                            binding.tvSllPhieuXuat.setText("0" + " phiếu xuất");
                            totalMoneyXuat = 0;
                            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            String formattedTotalValue = currencyFormatter.format(totalMoneyXuat);
                            binding.tvMoneyPhieuXuat.setText(formattedTotalValue);

                            return;
                        } else {
                            binding.tvSllPhieuXuat.setText("" + sllPhieuXuat + " phiếu xuất");
                            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            String formattedTotalValue = currencyFormatter.format(totalMoneyXuat);
                            binding.tvMoneyPhieuXuat.setText(formattedTotalValue);
                        }


                        // Now that you have the data for the selected date range (24 hours),
                        // you can perform your analysis on phieuXuatArrayList.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });




    }

    private void loadDataPhieuNhap(String selectedDate) {
        phieuNhapArrayList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar1 = Calendar.getInstance();

        try {
            Date date = simpleDateFormat.parse(this.selectedDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();

        //caculator the end timestampt
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endTime = calendar.getTimeInMillis();

        //query

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("phieu_nhap");
        binding.progressCircularNhapXuat.setVisibility(View.VISIBLE);
        databaseReference.orderByChild("timestamp")
                .startAt(startTime)
                .endAt(endTime)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        phieuNhapArrayList = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            PhieuNhap object = ds.getValue(PhieuNhap.class);
                            phieuNhapArrayList.add(object);
                        }


                        for (PhieuNhap object : phieuNhapArrayList) {
                            try {
                                float moneyPN = Float.parseFloat(object.getTong_tien_hang());
                                totalMoneyNhap += moneyPN;
                            } catch (Exception e) {
                                Log.d(TAG, "onDataChange: can not parse money" + e.getMessage());
                            }
                        }

                        binding.progressCircularNhapXuat.setVisibility(View.GONE);

                        sllPhieuNhap = phieuNhapArrayList.size();
                        if (phieuNhapArrayList.isEmpty()) {
                            binding.tvSlPhieuNhap.setText("0 phiếu nhập");
                            totalMoneyNhap = 0;
                            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            String formattedTotalValue = currencyFormatter.format(totalMoneyNhap);
                            binding.tvMoneyNhap.setText(formattedTotalValue);
                            return;
                        } else {
                            binding.tvSlPhieuNhap.setText("" + sllPhieuNhap + " phiếu nhập");
                            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            String formattedTotalValue = currencyFormatter.format(totalMoneyNhap);
                            binding.tvMoneyNhap.setText(formattedTotalValue);
                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    int slSpN = 0, slSpX = 0;

    private void loadDataHangTon() {
//        ArrayList<PhieuXuat> phieuXuatArrayList1 = new ArrayList<>();
//        ArrayList<PhieuNhap> phieuNhapArrayList1 = new ArrayList<>();
//
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(firebaseAuth.getCurrentUser().getUid());
//        binding.progressCircularHangTon.setVisibility(View.VISIBLE);
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String kh = "" + snapshot.child("kh").getValue(String.class);
//
//                DatabaseReference refSllPhieuXuat = FirebaseDatabase.getInstance().getReference("phieu_xuat");
//                Query query = refSllPhieuXuat.orderByChild("kh").equalTo(kh);
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        phieuXuatArrayList1.clear();
//                        for (DataSnapshot ds : snapshot.getChildren()) {
//                            PhieuXuat objectXuat = ds.getValue(PhieuXuat.class);
//                            phieuXuatArrayList1.add(objectXuat);
//                        }
//                        float totalMoneyPX = 0;
//                        for (PhieuXuat phieuXuat : phieuXuatArrayList1) {
//                            try {
//                                float moneyPX = Float.parseFloat(phieuXuat.getTong_tien_hang());
//                                totalMoneyPX += moneyPX;
//                                slSpX = Integer.parseInt(phieuXuat.getSo_luong());
//                                totalSLSPXuat += slSpX;
//                                float tienHangXuat = Float.parseFloat(phieuXuat.getTong_tien());
//                                totalTienHangXuat += tienHangXuat;
//                            } catch (Exception e) {
//                                Log.d(TAG, "onDataChange: can not parse money" + e.getMessage());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String kh = "" + snapshot.child("kh").getValue(String.class);
//
//                DatabaseReference refSllPhieuXuat = FirebaseDatabase.getInstance().getReference("phieu_nhap");
//                Query query = refSllPhieuXuat.orderByChild("kh").equalTo(kh);
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        phieuNhapArrayList1.clear();
//                        for (DataSnapshot ds : snapshot.getChildren()) {
//                            PhieuNhap objectNhap = ds.getValue(PhieuNhap.class);
//                            phieuNhapArrayList1.add(objectNhap);
//                        }
//                        Log.d(TAG, "onDataChange: data phieu nhap array list" + phieuNhapArrayList1.size());
//                        float totalMoneyPN = 0;
//                        for (PhieuNhap phieuNhap : phieuNhapArrayList1) {
//                            try {
//                                float moneyPN = Float.parseFloat(phieuNhap.getTong_tien_hang());
//                                totalMoneyPN += moneyPN;
//                                slSpN = Integer.parseInt(phieuNhap.getSo_luong());
//                                float tienHangNhap = Float.parseFloat(phieuNhap.getTong_tien());
//                                totalTienHangNhap += tienHangNhap;
//                                totalSlSPNhap += slSpN;
//
//                            } catch (Exception e) {
//                                Log.d(TAG, "onDataChange: can not parse money" + e.getMessage());
//                            }
//                        }
//                        Log.d(TAG, "onDataChange: total so luong san pham nhap " + totalSlSPNhap + totalSLSPXuat);
//                        binding.progressCircularHangTon.setVisibility(View.GONE);
//                        caculateHangTon(totalSlSPNhap, totalSLSPXuat);
//                        caculateTienTon(totalTienHangNhap, totalTienHangXuat);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//        Log.d(TAG, "loadDataHangTon: " + totalSlSPNhap + totalSLSPXuat);
        DatabaseReference referenceHangTon = FirebaseDatabase.getInstance().getReference("total_quantity");
        referenceHangTon.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total_quantity_hang_ton = 0; // Initialize it outside the loop
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String total_quantity = "" + ds.child("total_quantity").getValue();
                    try {
                        int total_quantity_int = 0;

                        if (total_quantity != null) {
                            total_quantity_int = Integer.parseInt(total_quantity);
                        }

                        total_quantity_hang_ton += total_quantity_int; // Add the quantity to the total
                    } catch (Exception e) {
                        Log.d(TAG, "onDataChange: can not parse total quantity " + e.getMessage());
                    }
                }

                // After the loop, set the TextView with the final total quantity
                binding.tvSlHangTon.setText(String.valueOf(total_quantity_hang_ton));
                Log.d(TAG, "onDataChange: total_quantity_int bang " + total_quantity_hang_ton);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });



    }

//    private void caculateHangTon(int slSpN, int slSpX) {
//        try {
//            int sllHangTon = slSpN - slSpX;
//            Log.d(TAG, "caculateHangTon: " + sllHangTon + slSpN + slSpX);
//            binding.tvSlHangTon.setText(String.valueOf(sllHangTon));
//
//        } catch (Exception e) {
//            Log.d(TAG, "caculateHangTon: can not convert  " + e.getMessage());
//        }
//
//
//    }

//    private void caculateTienTon(float totalTienHangNhap, float totalTienHangXuat) {
//        try {
//            float totalTienHangTon = totalTienHangNhap - totalTienHangXuat;
//
//            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//            String formattedTotalValue = currencyFormatter.format(totalTienHangTon);
//            binding.tvTienTonKho.setText(formattedTotalValue);
//
//        } catch (Exception e) {
//
//        }
//    }

    private void hideShowMoneyLoiNhuanFunction() {
        binding.hideShowMoneyLoiNhuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showDoanhSoAvailability) {
                    binding.hideShowMoneyLoiNhuan.setImageResource(R.drawable.hide);
                    binding.tvLoinhuanMoney.setText("**************");
                    binding.tvLoinhuanMoney.setTextColor(Color.GREEN);
                    showDoanhSoAvailability = false;
                } else {
                    binding.hideShowMoneyLoiNhuan.setImageResource(R.drawable.show_24);
                    totalDoanhSo = totalMoneyXuat - totalMoneyNhap;
                    if (totalDoanhSo < 0) {

                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String formattedTotalValue = currencyFormatter.format(totalDoanhSo);
                        binding.tvLoinhuanMoney.setText(formattedTotalValue);

                        binding.tvLoinhuanMoney.setTextColor(Color.RED);
                    } else if (totalDoanhSo > 0) {
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String formattedTotalValue = currencyFormatter.format(totalDoanhSo);
                        binding.tvLoinhuanMoney.setText(formattedTotalValue);
                        binding.tvLoinhuanMoney.setTextColor(Color.GREEN);
                    } else {
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String formattedTotalValue = currencyFormatter.format(totalDoanhSo);
                        binding.tvLoinhuanMoney.setText(formattedTotalValue);
                        binding.tvLoinhuanMoney.setTextColor(Color.GREEN);
                    }
                    showDoanhSoAvailability = true;
                }
            }
        });
    }


}
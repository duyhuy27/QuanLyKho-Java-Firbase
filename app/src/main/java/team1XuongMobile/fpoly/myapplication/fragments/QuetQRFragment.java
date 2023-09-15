package team1XuongMobile.fpoly.myapplication.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


import team1XuongMobile.fpoly.myapplication.databinding.FragmentQuetQRBinding;
import team1XuongMobile.fpoly.myapplication.functionGlobal.CaptureAct;

public class QuetQRFragment extends Fragment {

    private FragmentQuetQRBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuetQRBinding.inflate(inflater, container, false);
        binding.buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQr();
            }
        });
        return binding.getRoot();
    }

    private void scanQr() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volunm up to flash on");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureAct.class);
        barLaunch.launch(scanOptions);
    }

    private ActivityResultLauncher<ScanOptions> barLaunch = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String scannedResult = result.getContents();

            // Find the index of the separator ("\n") in the scanned result
            int separatorIndex = scannedResult.indexOf("\n");

            if (separatorIndex >= 0) {
                // Extract the substring after the separator
                String maSpValue = scannedResult.substring(separatorIndex + 1);

                // Use maSpValue for further processing
                // ...

                // Show a dialog with the extracted value
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Scanned Result");
                builder.setMessage(maSpValue)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            } else {
                // Handle case when separator is not found in the scanned result
            }
        }
    });


}
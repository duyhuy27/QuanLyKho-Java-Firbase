package team1XuongMobile.fpoly.myapplication.sanpham.qrcode;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentTaoQRBinding;


public class TaoQRFragment extends Fragment {

    private FragmentTaoQRBinding binding;

    private ArrayList<String> idSpArray, tenSpArray, maSpArray;

    private String chonIdSp = "", chonTenSp = "", chonMaSp = "";

    public static final String TAG = "TaoQRFragment";

    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTaoQRBinding.inflate(inflater, container, false);

        loadSanPham();

        listener();


        return binding.getRoot();
    }

    private void loadSanPham() {
        idSpArray = new ArrayList<>();
        tenSpArray = new ArrayList<>();
        maSpArray = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SanPham");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idSpArray.clear();
                tenSpArray.clear();
                maSpArray.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = "" + ds.child("idSanPham").getValue();
                    String ten = "" + ds.child("tenSp").getValue();
                    String ma = "" + ds.child("maSp").getValue();

                    idSpArray.add(id);
                    tenSpArray.add(ten);
                    maSpArray.add(ma);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }


    private void listener() {
        binding.tvChonSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an array to store the lines in "ten + ma" format
                ArrayList<String> dialogItems = new ArrayList<>();

// Add the lines to the dialogItems array
                for (int i = 0; i < tenSpArray.size(); i++) {
                    String line = tenSpArray.get(i) + "\n" + maSpArray.get(i);
                    dialogItems.add(line);
                }

// Convert the dialogItems array to a regular array for the AlertDialog
                final CharSequence[] items = dialogItems.toArray(new CharSequence[dialogItems.size()]);

// Create the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Chọn sản phẩm")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Get the selected item
                                chonTenSp = items[which].toString();
                                chonIdSp = idSpArray.get(which);
                                chonMaSp = maSpArray.get(which);

                                binding.tvChonSp.setText(chonTenSp);

                                Log.d(TAG, "onClick: ten sp : " + chonTenSp + "" + "ma sp  " + chonMaSp + " id sanpham " + chonIdSp);

                                // Do something with the selected item
                                // For example, display it in a Toast

                            }
                        });

// Show the AlertDialog
                builder.create().show();


            }
        });

        binding.buttonTaoQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taoQr();
            }
        });

        binding.buttonQuetQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQr();
            }
        });

        binding.buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQrCodeToGallery(bitmap);
            }
        });

        binding.buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        binding.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareQrCode(bitmap);
            }
        });

    }

    private void taoQr() {
        String text = binding.tvChonSp.getText().toString().trim() + " + " + chonIdSp;
        Log.d(TAG, "taoQr: id san pham qr " + chonIdSp);
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
            binding.idImgQrcode.setImageBitmap(bitmap);
            binding.tvGenCode.setVisibility(View.GONE);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

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

            // Find the index of the separator (" + ") in the scanned result
            int separatorIndex = scannedResult.indexOf(" + ");

            if (separatorIndex >= 0) {
                // Extract the substring after the separator
                String chonIdSp = scannedResult.substring(separatorIndex + 3);

                // Use chonIdSp for further processing
                // ...

                // Show a dialog with the extracted value
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Scanned Result");
                builder.setMessage(chonIdSp)
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

    private void saveQrCodeToGallery(Bitmap bitmap) {
        String fileName = "qr_code_" + System.currentTimeMillis() + ".png";
        OutputStream fos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getActivity().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                if (fos != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    Toast.makeText(getContext(), "Đã lưu mã QR của sản phẩm", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Lỗi " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "saveQrCodeToGallery: không thể lưu mã qr  " + e.getMessage());
            }
        } else {
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
            File file = new File(directory, fileName);
            try {
                fos = new FileOutputStream(file);
                if (fos != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    Toast.makeText(getContext(), "Đã lưu mã QR của sản phẩm", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to save QR code to Gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareQrCode(Bitmap bitmap) {
        Context context = requireContext(); // Get the context associated with the fragment

        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // Create the directory if it doesn't exist

            // Save the bitmap to a file within the cache directory
            File qrCodeFile = new File(cachePath, "qr_code.png");
            FileOutputStream fos = new FileOutputStream(qrCodeFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            // Create the share intent
            Uri qrCodeUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", qrCodeFile);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, qrCodeUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Start the activity for sharing
            startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to share QR code", Toast.LENGTH_SHORT).show();
        }
    }


}
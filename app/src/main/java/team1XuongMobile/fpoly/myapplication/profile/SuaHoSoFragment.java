package team1XuongMobile.fpoly.myapplication.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentSuaHoSoBinding;


public class SuaHoSoFragment extends Fragment {

    private FragmentSuaHoSoBinding binding;

    private FirebaseAuth firebaseAuth;

    public static final String TAG = "SuaHoSoFragment";

    private Uri img_uri;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuaHoSoBinding.inflate(inflater, container, false);


        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading...");

        loadDataUser();

        userClick();


        return binding.getRoot();
    }

    private void userClick() {
        binding.buttonPickCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialocChonAnh();
            }
        });

        binding.buttonHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private String fullName = "";

    private void validateData() {
        fullName = binding.edtFullname.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(getContext(), "Not empty...", Toast.LENGTH_SHORT).show();
        } else {
            submitDataToFirebase();
        }
    }

    private void submitDataToFirebase() {
        progressDialog.show();


        String filePathAndName = "ProfileImages/" + firebaseAuth.getUid();

        if (img_uri == null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("username", "" + fullName);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Accounts");
            databaseReference.child(firebaseAuth.getUid())
                    .updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Lỗi ! Vui lòng thử lại sau ", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: lỗi không update được " + e.getMessage());
                        }
                    });
        } else {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(img_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            while (!task.isSuccessful()) ;
                            Uri downloadUri = task.getResult();
                            if (task.isSuccessful()) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("username", "" + fullName);
                                hashMap.put("avatar" , ""+ downloadUri);

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Accounts");
                                databaseReference.child(firebaseAuth.getUid())
                                        .updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                getActivity().getSupportFragmentManager().popBackStack();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Lỗi ! Vui lòng thử lại sau ", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "onFailure: lỗi không update được " + e.getMessage());
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Lỗi ! Vui lòng thử lại sau ", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: lỗi không update được " + e.getMessage());
                        }
                    });
        }

    }

    private void showDialocChonAnh() {
        String[] options = {"Máy Ảnh", "Thư Viện"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // người dùng chọn máy ảnh
                    pickMayAnhFuntion();
                } else if (i == 1) {
                    // người dùng chọn thư viện
                    pickThuVienFuntion();
                }
            }
        }).show();
    }

    private void pickMayAnhFuntion() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Máy ảnh");
        contentValues.put(MediaStore.Images.Media.TITLE, "Máy ảnh");
        img_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, img_uri);
        cameraActivityResult.launch(intent);
    }

    private void pickThuVienFuntion() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResult.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();

                try {
                    Picasso.get().load(img_uri).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera).into(binding.profileImage);
                } catch (Exception e) {
                    Log.d(TAG, "onActivityResult: Không thể load ảnh " + e.getMessage());
                }
            }
        }
    });

    private ActivityResultLauncher<Intent> galleryActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();

                img_uri = intent.getData();

                try {
                    Picasso.get().load(img_uri).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera).into(binding.profileImage);
                } catch (Exception e) {
                    Log.d(TAG, "onActivityResult: Không thể load ảnh " + e.getMessage());
                }
            }
        }
    });


    private void loadDataUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Accounts");
        databaseReference.child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = "" + snapshot.child("email").getValue();
                        String name = "" + snapshot.child("username").getValue();
                        String img = "" + snapshot.child("avatar").getValue();//password
                        String password = "" + snapshot.child("password").getValue();
                        String sdt = "" + snapshot.child("sdt").getValue();

                        try {
                            Picasso.get().load(img).placeholder(R.drawable.logo).error(R.drawable.logo)
                                    .into(binding.profileImage);
                        } catch (Exception e) {

                        }

                        binding.edtFullname.setText(name);
                        binding.tvUserName.setText(name);
                        binding.tvEmail.setText(email);
                        binding.edtPassword.setText(password);
                        binding.edtPhone.setText(sdt);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
package team1XuongMobile.fpoly.myapplication.fragments;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentDoiMatKhauBinding;


public class DoiMatKhauFragment extends Fragment {

    private FragmentDoiMatKhauBinding binding;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    public static final String TAG = "DoiMatKhauFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoiMatKhauBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        userClick();


        return binding.getRoot();
    }

    private void userClick() {
        binding.edtPasswordNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passwordInput = charSequence.toString();
                if (passwordInput.length() >= 6) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(passwordInput);
                    boolean passwordsMatch = matcher.find();
                    if (passwordsMatch) {
                        binding.passwordNewTil.setHelperText("Mật khẩu mạnh !");
                        binding.passwordNewTil.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        binding.passwordNewTil.setError("");
                    } else {
                        binding.passwordNewTil.setError("Mật khẩu không hợp lệ");
                    }
                } else {
                    binding.passwordNewTil.setHelperText("Mật khẩu không hợp lệ");
                    binding.passwordNewTil.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edtPasswordNewCF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passwordInput = charSequence.toString();
                if (passwordInput.length() >= 6) {
                    if (passwordInput.equals(binding.edtPasswordNew.getText().toString().trim())) {
                        binding.passwordNewTilCF.setHelperText("Khớp mật khẩu");
                        binding.passwordNewTilCF.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        binding.passwordNewTilCF.setError("");
                    } else {
                        binding.passwordNewTilCF.setHelperText("Mật khẩu không khớp");
                        binding.passwordNewTilCF.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.mau_do)));
                        binding.passwordNewTilCF.setError("");
                    }
                } else {
                    binding.passwordNewTilCF.setHelperText("Mật khẩu không khớp");
                    binding.passwordNewTilCF.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.buttonHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private String oldPassword = "", newPassword = "";

    private void validateData() {
        oldPassword = binding.edtPassword.getText().toString().trim();
        newPassword = binding.edtPasswordNew.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(getContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(getContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();

        } else if (!newPassword.equals(binding.edtPasswordNewCF.getText().toString().trim())) {
            Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();

        } else {
            updatePassword(oldPassword, newPassword);

        }

    }

    private void updatePassword(String oldPassword, String newPassword) {
        progressDialog.show();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), md5(oldPassword));

        firebaseUser.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        firebaseUser.updatePassword(md5(newPassword))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("password", md5(newPassword));

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Accounts");
                                        databaseReference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Log.d(TAG, "onFailure: "+e.getMessage());
                                                        Toast.makeText(getContext(), "Lỗi ! Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Lỗi ! Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Lỗi ! Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: lỗi không update được mật khẩu " + e.getMessage());
                    }
                });

    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
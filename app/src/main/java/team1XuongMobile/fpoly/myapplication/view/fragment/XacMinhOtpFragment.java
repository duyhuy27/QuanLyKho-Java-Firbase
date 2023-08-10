package team1XuongMobile.fpoly.myapplication.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentXacMinhOtpBinding;


public class XacMinhOtpFragment extends Fragment {

    private FragmentXacMinhOtpBinding binding;

    long timeout = 60L;

    private String otpCode;

    private PhoneAuthProvider.ForceResendingToken resendingToken;

    public static final String SDT = "key_sdt";

    private String sdt;

    private FirebaseAuth firebaseAuth;

    public static final String KEY_USERNAME = "userName";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_PASSWORD = "password";

    private String username, email, password, passwordConfirm;


    public static final String TAG = "XacMinhOtpFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentXacMinhOtpBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        nhanDuLieu();


        listener(); // hàm xử lý view khi người dùng click

        guiOtp(sdt, false);


        return binding.getRoot();
    }

    private void nhanDuLieu() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            sdt = bundle.getString(SDT);
            username = bundle.getString(KEY_USERNAME);
            email = bundle.getString(KEY_EMAIL);
            password = bundle.getString(KEY_PASSWORD);
            Log.d(TAG, "nhanDuLieu: Số điện thoại nhận là : " + sdt + username + email + password);
        }
    }

    private void guiOtp(String sdt, boolean isResend) {
        setThoiGianDemNguoc();
        setProgressBar(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(sdt)
                .setTimeout(timeout, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        setProgressBar(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getContext(), "Lỗi không thể xác thực OTP", Toast.LENGTH_SHORT).show();
                        setProgressBar(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        otpCode = s;
                        resendingToken = forceResendingToken;
                        Toast.makeText(getContext(), "Gửi mã OTP thành công", Toast.LENGTH_SHORT).show();
                        setProgressBar(false);


                    }
                });

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());

        }


    }

    private void setThoiGianDemNguoc() {
        binding.edtGuiLI.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeout--;
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.edtGuiLI.setText("Gửi lại " + "(" + timeout + ")");
                    }
                });
                if (timeout <= 0) {
                    timeout = 60L;
                    timer.cancel();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.edtGuiLI.setEnabled(true);
                        }
                    });
                }
            }
        }, 0, 1000);
    }


    private void setProgressBar(boolean inProgress) {
        if (inProgress) {
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.buttonXacMinh.setVisibility(View.GONE);
        } else {
            binding.progressbar.setVisibility(View.GONE);
            binding.buttonXacMinh.setVisibility(View.VISIBLE);
        }
    }

    private void listener() {
        binding.edt01.addTextChangedListener(textWatcher);
        binding.edt02.addTextChangedListener(textWatcher);
        binding.edt03.addTextChangedListener(textWatcher);
        binding.edt04.addTextChangedListener(textWatcher);
        binding.edt05.addTextChangedListener(textWatcher);
        binding.edt06.addTextChangedListener(textWatcher);

        binding.buttonXacMinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maOtp =
                        binding.edt01.getText().toString().trim() +
                                binding.edt02.getText().toString().trim() +
                                binding.edt03.getText().toString().trim() +
                                binding.edt04.getText().toString().trim() +
                                binding.edt05.getText().toString().trim() +
                                binding.edt06.getText().toString().trim();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpCode, maOtp);
                confirmThanhCong(credential);
            }
        });

        binding.edtGuiLI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guiOtp(sdt, true);
            }
        });

        binding.buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void confirmThanhCong(PhoneAuthCredential credential) {
        setProgressBar(true);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.createUserWithEmailAndPassword(email, md5(password))
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            luuDuLieuLenFirebase();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            setProgressBar(false);
                                            Toast.makeText(getContext(), "Lỗi gì đó đã xảy ra " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            setProgressBar(false);
                        }
                    }
                });
    }


    private void luuDuLieuLenFirebase() {
        setProgressBar(true);

        long timestamp = System.currentTimeMillis();

        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("password", md5(password));
        hashMap.put("sdt", sdt);
        hashMap.put("timestamp", timestamp);
        hashMap.put("username", username);
        hashMap.put("vaiTro", "admin");
        hashMap.put("avatar", "");
        hashMap.put("kh", "" + timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        setProgressBar(false);
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_EMAIL, email);

                        XacMinhThanhCongFragment fragment = new XacMinhThanhCongFragment();
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_layout, fragment).addToBackStack(null).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Đăng ký tài khoản thất bại " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        setProgressBar(false);
                        Log.d(TAG, "onFailure: đăng ký tài khoản thất bại vì " + e.getMessage());
                    }
                });
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                View nextEditText = getNextEditText();

                if (nextEditText != null) {
                    nextEditText.requestFocus();
                    showKeyboard(nextEditText);
                }
            }
        }
    };

    private View getNextEditText() {
        View currentFocus = getActivity().getCurrentFocus();
        int currentIndex = -1;

        if (currentFocus instanceof EditText) {
            EditText currentEditText = (EditText) currentFocus;

            if (currentEditText == binding.edt01) {
                currentIndex = 0;
            } else if (currentEditText == binding.edt02) {
                currentIndex = 1;
            } else if (currentEditText == binding.edt03) {
                currentIndex = 2;
            } else if (currentEditText == binding.edt04) {
                currentIndex = 3;
            } else if (currentEditText == binding.edt05) {
                currentIndex = 4;
            } else if (currentEditText == binding.edt06) {
                currentIndex = 5;
            }
        }

        if (currentIndex >= 0 && currentIndex < 5) {
            return getEditTextByIndex(currentIndex + 1);
        }

        return null;
    }

    private EditText getEditTextByIndex(int index) {
        EditText editText = null;
        switch (index) {
            case 0:
                editText = binding.edt01;
                break;
            case 1:
                editText = binding.edt02;
                break;
            case 2:
                editText = binding.edt03;
                break;
            case 3:
                editText = binding.edt04;
                break;
            case 4:
                editText = binding.edt05;
                break;
            case 5:
                editText = binding.edt06;
                break;
        }
        return editText;
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
package team1XuongMobile.fpoly.myapplication.view.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoan.ThemTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentDangKyBinding;


public class DangKyFragment extends Fragment {

    private FragmentDangKyBinding binding;

    private String username, email, password, passwordConfirm;

    public static final String KEY_USERNAME = "userName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDangKyBinding.inflate(inflater, container, false);

        listener(); // hàm xử lý khi người dùng click view


        return binding.getRoot();
    }

    private void listener() {
        binding.buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
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
                        binding.passwordTil.setHelperText("Your Password are Strong");
                        binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        binding.passwordTil.setError("");
                    } else {
                        binding.passwordTil.setError("Please make sure your password includes at least one non-capital letter, one uppercase letter, and one special character for increased security");
                    }
                } else {
                    binding.passwordTil.setHelperText("Password must 6 Characters long");
                    binding.passwordTil.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.buttonDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private void validateData() {
        username = binding.edtUsername.getText().toString().trim();
        email = binding.edtEmail.getText().toString().trim();
        password = binding.edtPassword.getText().toString().trim();
        passwordConfirm = binding.edtNhaplaiPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            binding.edtUsername.setError("Bạn cần nhập đủ thông tin");
            binding.edtUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            binding.edtEmail.setError("Bạn cần nhập đủ thông tin");
            binding.edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.edtPassword.setError("Bạn cần nhập đủ thông tin");
            binding.edtPassword.requestFocus();
            return;
        } else if (!password.equals(passwordConfirm)) {
            binding.edtNhaplaiPassword.setError("Mật khẩu không khớp");
            binding.edtNhaplaiPassword.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Email không đúng định dạng");
            binding.edtEmail.requestFocus();
            return;
        } else {
            dangKy();
        }
    }

    private void dangKy() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USERNAME, username);
        bundle.putString(KEY_EMAIL, email);
        bundle.putString(KEY_PASSWORD, password);

        GuiOtpFragment fragment = new GuiOtpFragment();
        fragment.setArguments(bundle);
        ThemTaiKhoanFragment themTaiKhoanFragment = new ThemTaiKhoanFragment();
        themTaiKhoanFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_layout, fragment).addToBackStack(null).commit();
    }
}
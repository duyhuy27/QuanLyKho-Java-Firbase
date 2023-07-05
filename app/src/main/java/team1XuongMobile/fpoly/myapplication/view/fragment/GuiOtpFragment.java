package team1XuongMobile.fpoly.myapplication.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthProvider;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentGuiOtpBinding;


public class GuiOtpFragment extends Fragment {

    private FragmentGuiOtpBinding binding;

    private String username, email, password, passwordConfirm;

    public static final String KEY_USERNAME = "userName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public static final String TAG = "GuiOTPFragment";
    public static final String SDT = "key_sdt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuiOtpBinding.inflate(inflater, container, false);

        nhanDataTruyenSang();

        listener(); // hàm xử lý view khi người dùng click


        return binding.getRoot();
    }

    private void nhanDataTruyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString(KEY_USERNAME);
            email = bundle.getString(KEY_EMAIL);
            password = bundle.getString(KEY_PASSWORD);
            Log.d(TAG, "nhanDataTruyenSang: email: " + email + " password: " + password + "username" + username);
        }
    }

    private void listener() {
        binding.ccp.registerCarrierNumberEditText(binding.edtSoDienThoai);
        binding.buttonGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.ccp.isValidFullNumber()) {
                    binding.edtSoDienThoai.setError("Số điện thoại không tồn tại");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(SDT, binding.ccp.getFullNumberWithPlus());
                bundle.putString(KEY_EMAIL, email);
                bundle.putString(KEY_USERNAME, username);
                bundle.putString(KEY_PASSWORD, password);

                XacMinhOtpFragment fragment = new XacMinhOtpFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_layout, fragment).addToBackStack(null).commit();
            }
        });
        binding.buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}
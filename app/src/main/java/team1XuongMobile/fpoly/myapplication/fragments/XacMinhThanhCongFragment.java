package team1XuongMobile.fpoly.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team1XuongMobile.fpoly.myapplication.view.MainActivity;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentXacMinhThanhCongBinding;


public class XacMinhThanhCongFragment extends Fragment {

    private FragmentXacMinhThanhCongBinding binding;

    public static final String KEY_EMAIL = "email";

    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentXacMinhThanhCongBinding.inflate(inflater, container, false);

        nhanDuLieuTruyenSang();

        listener();

        return binding.getRoot();
    }

    private void nhanDuLieuTruyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString(KEY_EMAIL);
            binding.tvEmail.setText("Tài khoản của bạn đã được tạo thành công với địa chỉ Email: " + email);
        }
    }

    private void listener() {
        binding.buttonTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
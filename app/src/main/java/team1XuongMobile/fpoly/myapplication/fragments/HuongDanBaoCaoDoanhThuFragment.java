package team1XuongMobile.fpoly.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentHuongDanBaoCaoDoanhThuBinding;


public class HuongDanBaoCaoDoanhThuFragment extends Fragment {


    private FragmentHuongDanBaoCaoDoanhThuBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHuongDanBaoCaoDoanhThuBinding.inflate(inflater, container, false);

        userClick();

        return binding.getRoot();
    }

    private void userClick() {
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
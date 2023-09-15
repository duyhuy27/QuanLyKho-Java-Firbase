package team1XuongMobile.fpoly.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentHuongDanBaoCaoLaiLoBinding;


public class HuongDanBaoCaoLaiLoFragment extends Fragment {

   private FragmentHuongDanBaoCaoLaiLoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHuongDanBaoCaoLaiLoBinding.inflate(inflater, container, false);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }
}
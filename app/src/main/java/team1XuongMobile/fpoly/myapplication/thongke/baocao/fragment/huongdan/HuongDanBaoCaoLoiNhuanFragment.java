package team1XuongMobile.fpoly.myapplication.thongke.baocao.fragment.huongdan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentHuongDanBaoCaoLoiNhuanBinding;


public class HuongDanBaoCaoLoiNhuanFragment extends Fragment {

    private FragmentHuongDanBaoCaoLoiNhuanBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHuongDanBaoCaoLoiNhuanBinding.inflate(inflater, container, false);

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
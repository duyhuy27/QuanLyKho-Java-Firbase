package team1XuongMobile.fpoly.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentSanPhamBinding;


public class SanPhamFragment extends Fragment {

    private FragmentSanPhamBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSanPhamBinding.inflate(inflater, container, false);

        listener(); // hàm xử lý sự kiện khi người dùng lick vào view

        return binding.getRoot();
    }

    private void listener() {
        binding.cardviewThemSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new ThemSPFragment()).addToBackStack(null).commit();
            }
        });

        binding.tvDsSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new DanhSachSPFragment()).addToBackStack(null).commit();
            }
        });

        binding.tvNcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new NhaCungCapFragment()).addToBackStack(null).commit();

            }
        });

        binding.tvDvvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new VanChuyenFragment()).addToBackStack(null).commit();
            }
        });

        binding.tvTaoQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new TaoQRFragment()).addToBackStack(null).commit();

            }
        });

        binding.tvPhieuNh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new PhieuNhapFragment()).addToBackStack(null).commit();

            }
        });

    }
}
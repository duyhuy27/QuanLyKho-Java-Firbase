package team1XuongMobile.fpoly.myapplication.sanpham;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentSanPhamBinding;
import team1XuongMobile.fpoly.myapplication.sanpham.chat.ChatGptFragment;


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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new ChatGptFragment()).addToBackStack(null).commit();

            }
        });
    }
}
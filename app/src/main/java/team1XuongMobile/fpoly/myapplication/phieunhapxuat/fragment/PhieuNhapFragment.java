package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import team1XuongMobile.fpoly.myapplication.R;

public class PhieuNhapFragment extends Fragment {
    private FloatingActionButton fab_themPhieuNhap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieu_nhap, container, false);
        fab_themPhieuNhap = view.findViewById(R.id.fab_themPhieuNhap);
        fab_themPhieuNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickThemPhieuNhap();
            }
        });
        return view;
    }

    public void onclickThemPhieuNhap() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, new ChonDanhSachNCCFragment()).
                addToBackStack(null).commit();
    }
}

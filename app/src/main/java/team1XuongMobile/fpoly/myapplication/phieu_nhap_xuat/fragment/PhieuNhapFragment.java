package team1XuongMobile.fpoly.myapplication.phieu_nhap_xuat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import team1XuongMobile.fpoly.myapplication.R;

public class PhieuNhapFragment extends Fragment {
    FloatingActionButton fab_themPhieuNhap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieu_nhap, container, false);
        fab_themPhieuNhap = view.findViewById(R.id.fab_themPhieuNhap);
        fab_themPhieuNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_phieu_nhap, new TaoHDNFragment())
                        .addToBackStack(null).commit();
            }
        });
        return view;
    }
}
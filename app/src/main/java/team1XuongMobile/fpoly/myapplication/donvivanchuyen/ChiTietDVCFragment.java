package team1XuongMobile.fpoly.myapplication.donvivanchuyen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import team1XuongMobile.fpoly.myapplication.R;


public class ChiTietDVCFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chi_tiet_d_v_c, container, false);
    }
}
package team1XuongMobile.fpoly.myapplication.nhacungcap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import team1XuongMobile.fpoly.myapplication.R;


public class DanhSachNCCFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_danh_sach_n_c_c, container, false);
    }
}
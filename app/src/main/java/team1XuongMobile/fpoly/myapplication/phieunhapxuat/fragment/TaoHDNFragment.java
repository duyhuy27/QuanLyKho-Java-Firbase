package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team1XuongMobile.fpoly.myapplication.R;

public class TaoHDNFragment extends Fragment {
    TextView chonSanPham;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_h_d_n, container, false);
        chonSanPham = view.findViewById(R.id.tv_chonSanPham);
        chonSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                View viewDialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_chon_san_pham, null);
                builder.setView(viewDialog);
                builder.show();
            }
        });
        return view;
    }
}
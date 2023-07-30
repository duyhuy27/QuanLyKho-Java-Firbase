package team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.adapter.PhieuXuatAdapter;

public class PhieuXuatFragment extends Fragment {
    private RecyclerView rcvPhieuXuat;
    private FloatingActionButton fabPhieuXuat;
    private PhieuXuatAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieu_xuat, container, false);
        bindViews(view);
        setupUI();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindViews(View view) {
        rcvPhieuXuat = view.findViewById(R.id.rcv_phieu_xuat);
        fabPhieuXuat = view.findViewById(R.id.fab_themPhieuXuat);
    }

    private void setupUI() {
        fabPhieuXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, new TaoHDXFragment(), "TaoHDXFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
package team1XuongMobile.fpoly.myapplication.thongke;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import team1XuongMobile.fpoly.myapplication.R;


public class ThongKeFragment extends Fragment {
    TabLayout tabLayoutThongke;
    ViewPager2 viewPagerThongke;
    TabLayout_ThongKeAdapter tabLayout_thongKeAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke, container, false);
        tabLayoutThongke = view.findViewById(R.id.tablayout_thongke);
        viewPagerThongke = view.findViewById(R.id.viewpager_thongke);
        tabLayout_thongKeAdapter = new TabLayout_ThongKeAdapter(ThongKeFragment.this);
        viewPagerThongke.setAdapter(tabLayout_thongKeAdapter);
        new TabLayoutMediator(tabLayoutThongke, viewPagerThongke, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("DOANH THU");
                        break;
                    case 1:
                        tab.setText("TỔNG QUAN");
                        break;
                }
            }
        }).attach();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
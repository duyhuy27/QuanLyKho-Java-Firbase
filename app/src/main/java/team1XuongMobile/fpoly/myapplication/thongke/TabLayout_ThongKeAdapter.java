package team1XuongMobile.fpoly.myapplication.thongke;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabLayout_ThongKeAdapter extends FragmentStateAdapter {
    public TabLayout_ThongKeAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TongQuanFragment();
            case 1:
                return new DoanhThu_ThongKeFragment();
            default:
                return new TongQuanFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

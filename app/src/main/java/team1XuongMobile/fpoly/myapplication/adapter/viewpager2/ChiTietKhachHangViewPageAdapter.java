package team1XuongMobile.fpoly.myapplication.adapter.viewpager2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import team1XuongMobile.fpoly.myapplication.fragments.ChiTietKhachHangFragment;
import team1XuongMobile.fpoly.myapplication.fragments.LichSuChiTietKhachHangVFragment;
import team1XuongMobile.fpoly.myapplication.fragments.ThongTinChiTietKhachHangVFragment;

public class ChiTietKhachHangViewPageAdapter extends FragmentStateAdapter {

    public ChiTietKhachHangViewPageAdapter(@NonNull ChiTietKhachHangFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ThongTinChiTietKhachHangVFragment();
            case 1:
                return new LichSuChiTietKhachHangVFragment();


            default:
                return new ThongTinChiTietKhachHangVFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

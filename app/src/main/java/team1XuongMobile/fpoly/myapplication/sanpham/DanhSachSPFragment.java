package team1XuongMobile.fpoly.myapplication.sanpham;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.databinding.FragmentDanhSachSPBinding;


public class DanhSachSPFragment extends Fragment {

    private FragmentDanhSachSPBinding binding;

    private ViewPagerAdapter viewPagerAdapter;
    private FirebaseAuth firebaseAuth; // f


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDanhSachSPBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        configViewPager(binding.viewpager2);


        return binding.getRoot();
    }

    private void configViewPager(ViewPager viewpager2) {

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ArrayList<DanhSachSanPhamTheoMucFragment> fragmentArrayList = new ArrayList<>();
        private ArrayList<String> fragmentTitleList = new ArrayList<>();
        private Context context;

        public ViewPagerAdapter(@NonNull FragmentManager fm, Context context, int behavior) {
            super(fm, behavior);
            this.context = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        private void addFragment(DanhSachSanPhamTheoMucFragment fragment, String title) {
            fragmentArrayList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
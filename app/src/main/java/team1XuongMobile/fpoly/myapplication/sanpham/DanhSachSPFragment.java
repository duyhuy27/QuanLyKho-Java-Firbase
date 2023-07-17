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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.Model.LoaiSanPham;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentDanhSachSPBinding;


public class DanhSachSPFragment extends Fragment {

    private FragmentDanhSachSPBinding binding;

    private ViewPagerAdapter viewPagerAdapter;
    private FirebaseAuth firebaseAuth;

    private ArrayList<LoaiSanPham> loaiSanPhamArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDanhSachSPBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        configViewPager(binding.viewpager2);
        binding.tabLayout.setupWithViewPager(binding.viewpager2);

        binding.buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return binding.getRoot();
    }

    private void configViewPager(ViewPager viewpager2) {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getContext(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        loaiSanPhamArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loai_sp");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loaiSanPhamArrayList.clear();
                LoaiSanPham objectAll = new LoaiSanPham("01", "Tất cả", true, "", 1);

                loaiSanPhamArrayList.add(objectAll);

                viewPagerAdapter.addFragment(DanhSachSanPhamTheoMucFragment.newInstance(
                                "" + objectAll.getId_loai_sp(),
                                "" + objectAll.getTen_loai_sp(),
                                "" + objectAll.getUid()),
                        objectAll.getTen_loai_sp());

                viewPagerAdapter.notifyDataSetChanged();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    LoaiSanPham loaiSanPham = ds.getValue(LoaiSanPham.class);
                    loaiSanPhamArrayList.add(loaiSanPham);

                    viewPagerAdapter.addFragment(DanhSachSanPhamTheoMucFragment.newInstance(
                            "" + loaiSanPham.getId_loai_sp(),
                            "" + loaiSanPham.getTen_loai_sp(),
                            "" + loaiSanPham.getUid()), loaiSanPham.getTen_loai_sp()
                    );
                    viewPagerAdapter.notifyDataSetChanged();
                    viewpager2.setAdapter(viewPagerAdapter);

                    applyTabSpacing();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewpager2.setAdapter(viewPagerAdapter);

    }

    private void applyTabSpacing() {
        ViewGroup slidingTabStrip = (ViewGroup) binding.tabLayout.getChildAt(0);
        int tabCount = slidingTabStrip.getChildCount();
        int spacing = 20; // Adjust the spacing value as per your requirement

        for (int i = 0; i < tabCount; i++) {
            View tabView = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
            params.setMarginStart(spacing);
            params.setMarginEnd(spacing);
            tabView.requestLayout();
        }
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
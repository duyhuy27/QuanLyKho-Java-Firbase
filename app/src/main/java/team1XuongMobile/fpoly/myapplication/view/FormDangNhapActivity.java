package team1XuongMobile.fpoly.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.ActivityFormDangNhapBinding;
import team1XuongMobile.fpoly.myapplication.view.fragment.MainFragment;

public class FormDangNhapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFormDangNhapBinding binding = ActivityFormDangNhapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment();


    }

    private void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame_layout, new MainFragment()).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
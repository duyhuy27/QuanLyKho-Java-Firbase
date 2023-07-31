package team1XuongMobile.fpoly.myapplication.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentXoaTaiKhoanBinding;


public class XoaTaiKhoanFragment extends Fragment {

    private FragmentXoaTaiKhoanBinding binding;

    public static final String TAG = "XoaTaiKhoanFragment";

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentXoaTaiKhoanBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading...");


        userClick();


        return binding.getRoot();
    }

    private void userClick() {
        binding.buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        binding.buttonDieuDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFormGoogle();
            }
        });
    }

    private void openFormGoogle() {
        String surveyFormUrl = "https://forms.gle/E3sqeHeNmEFDLgcn9";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(surveyFormUrl));
        startActivity(intent);
    }}

package team1XuongMobile.fpoly.myapplication.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.MainActivity;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentMainBinding;


public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    public static final String TAG = "MainFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        checkRoleUser();

        listener(); // hàm xử lý các button hoặc các view khi người dùng click vào

        return binding.getRoot();
    }

    private void checkRoleUser() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            progressDialog.dismiss();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_layout, new MainFragment()).addToBackStack(null).commit();
        } else {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String role = "" + snapshot.child("vaiTro").getValue();

                            if (role.equals("nhanVien")) {
                                progressDialog.dismiss();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            } else if (role.equals("admin")) {
                                progressDialog.dismiss();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void listener() {
        binding.buttonDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_layout, new DangKyFragment()).addToBackStack(null).commit();
            }
        });

        binding.buttonDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_layout, new DangNhapFragment()).addToBackStack(null).commit();
            }
        });
    }
}
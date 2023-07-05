package team1XuongMobile.fpoly.myapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.MainActivity;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentDangNhapBinding;


public class DangNhapFragment extends Fragment {

    private FragmentDangNhapBinding binding;

    private FirebaseAuth firebaseAuth;

    private String email = "", password = "";

    public static final String TAG = "DangNhapFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        binding = FragmentDangNhapBinding.inflate(inflater, container, false);

        listener(); // hàm xử lý các sự kiện khi người dùng click

        return binding.getRoot();
    }

    private void listener() {
        binding.buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        binding.buttonDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

//        binding.tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.progressbar.setVisibility(View.VISIBLE);
//                email = binding.edtEmail.getText().toString().trim();
//                firebaseAuth.sendPasswordResetEmail(email)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                binding.progressbar.setVisibility(View.GONE);
//                                Toast.makeText(getContext(), "Đã gửi email đổi mật khẩu tới địa chỉ email của bạn", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                binding.progressbar.setVisibility(View.GONE);
//                                Toast.makeText(getContext(), "Gửi email đổi mật khẩu thất bại vì " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });

        binding.tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_layout, new QuenMatKhauFragment()).addToBackStack(null).commit();
            }
        });

    }

    private void validateData() {
        email = binding.edtEmail.getText().toString().trim();
        password = binding.edtPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Địa chỉ email không hợp lệ");
            binding.edtEmail.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            binding.edtPassword.setError("Mật khẩu không được để trống");
            binding.edtPassword.requestFocus();
            return;
        } else {
            dangNhap();
        }
    }

    private void dangNhap() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.buttonDangNhap.setVisibility(View.GONE);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkVaiTro();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressbar.setVisibility(View.GONE);
                        binding.buttonDangNhap.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Lỗi " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: Lỗi không thể đăng nhập " + e.getMessage());
                    }
                });
    }

    private void checkVaiTro() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.buttonDangNhap.setVisibility(View.GONE);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String role = "" + snapshot.child("vaiTro").getValue();

                        if (role.equals("nhanVien")) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        } else if (role.equals("admin")) {
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
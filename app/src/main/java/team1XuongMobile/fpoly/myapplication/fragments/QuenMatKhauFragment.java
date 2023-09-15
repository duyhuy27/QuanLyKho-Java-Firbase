package team1XuongMobile.fpoly.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentQuenMatKhauBinding;


public class QuenMatKhauFragment extends Fragment {

    private FragmentQuenMatKhauBinding binding;

    private FirebaseAuth firebaseAuth;

    public static final String TAG = "QuenMatKhauFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuenMatKhauBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        listener();

        return binding.getRoot();
    }

    private void listener() {
        binding.buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        binding.buttonGuiEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    binding.edtEmail.setError("Vui lòng nhập email");
                    binding.edtEmail.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.edtEmail.setError("Email không hợp lệ");
                    binding.edtEmail.requestFocus();
                    return;
                } else {
                    guiEmailDeResetPassword(email);
                }

            }
        });
    }

    private void guiEmailDeResetPassword(String email) {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.buttonGuiEmail.setVisibility(View.GONE);

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.progressbar.setVisibility(View.GONE);
                        binding.buttonGuiEmail.setVisibility(View.VISIBLE);
                        binding.tvNoiDung.setText("Chúng tôi đã gửi cho bạn một email với đường link giúp bạn đổi lại mật khẩu, hãy làm theo và quay trở lại màn hình đăng nhập để tiếp tục đăng nhập với mật khẩu mới");
                        binding.tvNoiDung.setTextColor(getResources().getColor(R.color.green));
                        Toast.makeText(getContext(), "Gửi email thành công ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressbar.setVisibility(View.GONE);
                        binding.buttonGuiEmail.setVisibility(View.VISIBLE);
                        binding.tvNoiDung.setText("Có vẻ như có lỗi xảy ra. Vui lòng thử lại");
                        Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: Lỗi không gửi được mail " + e.getMessage());
                    }
                });
    }
}
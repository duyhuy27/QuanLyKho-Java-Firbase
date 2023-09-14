package team1XuongMobile.fpoly.myapplication.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import team1XuongMobile.fpoly.myapplication.MainActivity;
import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentHoSoBinding;
import team1XuongMobile.fpoly.myapplication.profile.DoiMatKhauFragment;
import team1XuongMobile.fpoly.myapplication.profile.SuaHoSoFragment;
import team1XuongMobile.fpoly.myapplication.profile.XoaTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.view.FormDangNhapActivity;


public class HoSoFragment extends Fragment {

    private FragmentHoSoBinding binding;

    public static final String TAG = "HoSoFragment";

    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHoSoBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        
        loadDataFromFirebase();

        userClick();

        return binding.getRoot();
    }

    private void loadDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Accounts");
        databaseReference.child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = "" + snapshot.child("email").getValue();
                        String name = "" + snapshot.child("username").getValue();
                        String img = "" + snapshot.child("avatar").getValue();

                        binding.tvUsername.setText(name);
                        binding.tvEmail.setText(email);

                        try {
                            Picasso.get().load(img).placeholder(R.drawable.logo).error(R.drawable.logo)
                                    .into(binding.profileImage);
                        }
                        catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void userClick() {
        binding.tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Đăng xuất")
                        .setMessage("Đăng xuất khỏi " + firebaseAuth.getCurrentUser().getEmail() + " ?")
                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(getContext(), FormDangNhapActivity.class);
                                startActivity(intent);
                                requireActivity().finish();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        binding.doiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.layout_content, new DoiMatKhauFragment()).addToBackStack(null).commit();

            }
        });

        binding.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.layout_content, new SuaHoSoFragment()).addToBackStack(null).commit();
            }
        });

        binding.xoaTk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.layout_content, new XoaTaiKhoanFragment()).addToBackStack(null).commit();

            }
        });



    }
}
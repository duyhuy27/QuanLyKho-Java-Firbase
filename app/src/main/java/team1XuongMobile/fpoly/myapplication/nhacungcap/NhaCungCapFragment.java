package team1XuongMobile.fpoly.myapplication.nhacungcap;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentDanhSachNCCBinding;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentSuaNCCBinding;
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenAdapter;


public class NhaCungCapFragment extends Fragment implements NhaCungCapAdapter.chucNangInterfaceNhaCungCap{
    private FragmentDanhSachNCCBinding binding;
    private NhaCungCapAdapter adapter;
    private ArrayList<NhaCungCapModel> nhaCungCapModelArrayList;
    private NhaCungCapAdapter.chucNangInterfaceNhaCungCap chucNangInterfaceNhaCungCap;
    public static final String TAG = "NhaCungCapFragment";
    public static final String KEY_ID = "id";
    private String kh = "";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDanhSachNCCBinding.inflate(inflater, container,false);
        nhaCungCapModelArrayList = new ArrayList<>();
        chucNangInterfaceNhaCungCap = this;
        laydulieudangnhap();
        loadDataFirebase();

        listener();
        return binding.getRoot();

    }
    private void listener() {
        binding.buttonThemMoiNCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new ThemNCCFragment()).addToBackStack(null).commit();
            }
        });
        binding.edTimKiemNCC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    adapter.getFilter().filter(charSequence);

                }catch (Exception e) {
                    Log.d(TAG, "onTextChanged: loi search"+ e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    private void loadDataFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
        ref.orderByChild("kh").equalTo(kh)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nhaCungCapModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NhaCungCapModel nhaCungCapModel = dataSnapshot.getValue(NhaCungCapModel.class);
                    nhaCungCapModelArrayList.add(nhaCungCapModel);
                }
                adapter = new NhaCungCapAdapter(nhaCungCapModelArrayList,getContext(),chucNangInterfaceNhaCungCap);
                binding.rcvDanhSachNCC.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: loi load du lieu tu firebase:"+ error.getMessage());
            }
        });
    }

    @Override
    public void update(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        Log.d(TAG, "update: id" + id);
        SuaNCCFragment suaNCCFragment = new SuaNCCFragment();
        suaNCCFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,  suaNCCFragment).addToBackStack(null).commit();
    }

    @Override
    public void delete(String id) {

        dialogXacNhanXoa(id);

    }

    @Override
    public void xemChiTiet(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        ChiTietNCCFragment chiTietNCCFragment = new ChiTietNCCFragment();
        chiTietNCCFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,  chiTietNCCFragment).addToBackStack(null).commit();
    }

    @Override
    public void goiClick(String sdt) {
        Uri phoneUri = Uri.parse("tel:" + sdt);
        Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
        startActivity(intent);
    }

    private void dialogXacNhanXoa(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bạn có chắc muốn xóa không ?");
        builder.setPositiveButton("chắc chắn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
                ref.child(id).removeValue();
                Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
                
            }
        });
        builder.setNegativeButton("không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Bạn chọn không xóa", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();

            }
        });


        builder.show();
    }
    public void laydulieudangnhap() {
        FirebaseUser firebaseUser;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kh  = "" + snapshot.child("kh").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
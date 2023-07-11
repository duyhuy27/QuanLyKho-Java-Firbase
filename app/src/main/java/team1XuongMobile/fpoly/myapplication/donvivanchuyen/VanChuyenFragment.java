package team1XuongMobile.fpoly.myapplication.donvivanchuyen;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentDanhSachDVCBinding;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentVanChuyenBinding;
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.ThemDVCFragment;
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenAdapter;
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenModel;


public class VanChuyenFragment extends Fragment implements VanChuyenAdapter.chucNangInterfaceVanChuyen {
    private FragmentDanhSachDVCBinding binding;
    private ArrayList<VanChuyenModel> danhSachDVCList;
    private VanChuyenAdapter adapter;
    private static final String TAG = "VanChuyenFragment";
    private VanChuyenAdapter.chucNangInterfaceVanChuyen chucNangInterfaceVanChuyen;
    public static final String KEY_ID = "id";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDanhSachDVCBinding.inflate(inflater, container, false);
        chucNangInterfaceVanChuyen = this;
        listener();
        loadDataFireBase();






        return binding.getRoot();
    }
    public void listener() {
        binding.edTimKiemDVC.addTextChangedListener(new TextWatcher() {
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
        binding.buttonThemMoiDVC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, new ThemDVCFragment()).addToBackStack(null).commit();
            }
        });
    }
    public void loadDataFireBase() {
        danhSachDVCList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("don_vi_vc");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhSachDVCList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    VanChuyenModel model = ds.getValue(VanChuyenModel.class);
                    danhSachDVCList.add(model);
                }
                adapter = new VanChuyenAdapter(danhSachDVCList,getContext(),chucNangInterfaceVanChuyen);
                binding.rcvDanhSachDVC.setAdapter(adapter);
                Log.d(TAG, "onDataChange: "+ danhSachDVCList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: lỗi load dũ liệu firebase: " + error.getMessage());
            }
        });
    }

    @Override
    public void update(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        SuaDVCFragment suaDVCFragment = new SuaDVCFragment();
        suaDVCFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, suaDVCFragment).addToBackStack(null).commit();
    }

    @Override
    public void delete(String id) {
        dialogXacNhanXoa(id);
    }

    @Override
    public void xemChiTiet(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        ChiTietDVCFragment chiTietDVCFragment = new ChiTietDVCFragment();
        chiTietDVCFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, chiTietDVCFragment).addToBackStack(null).commit();
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
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("don_vi_vc");
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
}
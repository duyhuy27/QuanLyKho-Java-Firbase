package team1XuongMobile.fpoly.myapplication.sanpham;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentThemSPBinding;


public class ThemSPFragment extends Fragment implements ThuocTinhAdapter.thuocTinhInterface {

    private FragmentThemSPBinding binding;

    private FirebaseAuth firebaseAuth;

    public static final String TAG = "ThemSPFragment";

    private Uri img_uri;

    private boolean thueAvailable = false;

    private boolean trangThaiAvailable = false;

    private HashMap<String, Object> attributesHashMap;

    private ArrayList<String> idNccList, tenNccList;

    private ArrayList<String> idLspList, tenLspList;

    private String chonIdNcc, chonTenNcc;

    private String chonIdLsp, chonTenLsp;

    private ArrayList<ThuocTinhModels> thuocTinhModelsArrayList;

    private ThuocTinhAdapter thuocTinhAdapter;

    private ProgressDialog progressDialog;

    private ThuocTinhAdapter.thuocTinhInterface thuocTinhInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentThemSPBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        thuocTinhInterface = this;

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        loadDuLieuNCC();

        loadDuLieuLSP();

        loadDuLieuThuocTinh();

        listener();

        return binding.getRoot();
    }


    private void listener() {
        binding.cardPickerCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialocChonAnh();
            }
        });

        binding.buttonSwitchThue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.liner01.setVisibility(View.VISIBLE);
                } else {
                    binding.liner01.setVisibility(View.GONE);
                }
            }
        });

        binding.buttonAddThuocTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogAddThuocTinh();
            }
        });

        binding.buttonHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        binding.edtLsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChonLSP();
            }
        });

        binding.edtNcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChonNCC();
            }
        });


    }

    private String tenSp = "", maSp = "", khoiLuong = "", thueDauRa = "", thueDauVao = "", giaGoc = "", giaBan = "", moTa = "";

    private void validateData() {
        thueAvailable = binding.buttonSwitchThue.isChecked();
        trangThaiAvailable = binding.buttonSwitchTrangthai.isChecked();

        tenSp = binding.edtTenSp.getText().toString().trim();
        maSp = binding.edtMaSp.getText().toString().trim();
        khoiLuong = binding.edtKhoiLuong.getText().toString().trim();
        giaGoc = binding.edtGiagoc.getText().toString().trim();
        giaBan = binding.edtGiaban.getText().toString().trim();
        moTa = binding.tvMota.getText().toString().trim();

        if (tenSp.isEmpty() || maSp.isEmpty() || khoiLuong.isEmpty() || giaGoc.isEmpty() || giaBan.isEmpty() || moTa.isEmpty()) {
            Toast.makeText(getContext(), "Mời bạn nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;

        } else if (chonTenNcc.isEmpty() || chonTenLsp.isEmpty()) {
            Toast.makeText(getContext(), "Bạn hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        } else if (!giaGoc.matches("\\d+")) {
            Toast.makeText(getContext(), "Giá chỉ được nhập số", Toast.LENGTH_SHORT).show();
            return;
        } else if (!giaBan.matches("\\d+")) {
            Toast.makeText(getContext(), "Giá chỉ được nhập số", Toast.LENGTH_SHORT).show();
            return;
        } else if (thueAvailable) {
            thueDauRa = binding.edtThueDaura.getText().toString().trim();
            thueDauVao = binding.edtThueDauvao.getText().toString().trim();
            if (thueDauRa.isEmpty() || thueDauVao.isEmpty()) {
                Toast.makeText(getContext(), "Mời bạn nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            } else if (!thueDauRa.matches("\\d+")) {
                Toast.makeText(getContext(), "Thuế chỉ được nhập số", Toast.LENGTH_SHORT).show();
                return;
            } else if (!thueDauVao.matches("\\d+")) {
                Toast.makeText(getContext(), "Thuế chỉ được nhập số", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {
            thueDauRa = "0";
            thueDauVao = "0";
        }

        luuDuLieuSpLenFirebase();
    }

    private void luuDuLieuSpLenFirebase() {
        progressDialog.setTitle("Xin Đợi...");
        progressDialog.show();

        String timestamp = "" + System.currentTimeMillis();

        if (img_uri == null) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String path = "sanpham_img/" + "" + timestamp;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(path);
            storageReference.putFile(img_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            while (!task.isSuccessful()) ;
                            Uri downloadUri = task.getResult();
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                // Generate a unique product ID
                                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("SanPham");
                                String productId = productsRef.push().getKey();

                                // Save the product to the database
                                HashMap<String, Object> productData = new HashMap<>();
                                productData.put("idSanPham", "" + productId);
                                productData.put("tenSp", "" + tenSp);
                                productData.put("maSp", "" + maSp);
                                productData.put("khoiLuong", "" + khoiLuong);
                                productData.put("giaNhap", "" + giaGoc);
                                productData.put("giaBan", "" + giaBan);
                                productData.put("thueAvailable", "" + thueAvailable);
                                productData.put("thueDauRa", "" + thueDauRa);
                                productData.put("thueDauVao", "" + thueDauVao);
                                productData.put("trangThaiAvailable", "" + trangThaiAvailable);
                                productData.put("ten_loai", "" + chonTenLsp);
                                productData.put("id_loai", "" + chonIdLsp);
                                productData.put("id_nha_cc", "" + chonIdNcc);
                                productData.put("ten_nha_cc", "" + chonTenNcc);
                                productData.put("uid", firebaseUser.getUid());
                                productData.put("codeIme", "");
                                productData.put("timestamp","" + timestamp);
                                productData.put("img", "" + downloadUri);

                                productsRef.child(productId).setValue(productData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DatabaseReference attributesRef = FirebaseDatabase.getInstance().getReference("attributes");
                                                for (ThuocTinhModels thuocTinhModel : thuocTinhModelsArrayList) {
                                                    // Generate a unique attribute ID
                                                    String attributeId = attributesRef.push().getKey();

                                                    // Create a HashMap to store attribute details
                                                    HashMap<String, Object> attributeData = new HashMap<>();
                                                    attributeData.put("product_id", productId);
                                                    attributeData.put("ten_tt", thuocTinhModel.getTen_tt());
                                                    attributeData.put("gia_tri_tt", thuocTinhModel.getGia_tri_tt());

                                                    // Save the attribute to the database
                                                    attributesRef.child(attributeId).setValue(attributeData);
                                                }

                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                                // Reset the input fields and clear the attributes list
                                                thuocTinhModelsArrayList.clear();
                                                thuocTinhAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "onFailure: thêm sản phẩm thất bại " + e.getMessage());
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: không thể tại ảnh lên vì " + e.getMessage());
                        }
                    });
        }
    }

    private void showDialogChonNCC() {
        String[] nccArr = new String[tenNccList.size()];

        for (int i = 0; i < tenNccList.size(); i++) {
            nccArr[i] = tenNccList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn nhà cung cấp")
                .setItems(nccArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        chonIdNcc = idNccList.get(i);
                        chonTenNcc = tenNccList.get(i);

                        binding.edtNcc.setText(chonTenNcc);

                        Log.d(TAG, "onClick: Id và tên nhà cung cấp mà người dùng đã chọn " + chonIdNcc + chonTenNcc);
                    }
                })
                .show();
    }

    private void showDialogChonLSP() {
        String[] lspArr = new String[tenLspList.size()];

        for (int i = 0; i < tenLspList.size(); i++) {
            lspArr[i] = tenLspList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn loại sản phẩm")
                .setItems(lspArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        chonIdLsp = idLspList.get(i);
                        chonTenLsp = tenLspList.get(i);

                        binding.edtLsp.setText(chonTenLsp);

                        Log.d(TAG, "onClick: Id và tên loại sản phẩm mà người dùng đã chọn " + chonIdLsp + chonTenLsp);
                    }
                })
                .show();
    }

    private void loadDuLieuNCC() {
        idNccList = new ArrayList<>();
        tenNccList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nha_cung_cap");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                idNccList.clear();
                tenNccList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = "" + ds.child("id_nha_cc").getValue();
                    String ten = "" + ds.child("ten_nha_cc").getValue();

                    idNccList.add(id);
                    tenNccList.add(ten);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadDuLieuLSP() {
        tenLspList = new ArrayList<>();
        idLspList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loai_sp");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenLspList.clear();
                idLspList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = "" + ds.child("id_loai").getValue();
                    String ten = "" + ds.child("ten_loai").getValue();

                    idLspList.add(id);
                    tenLspList.add(ten);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadDuLieuThuocTinh() {
        thuocTinhModelsArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("thuoc_tinh");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                thuocTinhModelsArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ThuocTinhModels thuocTinhModels = dataSnapshot.getValue(ThuocTinhModels.class);
                    thuocTinhModelsArrayList.add(thuocTinhModels);
                }
                thuocTinhAdapter = new ThuocTinhAdapter(getContext(), thuocTinhModelsArrayList, thuocTinhInterface);
                binding.rcvThuoctinhSp.setAdapter(thuocTinhAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialocChonAnh() {
        String[] options = {"Máy Ảnh", "Thư Viện"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // người dùng chọn máy ảnh
                            pickMayAnhFuntion();
                        } else if (i == 1) {
                            // người dùng chọn thư viện
                            pickThuVienFuntion();
                        }
                    }
                })
                .show();
    }

    private void pickMayAnhFuntion() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Máy ảnh");
        contentValues.put(MediaStore.Images.Media.TITLE, "Máy ảnh");
        img_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, img_uri);
        cameraActivityResult.launch(intent);
    }

    private void pickThuVienFuntion() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResult.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();

                        try {
                            Picasso.get().load(img_uri).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera)
                                    .into(binding.cardPickerCamera);
                        } catch (Exception e) {
                            Log.d(TAG, "onActivityResult: Không thể load ảnh " + e.getMessage());
                        }
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> galleryActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();

                        img_uri = intent.getData();

                        try {
                            Picasso.get().load(img_uri).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera)
                                    .into(binding.cardPickerCamera);
                        } catch (Exception e) {
                            Log.d(TAG, "onActivityResult: Không thể load ảnh " + e.getMessage());
                        }
                    }
                }
            }
    );

    private void showDialogAddThuocTinh() {
        Dialog dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.dialog_add_thuoc_tinh);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.AndroidAnimation;

        AppCompatButton buttonThoat = dialog.findViewById(R.id.button_thoat);
        AppCompatButton buttonThem = dialog.findViewById(R.id.button_them);

        EditText edtTenTT = dialog.findViewById(R.id.edt_ten_tt);
        EditText edtGiaTri = dialog.findViewById(R.id.edt_giatri_tt);

        buttonThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                long timestamp = System.currentTimeMillis();

                String tenTT = edtTenTT.getText().toString().trim();
                String giaTriTT = edtGiaTri.getText().toString().trim();

                if (TextUtils.isEmpty(tenTT) || TextUtils.isEmpty(giaTriTT)) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    progressDialog.show();

                    attributesHashMap = new HashMap<>();
                    attributesHashMap.put("id", "" + timestamp);
                    attributesHashMap.put("uid", firebaseUser.getUid());
                    attributesHashMap.put("ten_tt", "" + tenTT);
                    attributesHashMap.put("gia_tri_tt", "" + giaTriTT);
                    attributesHashMap.put("timestamp", timestamp);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("thuoc_tinh");
                    ref.child("" + timestamp)
                            .setValue(attributesHashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();
                                    loadDuLieuThuocTinh();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: theem thông tin thất bại vì" + e.getMessage());
                                }
                            });
                }
            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDuLieuThuocTinh();
    }

    @Override
    public void xoaThuocTinh(String id) {
        progressDialog.setTitle("Đợi...");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("thuoc_tinh");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        loadDuLieuThuocTinh();
                        Toast.makeText(getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
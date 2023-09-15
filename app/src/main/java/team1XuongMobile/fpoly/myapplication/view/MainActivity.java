package team1XuongMobile.fpoly.myapplication.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import team1XuongMobile.fpoly.myapplication.R;
import team1XuongMobile.fpoly.myapplication.fragments.KhachHangFragment;
import team1XuongMobile.fpoly.myapplication.fragments.LoaiSanPhamFragment;
import team1XuongMobile.fpoly.myapplication.fragments.ManHinhChinhFragment;
import team1XuongMobile.fpoly.myapplication.fragments.NhanVienFragment;
import team1XuongMobile.fpoly.myapplication.fragments.QuanLyTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.models.KhachHang;
import team1XuongMobile.fpoly.myapplication.fragments.VanChuyenFragment;
import team1XuongMobile.fpoly.myapplication.models.VanChuyenModel;
import team1XuongMobile.fpoly.myapplication.fragments.NhaCungCapFragment;
import team1XuongMobile.fpoly.myapplication.fragments.PhieuNhapFragment;
import team1XuongMobile.fpoly.myapplication.fragments.PhieuXuatFragment;
import team1XuongMobile.fpoly.myapplication.fragments.TaoHDNFragment;
import team1XuongMobile.fpoly.myapplication.fragments.TaoHDXFragment;
import team1XuongMobile.fpoly.myapplication.interfaced.ChonDonViVanChuyenListener;
import team1XuongMobile.fpoly.myapplication.interfaced.ChonKhachHangListener;
import team1XuongMobile.fpoly.myapplication.models.ChonNCC;
import team1XuongMobile.fpoly.myapplication.interfaced.ChonNhaCungCapListener;
import team1XuongMobile.fpoly.myapplication.models.ChonSanPham;
import team1XuongMobile.fpoly.myapplication.interfaced.ChonSanPhamListener;
import team1XuongMobile.fpoly.myapplication.interfaced.ChonSanPhamXListener;
import team1XuongMobile.fpoly.myapplication.fragments.SanPhamFragment;
import team1XuongMobile.fpoly.myapplication.fragments.ThongKeFragment;
import team1XuongMobile.fpoly.myapplication.fragments.HoSoFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ChonNhaCungCapListener, ChonSanPhamListener, ChonSanPhamXListener, ChonKhachHangListener, ChonDonViVanChuyenListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TaoHDNFragment fragmentN;
    private TaoHDXFragment fragmentX;

    TextView ten_nguoidung;
    String tenstring, vaitrostring;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        replaceFragment(new ManHinhChinhFragment());
        firebaseAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navi);


//        recieveNotificationFromFirebase();
        View layout_header = navigationView.getHeaderView(0);
        ten_nguoidung = layout_header.findViewById(R.id.tv_ten_nguoi_dung_layout_header);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, 0, 0);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fragmentN = new TaoHDNFragment();
        fragmentX = new TaoHDXFragment();
        laydulieudangnhap();
        requestPer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ManHinhChinh_navi) {
            setTitle("Màn Hình Chính");
            replaceFragment(new ManHinhChinhFragment());
            drawerLayout.close();
        } else if (id == R.id.NhaCungCap_navi) {
            setTitle("Nhà Cung Cấp");
            replaceFragment(new NhaCungCapFragment());
            drawerLayout.close();
        } else if (id == R.id.VanChuyen_navi) {
            setTitle("Vận Chuyển");
            replaceFragment(new VanChuyenFragment());
            drawerLayout.close();
        } else if (id == R.id.KhachHang_navi) {
            setTitle("Khách Hàng");
            replaceFragment(new KhachHangFragment());
            drawerLayout.close();
        } else if (id == R.id.LoaiSanPham_navi) {
            setTitle("Loại Sản Phẩm");
            replaceFragment(new LoaiSanPhamFragment());
            drawerLayout.close();
        } else if (id == R.id.SanPham_navi) {
            setTitle("Sản Phẩm");
            replaceFragment(new SanPhamFragment());
            drawerLayout.close();
        } else if (id == R.id.PhieuNhap_navi) {
            setTitle("Phiếu Nhập");
            replaceFragment(new PhieuNhapFragment());
            drawerLayout.close();
        } else if (id == R.id.PhieuXuat_navi) {
            setTitle("Phiếu Xuất");
            replaceFragment(new PhieuXuatFragment()
            );
            drawerLayout.close();
        } else if (id == R.id.NhanVien_navi) {
            setTitle("Nhân Viên");
            replaceFragment(new NhanVienFragment());
            drawerLayout.close();
        } else if (id == R.id.QuanLyTaiKhoan_navi) {
            setTitle("Quản Lý Tài Khoản");
            replaceFragment(new QuanLyTaiKhoanFragment());
            drawerLayout.close();
        } else if (id == R.id.HoSoNhanVien_navi) {
            setTitle("Hồ Sơ Của Bạn");
            replaceFragment(new HoSoFragment());
            drawerLayout.close();
        } else if (id == R.id.ThongKe_navi) {
            setTitle("Báo cáo");
            replaceFragment(new ThongKeFragment());
            drawerLayout.close();
        } else if (id == R.id.DangXuat_navi) {
            setTitle("Nhà Cung Cấp");
            replaceFragment(new NhaCungCapFragment());
            drawerLayout.close();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setMessage("Bạn có muốn đăng xuất hay không");
            alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, FormDangNhapActivity.class);
                    startActivity(intent);
                    finish();


                }
            });
            alertDialog.show();
        }
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_content, fragment);
        transaction.commit();
    }

    @Override
    public void onClickChonNhaCungCap(ChonNCC objChonNCC) {
        Bundle bundleNhaCungCap = new Bundle();
        bundleNhaCungCap.putString("idNhaCungCap", objChonNCC.getId_nha_cc());
        fragmentN.setArguments(bundleNhaCungCap);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, fragmentN)
                .commit();
    }

    @Override
    public void onClickChonSanPham(ChonSanPham objChonSanPham) {
        Bundle bundleSanPham = new Bundle();
        bundleSanPham.putString("idSanPham", objChonSanPham.getIdSanPham());
        bundleSanPham.putBoolean("trangThaiChonSp", true);
        fragmentN.setArguments(bundleSanPham);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, fragmentN)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickChonSanPhamX(ChonSanPham objChonSanPham) {
        String idSanPham =objChonSanPham.getIdSanPham();
        boolean trangThai = true;
        fragmentX = (TaoHDXFragment) getSupportFragmentManager().findFragmentByTag("TaoHDXFragment");
        if (fragmentX != null) {
            fragmentX.nhanDuLieuChonSanPhamX(idSanPham, trangThai);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClickChonKhachHang(KhachHang objKhachHang) {
        String tenKhachHang = objKhachHang.getTen_kh();
        String idKhachHang = objKhachHang.getId_kh();
        fragmentX = (TaoHDXFragment) getSupportFragmentManager().findFragmentByTag("TaoHDXFragment");
        if (fragmentX != null) {
            fragmentX.nhanIdKhachHang(tenKhachHang,idKhachHang);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClickChonDonViVanChuyen(VanChuyenModel objVanChuyenModel) {
        String tenDonViVanChuyen = objVanChuyenModel.getTen();
        String idDonViVanChuyen = objVanChuyenModel.getId_don_vi_vc();
        fragmentX = (TaoHDXFragment) getSupportFragmentManager().findFragmentByTag("TaoHDXFragment");
        if (fragmentX != null) {
            fragmentX.nhanIdDonViVanChuyen(tenDonViVanChuyen,idDonViVanChuyen);
        }
        getSupportFragmentManager().popBackStack();
    }

    public void laydulieudangnhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenstring = "" + snapshot.child("username").getValue();
                vaitrostring = "" + snapshot.child("vaiTro").getValue();
                ten_nguoidung.setText(tenstring);

                if (vaitrostring.equals("nhanVien") == true) {

                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.menu_navigation_nhanvien);
                    Toast.makeText(MainActivity.this, "Bạn Đã Đăng Bằng Tài Khoản Nhân Viên", Toast.LENGTH_SHORT).show();
                } else {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.menu_navigation);
                    Toast.makeText(MainActivity.this, "Bạn Đã Đăng Bằng Tài Khoản Admin", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recieveNotificationFromFirebase() {


//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("FCM Token from Main Activity", "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
//
//                        Log.d("FCM Token from Main Activity", token);
//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
//                    }
//                });

        FirebaseMessaging.getInstance().subscribeToTopic("News")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });
    }

    private void requestPer() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            int permissions = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS);

            if (permissions != PackageManager.PERMISSION_GRANTED) {
                String[] NOTI_PERMISSIONS = {Manifest.permission.POST_NOTIFICATIONS};

                ActivityCompat.requestPermissions(this, NOTI_PERMISSIONS, 100);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
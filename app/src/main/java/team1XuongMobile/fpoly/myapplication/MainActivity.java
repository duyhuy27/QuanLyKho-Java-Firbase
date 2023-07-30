package team1XuongMobile.fpoly.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team1XuongMobile.fpoly.myapplication.Fragment.KhachHangFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPhamFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.ManHinhChinhFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVienFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoanFragment;
import team1XuongMobile.fpoly.myapplication.Model.KhachHang;
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenFragment;
import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenModel;
import team1XuongMobile.fpoly.myapplication.nhacungcap.NhaCungCapFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.PhieuNhapFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.PhieuXuatFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.TaoHDNFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.TaoHDXFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonDonViVanChuyenListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonKhachHangListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonNCC;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonNhaCungCapListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonSanPhamListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.listener.ChonSanPhamXListener;
import team1XuongMobile.fpoly.myapplication.sanpham.SanPhamFragment;
import team1XuongMobile.fpoly.myapplication.thongke.ThongKeFragment;
import team1XuongMobile.fpoly.myapplication.view.FormDangNhapActivity;
import team1XuongMobile.fpoly.myapplication.view.fragment.HoSoFragment;


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

        laydulieudangnhap();
        View layout_header = navigationView.getHeaderView(0);
        ten_nguoidung = layout_header.findViewById(R.id.tv_ten_nguoi_dung_layout_header);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, 0, 0);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fragmentN = new TaoHDNFragment();
        fragmentX = new TaoHDXFragment();

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
            setTitle("Thống Kê");
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
        Bundle bundleSanPhamX = new Bundle();
        bundleSanPhamX.putString("idSanPhamX", objChonSanPham.getIdSanPham());
        bundleSanPhamX.putBoolean("trangThaiChonSpX", true);
        fragmentX.setArguments(bundleSanPhamX);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, fragmentX)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickChonKhachHang(KhachHang objKhachHang) {
        Bundle bundleChonKhachHang = new Bundle();
        bundleChonKhachHang.putString("idKhachHang", objKhachHang.getId_kh());
        fragmentX.setArguments(bundleChonKhachHang);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, fragmentX)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickChonDonViVanChuyen(VanChuyenModel objVanChuyenModel) {
        Bundle bundleChonDonViVanChuyen = new Bundle();
        bundleChonDonViVanChuyen.putString("idDonViVanChuyen", objVanChuyenModel.getId_don_vi_vc());
        fragmentX.setArguments(bundleChonDonViVanChuyen);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, fragmentX)
                .addToBackStack(null)
                .commit();
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
                Log.d("quanquan", "vai tro dn " + vaitrostring);
                ten_nguoidung.setText(tenstring);
                if (vaitrostring.equals("nhanVien") == true) {
                    navigationView.getMenu().clear();
                    Log.d("quanquan", "chay dang nhap vao day ");
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
}
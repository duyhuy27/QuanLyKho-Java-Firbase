package team1XuongMobile.fpoly.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import team1XuongMobile.fpoly.myapplication.donvivanchuyen.VanChuyenFragment;

import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonNhaCungCapListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPhamListener;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.PhieuNhapFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.PhieuXuatFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.fragment.TaoHDNFragment;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonNCC;
import team1XuongMobile.fpoly.myapplication.phieunhapxuat.model.ChonSanPham;
import team1XuongMobile.fpoly.myapplication.view.fragment.HoSoFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.KhachHangFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.LoaiSanPhamFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.ManHinhChinhFragment;
import team1XuongMobile.fpoly.myapplication.nhacungcap.NhaCungCapFragment;
import team1XuongMobile.fpoly.myapplication.Fragment.NhanVienFragment;

import team1XuongMobile.fpoly.myapplication.Fragment.QuanLyTaiKhoanFragment;

import team1XuongMobile.fpoly.myapplication.sanpham.SanPhamFragment;

import team1XuongMobile.fpoly.myapplication.thongke.ThongKeFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ChonNhaCungCapListener, ChonSanPhamListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TaoHDNFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        replaceFragment(new ManHinhChinhFragment());
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navi);
        navigationView.inflateMenu(R.menu.menu_navigation);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, 0, 0);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new TaoHDNFragment();
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
            setTitle("Phiếu Nhập Xuất");
            replaceFragment(new PhieuNhapFragment());
            drawerLayout.close();
        } else if (id == R.id.PhieuXuat_navi) {
            setTitle("Phiếu Nhập Xuất");
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
        fragment.setArguments(bundleNhaCungCap);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickChonSanPham(ChonSanPham objChonSanPham) {
        Bundle bundleSanPham = new Bundle();
        bundleSanPham.putString("idSanPham", objChonSanPham.getIdSanPham());
        fragment.setArguments(bundleSanPham);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_content, fragment)
                .addToBackStack(null)
                .commit();
        fragment.loadDataFirebaseChonSanPham(objChonSanPham.getIdSanPham());
    }
}
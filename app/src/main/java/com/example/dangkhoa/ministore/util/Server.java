package com.example.dangkhoa.ministore.util;

/**
 * Created by Khoa on 6/2/2017.
 */

public class Server {
    public static  String localhost = "ministore.esy.es";
    public static String urlLoaiSP = "http://" + localhost + "/server/getLoaiSanPham.php";
    public static String urlSanPhamMoiNhat = "http://" + localhost + "/server/getSanPhamMoiNhat.php";
    public static String urlDienThoai = "http://" + localhost + "/server/getSanPham.php?page=";
    public static String urlDonHang = "http://" + localhost + "/server/thongTinKhachHang.php";
    public static String urlChiTietDonHang = "http://" + localhost + "/server/chiTietDonHang.php";
}

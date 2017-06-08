package com.example.dangkhoa.ministore.model;

import java.io.Serializable;

/**
 * Created by Khoa on 6/3/2017.
 */

public class sanPham implements Serializable{
    public int ID;
    public String tenSanPham;
    public Integer giaSanPham;
    public String hinhAnhSanPham;
    public String moTaSanPham;
    public int IDSanPham;

//    public sanPham(int ID, String tenSanPham, Integer giaSanPham, String hinhAnhSanPham, String moTaSanPham, int IDSanPham) {
//        this.ID = ID;
//        this.tenSanPham = tenSanPham;
//        this.giaSanPham = giaSanPham;
//        this.hinhAnhSanPham = hinhAnhSanPham;
//        this.moTaSanPham = moTaSanPham;
//        this.IDSanPham = IDSanPham;
//    }


    public sanPham(int ID, String tenSanPham, Integer giaSanPham, String hinhAnhSanPham, String moTaSanPham, int IDSanPham) {
        this.ID = ID;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
        this.hinhAnhSanPham = hinhAnhSanPham;
        this.moTaSanPham = moTaSanPham;
        this.IDSanPham = IDSanPham;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Integer getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(Integer giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public String getHinhAnhSanPham() {
        return hinhAnhSanPham;
    }

    public void setHinhAnhSanPham(String hinhAnhSanPham) {
        this.hinhAnhSanPham = hinhAnhSanPham;
    }

    public String getMoTaSanPham() {
        return moTaSanPham;
    }

    public void setMoTaSanPham(String moTaSanPham) {
        this.moTaSanPham = moTaSanPham;
    }

    public int getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(int IDSanPham) {
        this.IDSanPham = IDSanPham;
    }
}

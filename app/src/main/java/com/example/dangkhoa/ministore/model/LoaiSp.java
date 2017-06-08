package com.example.dangkhoa.ministore.model;

/**
 * Created by Dang Khoa on 5/26/2017.
 */

public class LoaiSp {
    public int id;
    public String TenLoaiSp;
    public String HinhAnhLoaiSp;


    public LoaiSp(int id, String tenLoaiSp, String hinhAnhLoaiSp) {
        this.id = id;
        TenLoaiSp = tenLoaiSp;
        HinhAnhLoaiSp = hinhAnhLoaiSp;
    }

    public int getId() {
        return id;
    }

    public String getTenLoaiSp() {
        return TenLoaiSp;
    }

    public String getHinhAnhLoaiSp() {
        return HinhAnhLoaiSp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTenLoaiSp(String tenLoaiSp) {
        TenLoaiSp = tenLoaiSp;
    }

    public void setHinhAnhLoaiSp(String hinhAnhLoaiSp) {
        HinhAnhLoaiSp = hinhAnhLoaiSp;
    }
}

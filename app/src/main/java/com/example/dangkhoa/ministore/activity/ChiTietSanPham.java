package com.example.dangkhoa.ministore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.model.gioHang;
import com.example.dangkhoa.ministore.model.sanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import static com.example.dangkhoa.ministore.activity.MainActivity.mangGioHang;

public class ChiTietSanPham extends AppCompatActivity {

    Toolbar toolbarCT;
    ImageView imageViewCT;
    TextView textViewTen, textViewGia, textViewMoTa;
    Spinner spinner;
    Button buttonDatMua;

    int id = 0;
    String tenCTSP = "";
    int giaCTSP = 0;
    String hinhanhCTSP = "";
    String motaCTSP = "";
    int idspCTSP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        AnhXa();
        ActionToolbar();
        getInformation();
        catchEventSpinner();
        eventButton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menucart:
                Intent intent = new Intent(getApplicationContext(),GioHang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void eventButton() {
        buttonDatMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mangGioHang.size() > 0){
                    int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exist = false;
                    for(int i = 0; i < mangGioHang.size(); i++){
                        if(MainActivity.mangGioHang.get(i).getIdsp() == id){
                            MainActivity.mangGioHang.get(i).setSoluongsp(MainActivity.mangGioHang.get(i).getSoluongsp() + sl);
                            if(MainActivity.mangGioHang.get(i).getSoluongsp() >= 10){
                                MainActivity.mangGioHang.get(i).setSoluongsp(10);
                            }
                            MainActivity.mangGioHang.get(i).setGiasp(giaCTSP*MainActivity.mangGioHang.get(i).getSoluongsp());
                            exist = true;
                        }
                    }
                    if(exist == false){
                        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                        long giaMoi = soluong * giaCTSP;
                        mangGioHang.add(new gioHang(id, tenCTSP, giaMoi, hinhanhCTSP, soluong));
                    }
                }else {
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long giaMoi = soluong * giaCTSP;
                    mangGioHang.add(new gioHang(id, tenCTSP, giaMoi, hinhanhCTSP, soluong));
                }
                Intent intent = new Intent(getApplicationContext(), GioHang.class);
                startActivity(intent);
            }
        });
    }

    private void catchEventSpinner() {
        Integer[] soluong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, soluong );
        spinner.setAdapter(arrayAdapter);
    }

    private void getInformation() {
        sanPham sp = (sanPham) getIntent().getSerializableExtra("thongtinsanpham"); // truyen theo key la motasanpham | trong class sanPham them vao Serializable
        id = sp.getID();
        tenCTSP = sp.getTenSanPham();
        giaCTSP = sp.getGiaSanPham();
        hinhanhCTSP = sp.getHinhAnhSanPham();
        motaCTSP = sp.getMoTaSanPham();

        textViewTen.setText(tenCTSP);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        textViewGia.setText("Giá: " + decimalFormat.format(sp.getGiaSanPham()) + " Đ");
        textViewMoTa.setText(sp.getMoTaSanPham());
        Picasso.with(getApplicationContext()).load(hinhanhCTSP)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imageViewCT);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarCT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCT.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbarCT = (Toolbar) findViewById(R.id.toolbarChiTietSP);
        imageViewCT = (ImageView) findViewById(R.id.imageViewAnhCTSP);
        textViewTen = (TextView) findViewById(R.id.textViewTenCTSP);
        textViewGia = (TextView) findViewById(R.id.textViewGiaCTSP);
        textViewMoTa = (TextView) findViewById(R.id.textviewMoTaCTSP);
        spinner = (Spinner) findViewById(R.id.spinnerCTSP);
        buttonDatMua = (Button) findViewById(R.id.btnDatMua);

    }


}

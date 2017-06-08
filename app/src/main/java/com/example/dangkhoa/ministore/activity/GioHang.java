package com.example.dangkhoa.ministore.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.adapter.AdapterGioHang;
import com.example.dangkhoa.ministore.util.checkConnection;

import java.text.DecimalFormat;

public class GioHang extends AppCompatActivity {

    ListView listViewGH;
    TextView textViewTB;
    static TextView textViewTongTien;
    Button btnthanhtoan, btntieptuc;
    Toolbar toolbarGH;
    AdapterGioHang adapterGioHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        AnhXa();
        ActionToolbar();
        CheckData();
        EventUltil();
        CatchOnItemListView();
        EventButton();
    }

    private void EventButton() {
        btntieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mangGioHang.size() > 0){
                    Intent intent = new Intent(getApplicationContext(), ThongTinKhachHang.class);
                    intent.putExtra("hoten", MainActivity.fbName);
                    intent.putExtra("email", MainActivity.fbEmail);
                    startActivity(intent);
                }else{
                    checkConnection.ShowToast(getApplicationContext(), "Giỏ hàng của bạn vẫn còn trống");
                }
            }
        });
    }

    //Xoa san phem trong listview
    private void CatchOnItemListView() {
        listViewGH.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GioHang.this);
                builder.setTitle("Xác nhận xóa sản phẩm");
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm này? ");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(MainActivity.mangGioHang.size() <=0){
                            textViewTB.setVisibility(View.VISIBLE);
                        }else{
                            MainActivity.mangGioHang.remove(position);
                            adapterGioHang.notifyDataSetChanged();
                            EventUltil();
                            if(MainActivity.mangGioHang.size() <= 0 ){
                                textViewTB.setVisibility(View.VISIBLE);
                            }else{
                                textViewTB.setVisibility(View.INVISIBLE);
                                adapterGioHang.notifyDataSetChanged();
                                EventUltil();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterGioHang.notifyDataSetChanged();
                        EventUltil();
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    //get ra tong tien|su dung lai function
    public static void EventUltil() {
        long tongtien = 0;
        for(int i = 0; i < MainActivity.mangGioHang.size(); i ++){
            tongtien += MainActivity.mangGioHang.get(i).getGiasp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        textViewTongTien.setText(decimalFormat.format(tongtien) + " Đ");
    }

    private void CheckData() {
        if(MainActivity.mangGioHang.size() <=0 ){
            adapterGioHang.notifyDataSetChanged();
            textViewTB.setVisibility(View.VISIBLE);
            listViewGH.setVisibility(View.INVISIBLE);
        }else{
            adapterGioHang.notifyDataSetChanged();
            textViewTB.setVisibility(View.INVISIBLE);
            listViewGH.setVisibility(View.VISIBLE);
        }
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarGH);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGH.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        listViewGH = (ListView) findViewById(R.id.listviewGioHang);
        textViewTB = (TextView) findViewById(R.id.textviewThongBao);
        textViewTongTien = (TextView) findViewById(R.id.textviewTongTien);
        btnthanhtoan = (Button) findViewById(R.id.btnThanhToanGioHang);
        btntieptuc = (Button) findViewById(R.id.btnTiepTucMuaHang);
        toolbarGH = (Toolbar) findViewById(R.id.toolbarGioHang);
        adapterGioHang = new AdapterGioHang(GioHang.this, MainActivity.mangGioHang);
        listViewGH.setAdapter(adapterGioHang);
    }
}

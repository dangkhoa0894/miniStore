package com.example.dangkhoa.ministore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.adapter.AdapterLaptop;
import com.example.dangkhoa.ministore.model.sanPham;
import com.example.dangkhoa.ministore.util.Server;
import com.example.dangkhoa.ministore.util.checkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LaptopActivity extends AppCompatActivity {

    Toolbar toolbarLaptop;
    ListView listViewLaptop;
    AdapterLaptop adapterLaptop;
    ArrayList<sanPham> arrayLaptop;
    int idLaptop = 0;
    int page = 1;
    View footerView;
    boolean isloading = false; //crash
    mHandler myHandler;
    boolean limitData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        Anhxa();
        getIDLoaiSP();
        ActionToolbar();
        getData(page);
        loadMoreData();
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

    private void loadMoreData() {
        listViewLaptop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham", arrayLaptop.get(position));
                startActivity(intent);
            }
        });
        listViewLaptop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstItem, int visibleItem, int totalItem) {
                if(firstItem + visibleItem == totalItem && totalItem != 0 && isloading == false && limitData == false ){
                    isloading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();

                }
            }
        });
    }

    private void getData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.urlDienThoai + String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String tenLT = "";
                int giaLT = 0;
                String hinhAnhLT = "";
                String motaLT = "";
                int idspLT = 0;
                if(response != null && response.length() != 2 ){
                    listViewLaptop.removeFooterView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenLT = jsonObject.getString("tensp");
                            giaLT = jsonObject.getInt("giasp");
                            hinhAnhLT = jsonObject.getString("hinhanhsp");
                            motaLT = jsonObject.getString("motasp");
                            idspLT = jsonObject.getInt("idloaisp");
                            arrayLaptop.add(new sanPham(id, tenLT, giaLT, hinhAnhLT, motaLT, idspLT));
                            adapterLaptop.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    limitData = true;
                    listViewLaptop.removeFooterView(footerView);
                    checkConnection.ShowToast(getApplicationContext(), "Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham",String.valueOf(idLaptop));//key là column trong table
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarLaptop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLaptop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void Anhxa() {
        toolbarLaptop = (Toolbar) findViewById(R.id.toolbarLaptop);
        listViewLaptop = (ListView) findViewById(R.id.listviewLaptop);
        arrayLaptop = new ArrayList<>();
        adapterLaptop = new AdapterLaptop(getApplicationContext(),arrayLaptop);
        listViewLaptop.setAdapter(adapterLaptop);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.progressbar, null);
        myHandler = new mHandler();
    }
    private void getIDLoaiSP(){
        idLaptop = getIntent().getIntExtra("idSanPham", -1);
        Log.d("giatriloaisanpham", idLaptop + "");
    }
    public class mHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    listViewLaptop.addFooterView(footerView);
                    break;
                case 1:
                    //page ++;
                    getData(++page);
                    isloading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends Thread{
        @Override
        public void run() {
            myHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage(1);// phương thức liên kết thread với handler
            myHandler.sendMessage(message);
            super.run();
        }
    }


}

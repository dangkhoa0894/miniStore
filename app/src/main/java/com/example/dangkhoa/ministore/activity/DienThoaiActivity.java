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
import com.example.dangkhoa.ministore.adapter.AdapterDienThoai;
import com.example.dangkhoa.ministore.model.sanPham;
import com.example.dangkhoa.ministore.util.Server;
import com.example.dangkhoa.ministore.util.checkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DienThoaiActivity extends AppCompatActivity {

    Toolbar toolbarDT;
    ListView listViewDT;
    AdapterDienThoai adapterDT;
    ArrayList<sanPham> sanPhamArray;
    int idDT = 0;
    int page = 1;
    View footerView;
    boolean isloading = false; //crash
    mHandler myHandler;
    boolean limitData = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        Anhxa();
        if(checkConnection.haveNetworkConnection(getApplicationContext())){
            getIDLoaiSP();
            ActionToolbar();
            getData(page);
            loadMoreData();
        }else{
            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối của bạn.");
            finish();
        }

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
        listViewDT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham", sanPhamArray.get(position));
                startActivity(intent);
            }
        });
        listViewDT.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                String tenDT = "";
                int giaDT = 0;
                String hinhAnhDT = "";
                String motaDT = "";
                int idspDT = 0;
                if(response != null && response.length() != 2 ){
                    listViewDT.removeFooterView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenDT = jsonObject.getString("tensp");
                            giaDT = jsonObject.getInt("giasp");
                            hinhAnhDT = jsonObject.getString("hinhanhsp");
                            motaDT = jsonObject.getString("motasp");
                            idspDT = jsonObject.getInt("idloaisp");
                            sanPhamArray.add(new sanPham(id, tenDT, giaDT, hinhAnhDT, motaDT, idspDT));
                            adapterDT.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    limitData = true;
                    listViewDT.removeFooterView(footerView);
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
                param.put("idsanpham",String.valueOf(idDT));//key là column trong table
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarDT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDT.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbarDT = (Toolbar) findViewById(R.id.toolbarDienThoai);
        listViewDT = (ListView) findViewById(R.id.listviewDienThoai);
        sanPhamArray = new ArrayList<>();
        adapterDT = new AdapterDienThoai(getApplicationContext(),sanPhamArray);
        listViewDT.setAdapter(adapterDT);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.progressbar, null);
        myHandler = new mHandler();


    }

    public void getIDLoaiSP() {
        idDT = getIntent().getIntExtra("idSanPham", -1);
        Log.d("giatriloaisanpham", idDT + "");
    }

    public class mHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    listViewDT.addFooterView(footerView);
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
